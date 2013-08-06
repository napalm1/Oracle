# Oracle

*The Oracle* is a bukkit-based plugin with a wide range of administrative information-management - including detailed playtime tracking, IP tracking, BungeeCord support, cross-server ban/unban management, player warning system, and more!

Oracle is based on dhmcStats, a custom/private plugin that formerly ran DHMC.us. Most of that functionality has been rewritten and polished for release as Oracle.



## Features Overview

- Detailed playtime tracking: Tracks join/quit timestamps, IPs, with checks for uncaught disconnects, and more.
- Provides a detailed list of playtime - totals, etc.
- Seen command for viewing first join and recent playtimes for players
- View recent play history for a player with `/playhist`
- Easily check if a player `/ison` 
- Supports servers behind BungeeCord proxies
- Cross-server ban/unban management. View history of all player bans - they're never lost.
- Player warning system - staff is alerted if they receive three warnings.
- Log commands and world/coordinates of their use to console
- Schedule server-wide announcements
- Macro system for server staff


## Permissions

oracle.ban
oracle.ison
oracle.seen
oracle.unban
oracle.reload
oracle.ignore-alt-check
oracle.guide-book-on-join
oracle.warn


## Events & API
 
Oracle contains a ton of useful information, so we'll work on evolving these events and the API over time.
 
- `OracleFirstTimePlayerEvent` - Fired the first time a player joins with a `Player getPlayer()` method.

There's a basic internal API you can use by including the plugin in your project.