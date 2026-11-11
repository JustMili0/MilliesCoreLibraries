### Notes
- ALWAYS USE MINECRAFT'S CLASSES WHEN AND AS MUCH POSSIBLE SO OPTIMIZATION MODS CAN TAKE THE WHEEL AFTER
- Keep TODO updated
- Dump mod version for every code change for every commit (In `stonecutter.properties.toml` and `CoreLibs.java`)
- A lot of classes such as utils etc. can be copied from JustMili's other projects

# FIRST-PRIORITY TODO (EARLY BETA)
- [EVERYONE] Finish all the events
- Rewrite config from `v1.config` to `v2.config`
  - [MILLIE] Add support for registry entry/tag options (e.g. `minecraft:oak_log` or `#minecraft:logs`, `minecraft:diamond_ore` or `#minecraft:diamond_ores`)
    - Saved as an id or a tag key (via `ExtraCodecs.TAG_OR_ELEMENT_ID`), only resolved when checked, so stays correct after datapack reload
    - No properties/NBT comparing like Mojang's `BlockPredicateArgument`/`ItemPredicateArgument` does, it's just a list of entries and tags
    - Not generalized, only for what vanilla already provides (blocks, items, entity types, enchantments (1.21+), biomes, etc.)
  - [MILLIE] Fix, rework and improve priority config synchronization
  - [APPELS] Add full TOML support (including for old file conversion)
- [MILLIE] Finish reworking data generation utils
- Use `Config.enableDebug` where `Platform.IS_DEV_ENV` is used etc. generally use for logging extra info that'd only be useful for a developer
- [EVERYONE] Port everything to all versions
  - Additionally exclude some files from being included in some jars at all

# TODO (EARLY BETA)
- Rework most util classes, attempt optimizations, add a bit more functionality to all
- Add Javadocs, proper comments, write documentation
- ADD AN ICON

# TODO (RELEASE ALPHA)
- Add a Screen lib for easy screen creation 
  - Easy enabling/disabling and adding screen elements
  - Similar builder layout to Config Lib
- [REQUIRES CONFIG AND SCREEN LIB FINISHED] Add a proper config screen for Core Libs for our config

# TODO (RELEASE BETA)
- Complete utils by adding as much functionality as possible by turning repetitive code from across multiple projects into utils
- Code own version of Fabric Data Attachments API
    - With support for converting from FDAAPI and from whatever the fuck True End uses

# TODO (FULL RELEASE)
- Finish `docs.lumynitystudios.net` for more documentation