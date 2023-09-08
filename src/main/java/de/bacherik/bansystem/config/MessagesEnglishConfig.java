package de.bacherik.bansystem.config;

public class MessagesEnglishConfig extends MessagesConfig {

    public MessagesEnglishConfig(String name, String path) {
        super(name, path);
    }

    @Override
    public void loadMessages() {
        messages.put("bansystem.prefix", "&4BanSystem &7» ");
        messages.put("bansystem.consolename", "CONSOLE");
        messages.put("bansystem.playernotfound", "&cPlayer %PLAYER% not found.");
        messages.put("bansystem.alreadybanned", "&cPlayer %PLAYER% is already banned.\n" +
                "Use /baninfo %PLAYER%");
        messages.put("bansystem.help",
                "&7&m-----------------&r&e« BanSystem »&7&m-----------------\n\n" +
                        "&7/ban &a<Player> [<Time>] <Reason> &8- &7Ban a player\n" +
                        "&7/ban &a<Player> #<template-id> &8- &7Ban a player\n" +
                        "&7/unban &a<Player> &8- &7Unban a player\n" +
                        "&7/baninfo &a<Player> &8-&7View player's ban history\n" +
                        "&7/clearbans &a<Player> &8- &7Clear all ban entries\n" +
                        "&7/bantemplate &alist &8- &7Show ban templates\n" +
                        "&7/bantemplate &aadd <time> <reason> &8- &7Add ban template\n" +
                        "&7/bantemplate &aedit <time> <reason> &8- &7Edit ban template\n" +
                        "&7/bantemplate &aremove <id> &8- &7Remove ban template\n" +
                        "&7/banreload &8- &7Reload messages from config\n\n" +
                        "&7&m-----------------&r&e« BanSystem »&7&m-----------------");
        messages.put("bansystem.ban.syntax", "&cSyntax: /ban <Player> [<Time>] <Reason>");
        messages.put("bansystem.ban.successful",
                "&7Player &c%PLAYER% &7was banned by &c%BANNED_BY%\n" +
                        "&7Reason: &c%REASON%\n" +
                        "&7Time: &c%TIME%");
        messages.put("bansystem.unban.syntax", "&cSyntax: /unban <Player> [<Reason>]");
        messages.put("bansystem.unban.notbanned", "&cPlayer &e%PLAYER% &cis not banned.");
        messages.put("bansystem.unban.successful", "&7Player &e%PLAYER% &7was unbanned by &e%UNBANNED_BY%" +
                "\n&7Reason: &e%REASON%");
        messages.put("bansystem.baninfo.syntax", "&cSyntax: /baninfo <Player>");
        messages.put("bansystem.baninfo.nocurrentban", "&cPlayer %PLAYER% is not banned currently.");
        messages.put("bansystem.baninfo.currentban",
                "&8------- &eCurrent ban of &e%PLAYER% &8-------\n" +
                        "&7Reason: &e%REASON%\n" +
                        "&7Banned by: &e%BANNED_BY%\n" +
                        "&7Banned on: &e%BAN_START%\n" +
                        "&7Banned until: &e%BAN_END%\n" +
                        "&7Ban duration: &e%DURATION%\n" +
                        "&7Remaining time: &e%REMAINING_TIME%\n" +
                        "&8------- &eCurrent ban of &e%PLAYER% &8-------");
        messages.put("bansystem.baninfo.nopastban", "&cPlayer %PLAYER% has no previous bans.");
        messages.put("bansystem.baninfo.pastbanUnbanned",
                "&8------- &ePrevious ban of &e%PLAYER% &7(&e%INDEX%&7) &8-------\n" +
                        "&7Reason: &e%REASON%\n" +
                        "&7Banned by: &e%BANNED_BY%\n" +
                        "&7Banned on: &e%BAN_START%\n" +
                        "&7Banned until: &e%BAN_END%\n" +
                        "&7Ban duration: &e%DURATION%\n" +
                        "&7Unbanned: &aYES\n" +
                        "&7Unbanned by: &e%UNBANNED_BY%\n" +
                        "&7Unban reason: &e%UNBAN_REASON%\n" +
                        "&7Unban time: &e%UNBAN_TIME%\n" +
                        "&8------- &ePrevious ban of &e%PLAYER% &7(&e%INDEX%&7) &8-------");
        messages.put("bansystem.baninfo.pastbanNotunbanned",
                "&8------- &ePrevious ban of &e%PLAYER% &7(&e%INDEX%&7) &8-------\n" +
                        "&7Reason: &e%REASON%\n" +
                        "&7Banned by: &e%BANNED_BY%\n" +
                        "&7Banned on: &e%BAN_START%\n" +
                        "&7Banned until: &e%BAN_END%\n" +
                        "&7Ban duration: &e%DURATION%\n" +
                        "&7Unbanned: &cNO\n" +
                        "&8------- &ePrevious ban of &e%PLAYER% &7(&e%INDEX%&7) &8-------");
        messages.put("bansystem.clearbans.syntax", "&cSyntax: /clearbans <Player>");
        messages.put("bansystem.clearbans.successful", "&4!! &7Ban entries of &e%PLAYER% &7were cleared by &e%UNBANNED_BY% &4!!");
        messages.put("bansystem.template.syntax",
                "&cSyntax error. Possibilities:\n" +
                        "&7/bantemplate &alist &8- &7list existing ban templates\n" +
                        "&7/bantemplate &aadd <time> <reason> &8- &7add ban template\n" +
                        "&7/bantemplate &aedit <id> <time> <reason> &8- &7edit ban template\n" +
                        "&7/bantemplate &aremove <id> &8- &7remove ban template");
        messages.put("bansystem.template.list.header", "&7------------------- &eBan Templates &7-------------------");
        messages.put("bansystem.template.list.format.head", "%-10s%-20s%-45s");
        messages.put("bansystem.template.list.format.content", "%-10s%-18s%-45s");
        messages.put("bansystem.template.list.id.head", "&eID");
        messages.put("bansystem.template.list.id.content", "&6%ID%");
        messages.put("bansystem.template.list.time.head", "&eTime");
        messages.put("bansystem.template.list.time.content", "&c%TIME%");
        messages.put("bansystem.template.list.reason.head", "&eReason");
        messages.put("bansystem.template.list.reason.content", "&a%REASON%");
        messages.put("bansystem.template.list.footer", "&7------------------- &eBan Templates &7-------------------");
        messages.put("bansystem.template.add.successful", "&aBan Template added!");
        messages.put("bansystem.template.add.error", "&cBan template not found!");
        messages.put("bansystem.template.edit.successful", "&aBan Template edited!");
        messages.put("bansystem.template.remove.successful", "&aBan Template removed!");

        messages.put("bansystem.banmessage",
                "&cYou have been banned from the XY Network.\n" +
                        "&eReason: &c%REASON%\n" +
                        "&eRemaining time: &c%REMAINING_TIME%");
        messages.put("bansystem.timeformat.years", " Years ");
        messages.put("bansystem.timeformat.year", " Year ");
        messages.put("bansystem.timeformat.days", " Days ");
        messages.put("bansystem.timeformat.day", " Day ");
        messages.put("bansystem.timeformat.hours", " Hours ");
        messages.put("bansystem.timeformat.hour", " Hour ");
        messages.put("bansystem.timeformat.minutes", " Minutes ");
        messages.put("bansystem.timeformat.minute", " Minute ");
        messages.put("bansystem.timeformat.seconds", " Seconds ");
        messages.put("bansystem.timeformat.second", " Second ");
        messages.put("bansystem.timeformat.permanent", "PERMANENT");
    }
}
