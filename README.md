# Oracle

*The Oracle* is a bukkit-based plugin with a wide range of administrative information-management - including detailed playtime tracking, IP tracking, BungeeCord support, cross-server ban/unban management, player warning system, and more!

Oracle is based on dhmcStats, a custom/private plugin that formerly ran DHMC.us. Most of that functionality has been rewritten and polished for release as Oracle.


## Features Overview

- You can disable each primary feature group if you don't want it.
- Detailed playtime tracking: Tracks join/quit timestamps, IPs, with checks for uncaught disconnects, and more.
- Supports servers behind BungeeCord proxies
- Use `/played` to view the total playtime for a player
- `/seen` command for viewing first join and most recent playtime
- Easily check if a player `/ison` 
- Ban/unban support - stored in a database so it works cross-server.
- Player warning system. `/warn` players for rule violation. They're shown a highlighted message of the warning, and staff are alerted if a player gets more than three.
- Log commands and world/coordinates of their use to console.
- Scheduled broadcast announcements (in-dev)

(Coming soon: Better reports, stats, player history, in-game way to control announcements)



## Installation

Throw into `plugins` directory. Start server once to generate config.

This plugin *requires* mysql, so you'll need to configure the connection settings in the config.


## Permissions / Commands

*Ban-related*

- `oracle.ban` - `/ban [player] [reason]` - Ban a player.
- `oracle.unban` - `/unban [player]` - Unban a player.
- `oracle.lookup` - `/lookup [player]` - Check if a player has been banned.

*Joins-related*

- `oracle.alts` - `/alts [player]` - View all alternate possible accounts for this player
- `oracle.seen` - `/seen [player]` - View the date a player joined and was last here
- `oracle.played` - `/played [player]` - View the total playtime of a player.

*Warnings-related*

- `oracle.warn` - `/warn [player] [message]` - File a warning for a player
- `oracle.warnings` - `/warnings [player]` - View warnings for a player

*Other*

- `oracle.ison` - `/ison [player]` - Check if a player is online. Supports partial usernames.
- `oracle.reload` - `/oracle reload` - Reloads the config

*Commands that only accept a single [player] argument may be left blank, and will default to the current user.*

## Events & API
 
Oracle contains a ton of useful information, so we'll work on evolving these events and the API over time.
 
- `OracleFirstTimePlayerEvent` - Fired the first time a player joins with a `Player getPlayer()` method.

There's a basic internal API you can use by including the plugin in your project.


## Get Help

IRC: irc.esper.net #prism

[Bug tracker](https://snowy-evening.com/botsko/oracle)       
           
## Credits

This plugin was custom designed by viveleroi for the amazing *s.dhmc.us* Minecraft server.


## Authors

- viveleroi (Creator, Lead Developer)
- nasonfish (Contributor to dhmcStats)