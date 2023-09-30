# BanSystem
This plugin allows you to ban players. I am currently actively developing the plugin and have many ideas and features in progress.

## Why Choose BanSystem?

BanSystem offers several compelling reasons to use it on your BungeeCord server:

1. **Efficient Player Management:** Effortlessly ban, unban, mute, and unmute players. Access comprehensive ban and mute histories to enforce server rules effectively.
2. **Simplified Ban and Mute Templates:** Use predefined templates for common scenarios or customize them to fit your server's needs, ensuring consistent rule enforcement.
3. **Flexible Control:** Assign permissions, fine-tune ban and mute durations, and manage player groups with ease, giving you full control over server discipline.
4. **Easy Configuration:** Quick installation, message customization, and adjustable ban durations make setup a breeze.

## Commands

| Command                              | Description                         |
|--------------------------------------|-------------------------------------|
| `/ban <Player> [<Time>] <Reason>`    | Ban a player                        |
| `/ban <Player> #<template-id>`       | Ban a player using a template       |
| `/mute <Player> [<Time>] <Reason>`   | Mute a player                       |
| `/mute <Player> #<template-id>`      | Mute a player using a template      |
| `/unban <Player>`                    | Unban a player                      |
| `/unmute <Player>`                   | Unmute a player                     |
| `/baninfo <Player>`                  | Show the player's ban history       |
| `/muteinfo <Player>`                 | Show the player's mute history      |
| `/clearban(s) <Player>`              | Clear all ban entries for a player  |
| `/clearmute(s) <Player>`             | Clear all mute entries for a player |
| `/bantemplate list`                  | List ban templates                  |
| `/bantemplate add <time> <reason>`   | Add a ban template                  |
| `/bantemplate edit <time> <reason>`  | Edit a ban template                 |
| `/bantemplate remove <id>`           | Remove a ban template               |
| `/mutetemplate list`                 | List ban templates                  |
| `/mutetemplate add <time> <reason>`  | Add a mute template                 |
| `/mutetemplate edit <time> <reason>` | Edit a mute template                |
| `/mutetemplate remove <id>`          | Remove a mute template              |
| `/banreload`                         | Reload messages from configs        |

## Permissions

| Permission                 | Description                                               |
|----------------------------|-----------------------------------------------------------|
| `bansystem.ban`            | Permission for the `/ban` command                         |
| `bansystem.ban.permanent`  | Permission to use `/ban` with permanent bans              |
| `bansystem.ban.<group>`    | Maximum ban duration can be configured in `settings.yml`  |
| `bansystem.mute`           | Permission for the `/mute` command                        |
| `bansystem.mute.permanent` | Permission to use `/mute` with permanent mutes            |
| `bansystem.mute.<group>`   | Maximum mute duration can be configured in `settings.yml` |
| `bansystem.unban`          | Permission for the `/unban` command                       |
| `bansystem.unmute`         | Permission for the `/unmute` command                      |
| `bansystem.baninfo`        | Permission for the `/baninfo` command                     |
| `bansystem.muteinfo`       | Permission for the `/muteinfo` command                    |
| `bansystem.bantemplate`    | Permission for the `/bantemplate` command                 |
| `bansystem.mutetemplate`   | Permission for the `/mutetemplate` command                |
| `bansystem.reload`         | Permission for the `/banreload` command                   |
| `bansystem.ban.bypass`     | Permission to bypass to be banned                         |
| `bansystem.mute.bypass`    | Permission to bypass to be muted                          |
## Functions
- Ban players
- Unban players
- Mute players
- Unmute players
- Show ban history
- Show mute history
- Clear bans for specific players
- Clear mutes for specific players
- Create and use ban templates with IDs
- Create and use mute templates with IDs

## Requirements
- Java 17 or higher
- MySQL Database

## Installation
1. Move the plugin to the plugin's folder of your BungeeCord server.
2. Restart BungeeCord.
3. Configure MySQL settings in the `mysql.yml` configuration file.
4. Optionally, customize individual messages in the `messages.yml` configuration file.
5. If needed, adjust the ban duration settings for different groups in the `settings.yml`.

## How to Update
- [v0.1.0](https://github.com/BachErik-Team/BanSystem/releases/tag/v0.1.0) to [v0.2.0](https://github.com/BachErik-Team/BanSystem/tree/v0.2.0-BETA) can you find [here](https://docs.bacherik.eu/bansystem/how-to-update/v0.1.0-to-v0.2.0).

## Coming Soon
- If you are muted, commands that you don't able to use
- Add H2 and MongoDB Database support
- General history command to show all mutes, bans, and warns
- A warn System
- Spigot support

## In Planning
- Webpanel
- AI for the mute system
