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
        messages.put("bansystem.alreadymuted", "&cPlayer %PLAYER% is already muted.\n" +
                "Use /muteinfo %PLAYER%");
        messages.put("bansystem.help",
                "&7&m-----------------&r&e« BanSystem »&7&m-----------------\n\n" +
                        "&7/ban &a<Player> [<Time>] <Reason> &8- &7Ban a player\n" +
                        "&7/ban &a<Player> #<template-id> &8- &7Ban a player\n" +
                        "&7/mute &a<Player> [<Time>] <Reason> &8- &7Mute a player\n" +
                        "&7/mute &a<Player> #<template-id> &8- &7Mute a player\n" +
                        "&7/unban &a<Player> &8- &7Unban a player\n" +
                        "&7/unmute &a<Player> &8- &7Unmute a player\n" +
                        "&7/baninfo &a<Player> &8-&7View player's ban history\n" +
                        "&7/muteinfo &a<Player> &8-&7View player's mute history\n" +
                        "&7/clearbans &a<Player> &8- &7Clear all ban entries form a player\n" +
                        "&7/clearmutes &a<Player> &8- &7Clear all mute entries from a player\n" +
                        "&7/bantemplate &alist &8- &7Show ban templates\n" +
                        "&7/bantemplate &aadd <time> <reason> &8- &7Add ban template\n" +
                        "&7/bantemplate &aedit <time> <reason> &8- &7Edit ban template\n" +
                        "&7/bantemplate &aremove <id> &8- &7Remove ban template\n" +
                        "&7/mutetemplate &alist &8- &7Show mute templates\n" +
                        "&7/mutetemplate &aadd <time> <reason> &8- &7Add mute template\n" +
                        "&7/mutetemplate &aedit <time> <reason> &8- &7Edit mute template\n" +
                        "&7/mutetemplate &aremove <id> &8- &7Remove mute template\n" +
                        "&7/banreload &8- &7Reload messages from config\n\n" +
                        "&7&m-----------------&r&e« BanSystem »&7&m-----------------");
        messages.put("bansystem.ban.syntax", "&cSyntax: /ban <Player> [<Time>] <Reason>");
        messages.put("bansystem.mute.syntax", "&cSyntax: /mute <Player> [<Time>] <Reason>");
        messages.put("bansystem.ban.successful",
                "&7Player &c%PLAYER% &7was banned by &c%BANNED_BY%\n" +
                        "&7Reason: &c%REASON%\n" +
                        "&7Time: &c%TIME%");
        messages.put("bansystem.mute.successful",
                "&7Player &c%PLAYER% &7was muted by &c%MUTED_BY%\n" +
                        "&7Reason: &c%REASON%\n" +
                        "&7Time: &c%TIME%");
        messages.put("bansystem.ban.nopermission", "&cYou do not have permission to ban the player for the specified time!");
        messages.put("bansystem.mute.nopermission", "&cYou do not have permission to mute the player for the specified time!");
        messages.put("bansystem.unban.syntax", "&cSyntax: /unban <Player> [<Reason>]");
        messages.put("bansystem.unmute.syntax", "&cSyntax: /unmute <Player> [<Reason>]");
        messages.put("bansystem.unban.notbanned", "&cPlayer &e%PLAYER% &cis not banned.");
        messages.put("bansystem.unmute.notmuted", "&cPlayer &e%PLAYER% &cis not muted.");
        messages.put("bansystem.unban.successful", "&7Player &e%PLAYER% &7was unbanned by &e%UNBANNED_BY%" +
                "\n&7Reason: &e%REASON%");
        messages.put("bansystem.unmute.successful", "&7Player &e%PLAYER% &7was unmuted by &e%UNMUTED_BY%" +
                "\n&7Reason: &e%REASON%");
        messages.put("bansystem.baninfo.syntax", "&cSyntax: /baninfo <Player>");
        messages.put("bansystem.muteinfo.syntax", "&cSyntax: /muteinfo <Player>");
        messages.put("bansystem.baninfo.nocurrentban", "&cPlayer %PLAYER% is not banned currently.");
        messages.put("bansystem.muteinfo.nocurrentmute", "&cPlayer %PLAYER% is not muted currently.");
        messages.put("bansystem.baninfo.currentban",
                "&8------- &eCurrent ban of &e%PLAYER% &8-------\n" +
                        "&7Reason: &e%REASON%\n" +
                        "&7Banned by: &e%BANNED_BY%\n" +
                        "&7Banned on: &e%BAN_START%\n" +
                        "&7Banned until: &e%BAN_END%\n" +
                        "&7Ban duration: &e%DURATION%\n" +
                        "&7Remaining time: &e%REMAINING_TIME%\n" +
                        "&8------- &eCurrent ban of &e%PLAYER% &8-------");
        messages.put("bansystem.muteinfo.currentmute",
                "&8------- &eCurrent mute of &e%PLAYER% &8-------\n" +
                        "&7Reason: &e%REASON%\n" +
                        "&7Muted by: &e%MUTED_BY%\n" +
                        "&7Muted on: &e%MUTE_START%\n" +
                        "&7Muted until: &e%MUTE_END%\n" +
                        "&7Mute duration: &e%DURATION%\n" +
                        "&7Remaining time: &e%REMAINING_TIME%\n" +
                        "&8------- &eCurrent mute of &e%PLAYER% &8-------");
        messages.put("bansystem.baninfo.nopastban", "&cPlayer %PLAYER% has no previous bans.");
        messages.put("bansystem.muteinfo.nopastmute", "&cPlayer %PLAYER% has no previous mutes.");
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
        messages.put("bansystem.muteinfo.pastmuteUnmuted",
                "&8------- &ePrevious mute of &e%PLAYER% &7(&e%INDEX%&7) &8-------\n" +
                        "&7Reason: &e%REASON%\n" +
                        "&7Muted by: &e%MUTED_BY%\n" +
                        "&7Muted on: &e%MUTE_START%\n" +
                        "&7Muted until: &e%MUTE_END%\n" +
                        "&7Mute duration: &e%DURATION%\n" +
                        "&7Unmuted: &aYES\n" +
                        "&7Unmuted by: &e%UNMUTED_BY%\n" +
                        "&7Unmute reason: &e%UNMUTE_REASON%\n" +
                        "&7Unmute time: &e%UNMUTE_TIME%\n" +
                        "&8------- &ePrevious mute of &e%PLAYER% &7(&e%INDEX%&7) &8-------");
        messages.put("bansystem.baninfo.pastbanNotunbanned",
                "&8------- &ePrevious ban of &e%PLAYER% &7(&e%INDEX%&7) &8-------\n" +
                        "&7Reason: &e%REASON%\n" +
                        "&7Banned by: &e%BANNED_BY%\n" +
                        "&7Banned on: &e%BAN_START%\n" +
                        "&7Banned until: &e%BAN_END%\n" +
                        "&7Ban duration: &e%DURATION%\n" +
                        "&7Unbanned: &cNO\n" +
                        "&8------- &ePrevious ban of &e%PLAYER% &7(&e%INDEX%&7) &8-------");
        messages.put("bansystem.muteinfo.pastmuteNotunmuted",
                "&8------- &ePrevious mute of &e%PLAYER% &7(&e%INDEX%&7) &8-------\n" +
                        "&7Reason: &e%REASON%\n" +
                        "&7Muted by: &e%MUTED_BY%\n" +
                        "&7Muted on: &e%MUTE_START%\n" +
                        "&7Muted until: &e%MUTE_END%\n" +
                        "&7Mute duration: &e%DURATION%\n" +
                        "&7Unmuted: &cNO\n" +
                        "&8------- &ePrevious mute of &e%PLAYER% &7(&e%INDEX%&7) &8-------");
        messages.put("bansystem.clearbans.syntax", "&cSyntax: /clearbans <Player>");
        messages.put("bansystem.clearmutes.syntax", "&cSyntax: /clearmutes <Player>");
        messages.put("bansystem.clearbans.successful", "&4!! &7Ban entries of &e%PLAYER% &7were cleared by &e%UNBANNED_BY% &4!!");
        messages.put("bansystem.clearmutes.successful", "&4!! &7Mute entries of &e%PLAYER% &7were cleared by &e%UNMUTED_BY% &4!!");
        messages.put("bansystem.ban.template.syntax",
                "&cSyntax error. Possibilities:\n" +
                        "&7/bantemplate &alist &8- &7list existing ban templates\n" +
                        "&7/bantemplate &aadd <time> <reason> &8- &7add ban template\n" +
                        "&7/bantemplate &aedit <id> <time> <reason> &8- &7edit ban template\n" +
                        "&7/bantemplate &aremove <id> &8- &7remove ban template");
        messages.put("bansystem.mute.template.syntax",
                "&cSyntax error. Possibilities:\n" +
                        "&7/mutetemplate &alist &8- &7list existing mute templates\n" +
                        "&7/mutetemplate &aadd <time> <reason> &8- &7add mute template\n" +
                        "&7/mutetemplate &aedit <id> <time> <reason> &8- &7edit mute template\n" +
                        "&7/mutetemplate &aremove <id> &8- &7remove mute template");
        messages.put("bansystem.ban.template.list.header", "&7------------------- &eBan Templates &7-------------------");
        messages.put("bansystem.mute.template.list.header", "&7------------------- &eMute Templates &7-------------------");
        messages.put("bansystem.ban.template.list.format.head", "%-10s%-20s%-45s");
        messages.put("bansystem.mute.template.list.format.head", "%-10s%-20s%-45s");
        messages.put("bansystem.ban.template.list.format.content", "%-10s%-18s%-45s");
        messages.put("bansystem.mute.template.list.format.content", "%-10s%-18s%-45s");
        messages.put("bansystem.ban.template.list.id.head", "&eID");
        messages.put("bansystem.mute.template.list.id.head", "&eID");
        messages.put("bansystem.ban.template.list.id.content", "&6%ID%");
        messages.put("bansystem.mute.template.list.id.content", "&6%ID%");
        messages.put("bansystem.ban.template.list.time.head", "&eTime");
        messages.put("bansystem.mute.template.list.time.head", "&eTime");
        messages.put("bansystem.ban.template.list.time.content", "&c%TIME%");
        messages.put("bansystem.mute.template.list.time.content", "&c%TIME%");
        messages.put("bansystem.ban.template.list.reason.head", "&eReason");
        messages.put("bansystem.mute.template.list.reason.head", "&eReason");
        messages.put("bansystem.ban.template.list.reason.content", "&a%REASON%");
        messages.put("bansystem.mute.template.list.reason.content", "&a%REASON%");
        messages.put("bansystem.ban.template.list.footer", "&7------------------- &eBan Templates &7-------------------");
        messages.put("bansystem.mute.template.list.footer", "&7------------------- &eMute Templates &7-------------------");
        messages.put("bansystem.ban.template.add.successful", "&aTemplate added!");
        messages.put("bansystem.mute.template.add.successful", "&aTemplate added!");
        messages.put("bansystem.ban.template.add.error", "&cTemplate not found!");
        messages.put("bansystem.ban.template.edit.successful", "&aTemplate edited!");
        messages.put("bansystem.mute.template.edit.successful", "&aTemplate edited!");
        messages.put("bansystem.ban.template.remove.successful", "&aTemplate removed!");
        messages.put("bansystem.mute.template.remove.successful", "&aTemplate removed!");

        messages.put("bansystem.banmessage",
                "&cYou have been banned from the XY Network.\n" +
                        "&eReason: &c%REASON%\n" +
                        "&eRemaining time: &c%REMAINING_TIME%");
        messages.put("bansystem.mutemessage",
                "&cYou have been muted from the XY Network. \n" +
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
