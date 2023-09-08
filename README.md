# BanSystem
This plugin allows you to ban players. I am currently actively developing the plugin and have many ideas and features in progress.

## Commands
Command | Description
------- | -----------
`/ban <Player> [<Time>] <Reason>` | Ban a player
`/ban <Player> #<template-id>` | Ban a player using a template
`/unban <Player>` | Unban a player
`/baninfo <Player>` | Show the player's ban history
`/clearban(s) <Player>` | Clear all ban entries for a player
`/bantemplate list` | List ban templates
`/bantemplate add <time> <reason>` | Add a ban template
`/bantemplate edit <time> <reason>` | Edit a ban template
`/bantemplate remove <id>` | Remove a ban template
`/banreload` | Reload messages from configs

## Permissions
Permission | Description
---------- | -----------
`bansystem.ban` | Permission for the `/ban` command
`bansystem.ban.permanent` | Permission to use `/ban` with permanent bans
`bansystem.ban.<group>` | Maximum ban duration can be configured in `settings.yml`
`bansystem.unban` | Permission for the `/unban` command
`bansystem.baninfo` | Permission for the `/baninfo` command
`bansystem.bantemplate` | Permission for the `/bantemplate` command
`bansystem.reload` | Permission for the `/banreload` command

## Functions
- Ban players
- Unban players
- Show ban history
- Clear bans for specific players
- Create and use ban templates with IDs

## Requirements
- Java 17 or higher
- MySQL Database

## Coming Soon
- Mute system

## In Planning
- AI for the mute system
