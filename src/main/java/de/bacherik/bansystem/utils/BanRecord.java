package de.bacherik.bansystem.utils;

import de.bacherik.bansystem.Main;

import java.time.Duration;
import java.time.LocalDateTime;

public class BanRecord {


    private final String uuid;
    private final String bannedBy;
    private final String reason;
    private final LocalDateTime start;
    private final LocalDateTime end;

    private final boolean isUnbanned;
    private String unbannedBy;
    private String unbanReason;
    private LocalDateTime unbanTime;

    public BanRecord(String uuid, String bannedBy, String reason, LocalDateTime start, LocalDateTime end, boolean isUnbanned) {
        this.uuid = uuid;
        this.bannedBy = bannedBy;
        this.reason = reason;
        this.start = start;
        this.end = end;
        this.isUnbanned = isUnbanned;
    }

    public BanRecord(String uuid, String bannedBy, String reason, LocalDateTime start, LocalDateTime end,
                     boolean isUnbanned, String unbannedBy, String unbanReason, LocalDateTime unbanTime) {
        this.uuid = uuid;
        this.bannedBy = bannedBy;
        this.reason = reason;
        this.start = start;
        this.end = end;
        this.isUnbanned = isUnbanned;
        this.unbannedBy = unbannedBy;
        this.unbanReason = unbanReason;
        this.unbanTime = unbanTime;
    }

    public String getUuid() {
        return uuid;
    }

    public String getBannedBy() {
        return bannedBy;
    }

    public String getReason() {
        return reason;
    }

    public String getStart() {
        return start.format(TimeHelper.formatter);
    }

    public String getEnd() {
        if ((Duration.between(LocalDateTime.now(), end).toDays() / 365) > 100) {
            return Main.getInstance().getMessagesConfig().get("bansystem.timeformat.permanent");
        }
        return end.format(TimeHelper.formatter);
    }

    public LocalDateTime getStartDate() {
        return start;
    }

    public LocalDateTime getEndDate() {
        return end;
    }

    public String getDuration() {
        return TimeHelper.getDifference(start, end);
    }

    public String getRemaining() {
        return TimeHelper.getDifference(LocalDateTime.now(), end);
    }

    public boolean isUnbanned() {
        return isUnbanned;
    }

    public String getUnbannedBy() {
        return unbannedBy;
    }

    public String getUnbanReason() {
        return unbanReason;
    }

    public String getUnbanTime() {
        return unbanTime.format(TimeHelper.formatter);
    }
}
