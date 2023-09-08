package de.bacherik.bansystem.config;

public class MessagesGermanConfig extends MessagesConfig {

    public MessagesGermanConfig(String name, String path) {
        super(name, path);
    }

    @Override
    public void loadMessages() {
        messages.put("bansystem.prefix", "&4BanSystem &7» ");
        messages.put("bansystem.consolename", "CONSOLE");
        messages.put("bansystem.playernotfound", "&cSpieler %PLAYER% nicht gefunden.");
        messages.put("bansystem.alreadybanned", "&cSpieler %PLAYER% ist bereits gebannt.\n" +
                "Benutze /baninfo %PLAYER%");
        messages.put("bansystem.help",
                "&7&m-----------------&r&e« BanSystem »&7&m-----------------\n\n" +
                        "&7/ban &a<Spieler> [<Zeit>] <Grund> &8- &7Banne einen Spieler\n" +
                        "&7/ban &a<Spieler> #<template-id> &8- &7Banne einen Spieler\n" +
                        "&7/unban &a<Spieler> &8- &7Entbanne einen Spieler\n" +
                        "&7/baninfo &a<Spieler> &8-&7Zeige die Ban-Historie eines Spielers\n" +
                        "&7/clearbans &a<Spieler> &8- &7Lösche alle Ban-Einträge\n" +
                        "&7/bantemplate &alist &8- &7Zeige Ban-Vorlagen\n" +
                        "&7/bantemplate &aadd <Zeit> <Grund> &8- &7Bannvorlage hinzufügen\n" +
                        "&7/bantemplate &aedit <id> <Zeit> <Grund> &8- &7Bannvorlage bearbeiten\n" +
                        "&7/bantemplate &aremove <id> &8- &7Entferne eine Bannvorlage\n" +
                        "&7/banreload &8- &7Lade die Nachrichten aus der messages.yml neu\n\n" +
                        "&7&m-----------------&r&e« BanSystem »&7&m-----------------");
        messages.put("bansystem.ban.syntax", "&cSyntax: /ban <Spieler> [<Zeit>] <Grund>");
        messages.put("bansystem.ban.successful",
                "&7Spieler &c%PLAYER% &7wurde von &c%BANNED_BY% &7gebannt\n" +
                        "&7Grund: &c%REASON%\n" +
                        "&7Zeit: &c%TIME%");
        messages.put("bansystem.unban.syntax", "&cSyntax: /unban <Spieler> [<Grund>]");
        messages.put("bansystem.unban.notbanned", "&cSpieler &e%PLAYER% &cist nicht gebannt.");
        messages.put("bansystem.unban.successful", "&7Spieler &e%PLAYER% &7wurde von &e%UNBANNED_BY% &7entbannt" +
                "\n&7Grund: &e%REASON%");
        messages.put("bansystem.baninfo.syntax", "&cSyntax: /baninfo <Spieler>");
        messages.put("bansystem.baninfo.nocurrentban", "&cSpieler %PLAYER% ist aktuell nicht gebannt.");

        messages.put("bansystem.baninfo.currentban", "&8------- &eAktueller Ban von &e%PLAYER% &8-------\n" +
                "&7Grund: &e%REASON%\n" +
                "&7Gebannt von: &e%BANNED_BY%\n" +
                "&7Gebannt am: &e%BAN_START%\n" +
                "&7Gebannt bis: &e%BAN_END%\n" +
                "&7Ban Dauer: &e%DURATION%\n" +
                "&7Verbleibende Zeit: &e%REMAINING_TIME%\n" +
                "&8------- &eAktueller Ban von &e%PLAYER% &8-------");
        messages.put("bansystem.baninfo.nopastban", "&cSpieler %PLAYER% hat keine vorherigen Bans.");
        messages.put("bansystem.baninfo.pastbanUnbanned",
                "&8------- &eFrüherer Ban von &e%PLAYER% &7(&e%INDEX%&7) &8-------\n" +
                        "&7Grund: &e%REASON%\n" +
                        "&7Gebannt von: &e%BANNED_BY%\n" +
                        "&7Gebannt am: &e%BAN_START%\n" +
                        "&7Gebannt bis: &e%BAN_END%\n" +
                        "&7Ban Dauer: &e%DURATION%\n" +
                        "&7Entbannt: &aJA\n" +
                        "&7Entbannt von: &e%UNBANNED_BY%\n" +
                        "&7Entbannter Grund: &e%UNBAN_REASON%\n" +
                        "&7Entbannte Zeit: &e%UNBAN_TIME%\n" +
                        "&8------- &eFrüherer Ban von &e%PLAYER% &7(&e%INDEX%&7) &8-------");
        messages.put("bansystem.baninfo.pastbanNotunbanned",
                "&8------- &eFrüherer Ban von &e%PLAYER% &7(&e%INDEX%&7) &8-------\n" +
                        "&7Grund: &e%REASON%\n" +
                        "&7Gebannt von: &e%BANNED_BY%\n" +
                        "&7Gebannt am: &e%BAN_START%\n" +
                        "&7Gebannt bis: &e%BAN_END%\n" +
                        "&7Ban Dauer: &e%DURATION%\n" +
                        "&7Entbannt: &cNEIN\n" +
                        "&8------- &eFrüherer Ban von &e%PLAYER% &7(&e%INDEX%&7) &8-------");
        messages.put("bansystem.clearbans.syntax", "&cSyntax: /clearbans <Spieler>");
        messages.put("bansystem.clearbans.successful", "&4!! &7Ban Einträge von &e%PLAYER% &7wurden von &e%UNBANNED_BY% &7gelöscht &4!!");
        messages.put("bansystem.template.syntax",
                "&cSyntaxfehler. Möglichkeiten:\n" +
                        "&7/bantemplate &alist &8- &7Liste vorhandener Bannvorlagen\n" +
                        "&7/bantemplate &aadd <Zeit> <Grund> &8- &7Bannvorlage hinzufügen\n" +
                        "&7/bantemplate &aedit <ID> <Zeit> <Grund> &8- &7Bannvorlage bearbeiten\n" +
                        "&7/bantemplate &aremove <ID> &8- &7Bannvorlage entfernen");
        messages.put("bansystem.template.list.header", "&7------------------- &eBannvorlagen &7-------------------");
        messages.put("bansystem.template.list.format.head", "%-10s%-20s%-45s");
        messages.put("bansystem.template.list.format.content", "%-10s%-18s%-45s");
        messages.put("bansystem.template.list.id.head", "&eID");
        messages.put("bansystem.template.list.id.content", "&6%ID%");
        messages.put("bansystem.template.list.time.head", "&eZeit");
        messages.put("bansystem.template.list.time.content", "&c%TIME%");
        messages.put("bansystem.template.list.reason.head", "&eGrund");
        messages.put("bansystem.template.list.reason.content", "&a%REASON%");
        messages.put("bansystem.template.list.footer", "&7------------------- &eBannvorlagen &7-------------------");
        messages.put("bansystem.template.add.successful", "&aBannvorlage hinzugefügt!");
        messages.put("bansystem.template.add.error", "&cBannvorlage nicht gefunden!");
        messages.put("bansystem.template.edit.successful", "&aBannvorlage bearbeitet!");
        messages.put("bansystem.template.remove.successful", "&aBannvorlage entfernt!");

        messages.put("bansystem.banmessage",
                "&cDu wurdest vom XY Netzwerk gebannt.\n" +
                        "&eGrund: &c%REASON%\n" +
                        "&eVerbleibende Zeit: &c%REMAINING_TIME%");
        messages.put("bansystem.timeformat.years", " Jahre ");
        messages.put("bansystem.timeformat.year", " Jahr ");
        messages.put("bansystem.timeformat.days", " Tage ");
        messages.put("bansystem.timeformat.day", " Tag ");
        messages.put("bansystem.timeformat.hours", " Stunden ");
        messages.put("bansystem.timeformat.hour", " Stunde ");
        messages.put("bansystem.timeformat.minutes", " Minuten ");
        messages.put("bansystem.timeformat.minute", " Minute ");
        messages.put("bansystem.timeformat.seconds", " Sekunden ");
        messages.put("bansystem.timeformat.second", " Sekunde ");
        messages.put("bansystem.timeformat.permanent", "PERMANENT");
    }
}
