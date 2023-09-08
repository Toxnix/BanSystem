package de.bacherik.bansystem.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Consumer;

public class MySQL {

    private final HikariDataSource dataSource;

    public MySQL(String host, int port, String username, String password, String database) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true");
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);

        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS currentBans(" +
                    "UUID VARCHAR(40) PRIMARY KEY, " +
                    "bannedBy VARCHAR(40), " +
                    "reason VARCHAR(255), " +
                    "startTime DATETIME, " +
                    "endTime DATETIME)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS pastBans(" +
                    "UUID VARCHAR(40), " +
                    "bannedBy VARCHAR(40), " +
                    "reason VARCHAR(255), " +
                    "startTime DATETIME, " +
                    "endTime DATETIME, " +
                    "PRIMARY KEY(UUID, startTime))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS unbans(" +
                    "UUID VARCHAR(40), " +
                    "unbannedBy VARCHAR(40), " +
                    "unbanReason VARCHAR(255), " +
                    "banTime DATETIME, " +
                    "unbanTime DATETIME, " +
                    "PRIMARY KEY(UUID, banTime))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS banTemplates(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "time VARCHAR(8)," +
                    "reason VARCHAR(128))");

            ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '" + database + "' AND table_name = 'users'");
            boolean tableExists = false;
            if (resultSet.next()) {
                tableExists = resultSet.getInt(1) > 0;
            }
            resultSet.close();

            if (tableExists) {
                // Check if the columns exist and add them if not
                if (!columnExists("canMute")) {
                    stmt.executeUpdate("ALTER TABLE users ADD COLUMN canMute BOOLEAN DEFAULT FALSE");
                }
                if (!columnExists("canUnmute")) {
                    stmt.executeUpdate("ALTER TABLE users ADD COLUMN canUnmute BOOLEAN DEFAULT FALSE");
                }
                if (!columnExists("canDeletePastMutes")) {
                    stmt.executeUpdate("ALTER TABLE users ADD COLUMN canDeletePastMutes BOOLEAN DEFAULT FALSE");
                }
            } else {
                // Create the table if it doesn't exist
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users(" +
                        "uuid VARCHAR(36) PRIMARY KEY, " +
                        "password VARCHAR(255), " +
                        "canBan BOOLEAN, " +
                        "canUnban BOOLEAN, " +
                        "canDeletePastBans BOOLEAN, " +
                        "canMute BOOLEAN, " +
                        "canUnmute BOOLEAN, " +
                        "canDeletePastMutes BOOLEAN, " +
                        "canEditUsers BOOLEAN);");
            }


            stmt.close();
        } catch (SQLException e) {
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§c[BanSystem] MySQL Connection could not be established. Error:"));
            e.printStackTrace();
        }
    }

    public void close() {
        dataSource.close();
    }

    public BanRecord getBan(String uuid) {
        BanRecord banRecord = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM currentBans WHERE UUID = ?")) {
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LocalDateTime start = rs.getTimestamp("startTime").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("endTime").toLocalDateTime();

                String bannedBy = rs.getString("bannedBy");
                String reason = rs.getString("reason");

                if (LocalDateTime.now().minusSeconds(2).isAfter(end)) {
                    try (PreparedStatement deleteStmt = con.prepareStatement("DELETE FROM currentBans WHERE UUID = ?");
                         PreparedStatement insertStmt = con.prepareStatement("INSERT INTO pastBans VALUES (?, ?, ?, ?, ?)")) {
                        deleteStmt.setString(1, uuid);
                        deleteStmt.executeUpdate();
                        insertStmt.setString(1, uuid);
                        insertStmt.setString(2, bannedBy);
                        insertStmt.setString(3, reason);
                        insertStmt.setTimestamp(4, Timestamp.valueOf(start));
                        insertStmt.setTimestamp(5, Timestamp.valueOf(end));
                        insertStmt.executeUpdate();
                    }
                } else {
                    banRecord = new BanRecord(uuid, bannedBy, reason, start, end, false);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return banRecord;
    }

    public void getBan(String uuid, Consumer<BanRecord> banRecord) {
        Util.pool.execute(() -> banRecord.accept(getBan(uuid)));
    }

    public ArrayList<BanRecord> getPastBans(String uuid) {
        ArrayList<BanRecord> pastBanRecords = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT pastBans.UUID, bannedBy, reason, startTime, " +
                     "endTime, (unbannedBy IS NOT NULL) isUnbanned, unbannedBy, unbanReason, unbanTime " +
                     "FROM pastBans " +
                     "LEFT JOIN unbans ON pastBans.UUID = unbans.UUID AND pastBans.startTime = unbans.banTime " +
                     "WHERE pastBans.uuid=?")) {
            stmt.setString(1, uuid);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String bannedBy = rs.getString("bannedBy");
                    String reason = rs.getString("reason");
                    LocalDateTime start = rs.getTimestamp("startTime").toLocalDateTime();
                    LocalDateTime end = rs.getTimestamp("endTime").toLocalDateTime();
                    boolean isUnbanned = rs.getBoolean(6);
                    BanRecord banRecord = isUnbanned ?
                            new BanRecord(uuid, bannedBy, reason, start, end, true,
                                    rs.getString("unbannedBy"), rs.getString("unbanReason"),
                                    rs.getTimestamp("unbanTime").toLocalDateTime()) :
                            new BanRecord(uuid, bannedBy, reason, start, end, false);
                    pastBanRecords.add(banRecord);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!pastBanRecords.isEmpty()) {
            pastBanRecords.sort(Comparator.comparing(BanRecord::getStartDate));
            return pastBanRecords;
        }
        return null;
    }

    public void getPastBans(String uuid, Consumer<ArrayList<BanRecord>> banRecords) {
        Util.pool.execute(() -> banRecords.accept(getPastBans(uuid)));
    }

    public void ban(String uuid, String bannedBy, String reason, LocalDateTime start, LocalDateTime end) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement("INSERT INTO " +
                     "currentBans (UUID, bannedBy, reason, startTime, endTime) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setString(1, uuid);
            pstmt.setString(2, bannedBy);
            pstmt.setString(3, reason);
            pstmt.setTimestamp(4, Timestamp.valueOf(start));
            pstmt.setTimestamp(5, Timestamp.valueOf(end));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void banAsync(String uuid, String bannedBy, String reason, LocalDateTime start, LocalDateTime end) {
        Util.pool.execute(() -> ban(uuid, bannedBy, reason, start, end));
    }

    public void unban(String uuid, String bannedByUuid, String reason, LocalDateTime banStart,
                      LocalDateTime banEnd, String unbannedByUuid, String unbanReason, LocalDateTime unbanTime) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement deleteCurrentBansStmt = conn.prepareStatement("DELETE FROM currentBans WHERE UUID = ?");
             PreparedStatement insertPastBansStmt = conn.prepareStatement("INSERT INTO pastBans VALUES (?, ?, ?, ?, ?)");
             PreparedStatement insertUnbansStmt = conn.prepareStatement("INSERT INTO unbans VALUES (?, ?, ?, ?, ?)")) {

            conn.setAutoCommit(false);

            deleteCurrentBansStmt.setString(1, uuid);
            deleteCurrentBansStmt.executeUpdate();

            insertPastBansStmt.setString(1, uuid);
            insertPastBansStmt.setString(2, bannedByUuid);
            insertPastBansStmt.setString(3, reason);
            insertPastBansStmt.setTimestamp(4, Timestamp.valueOf(banStart));
            insertPastBansStmt.setTimestamp(5, Timestamp.valueOf(banEnd));
            insertPastBansStmt.executeUpdate();

            insertUnbansStmt.setString(1, uuid);
            insertUnbansStmt.setString(2, unbannedByUuid);
            insertUnbansStmt.setString(3, unbanReason);
            insertUnbansStmt.setTimestamp(4, Timestamp.valueOf(banStart));
            insertUnbansStmt.setTimestamp(5, Timestamp.valueOf(unbanTime));
            insertUnbansStmt.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unbanAsync(String uuid, String bannedByUuid, String reason, LocalDateTime banStart,
                           LocalDateTime banEnd, String unbannedByUuid, String unbanReason, LocalDateTime unbanTime) {
        Util.pool.execute(() -> unban(uuid, bannedByUuid, reason, banStart, banEnd, unbannedByUuid, unbanReason, unbanTime));
    }

    public void clearBans(String uuid) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement deleteCurrentBansStmt = con.prepareStatement("DELETE FROM currentBans WHERE UUID = ?");
             PreparedStatement deletePastBansStmt = con.prepareStatement("DELETE FROM pastBans WHERE UUID = ?");
             PreparedStatement deleteUnbansStmt = con.prepareStatement("DELETE FROM unbans WHERE UUID = ?")) {

            deleteCurrentBansStmt.setString(1, uuid);
            deletePastBansStmt.setString(1, uuid);
            deleteUnbansStmt.setString(1, uuid);

            deleteCurrentBansStmt.executeUpdate();
            deletePastBansStmt.executeUpdate();
            deleteUnbansStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BanTemplate> getBanTemplates() {
        ArrayList<BanTemplate> banTemplates = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM banTemplates ORDER BY id");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                banTemplates.add(new BanTemplate(rs.getInt("id"), rs.getString("time"), rs.getString("reason")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return banTemplates;
    }

    public BanTemplate getBanTemplate(int id) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM banTemplates WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new BanTemplate(rs.getInt("id"), rs.getString("time"), rs.getString("reason"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getBanTemplateAsync(int id, Consumer<BanTemplate> banTemplate) {
        Util.pool.execute(() -> banTemplate.accept(getBanTemplate(id)));
    }

    public void addBanTemplate(String time, String reason) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement("INSERT INTO banTemplates (time, reason) VALUES (?, ?)")) {
            pstmt.setString(1, time);
            pstmt.setString(2, reason);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editBanTemplate(int id, String newTime, String newReason) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement deleteTemplateStmt = con.prepareStatement("UPDATE banTemplates SET time = ?, reason= ? WHERE id = ?")) {
            deleteTemplateStmt.setString(1, newTime);
            deleteTemplateStmt.setString(2, newReason);
            deleteTemplateStmt.setInt(3, id);
            deleteTemplateStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBanTemplate(int id) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement deleteTemplateStmt = con.prepareStatement("DELETE FROM banTemplates WHERE id = ?")) {
            deleteTemplateStmt.setInt(1, id);
            deleteTemplateStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean columnExists(String columnName) throws SQLException {
        DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
        try (ResultSet rs = metaData.getColumns(null, null, "users", columnName)) {
            return rs.next();
        }
    }
}
