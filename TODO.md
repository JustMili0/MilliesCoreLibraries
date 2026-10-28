### Notes
- Keep TODO and README on the latest main branch you're working on
- A lot of classes such as utils etc. can be copied from JustMili's other projects

# FIRST-FIRST-PRIORITY TODO (0.0.2a)
- [MILLIE] Create default formatting to apply everywhere for Java and Kotlin (and maybe buildscripts too)

# FIRST-PRIORITY TODO (0.0.2a)
- [APPELS] Finish all the events
- [MILLIE] Rewrite config from `v1.config` to `v2.config` (deprecate `v1.config` after)
  - Completely new (hopefully better) config builder
  - No more split `ConfigEntry<?>`, `ListConfigEntry` etc., now one `ConfigOption<?>` for all
  - Rewrite using Codecs
  - Support for more entry types (Base [String, Numbers, boolean], Enum, Color, Lists [List, Set])
  - Support for "attaching" images to config values (usable via `v2.configscreen`)
  - Support for converting old config file(s) and their entries into new ones
  - Fix, rework and improve priority config synchronization
- [MILLIE] Finish reworking data generation utils
- [BLUSPRING] Port `build-logic` for Stonecutter because it's still based on Arch Loom
- [EVERYONE] Port everything to all versions
  - Additionally exclude some files from being included in some jars at all

# TODO (0.0.2a)
- Rework most util classes, attempt optimizations, add a bit more functionality to all
- Add Javadocs, proper comments, write documentation
  - For documentation create `docs.lumynitystudios.net`
  - [MILLIE] Maybe hire someone to write it all🧍‍♀️

# TODO (0.0.1b)
- Finish Config
  - Add full TOML support
- [REQUIRES CONFIG] Redo Config Screen to be more like HTML+CSS in `v2` - highly customizable
  - It will be absolute pain to make, but it'll be so worth it
- Complete utils by adding as much functionality as possible by turning repetitive code from across multiple projects into utils

# TODO (0.0.2b)
- Code own version of Fabric Data Attachments API
  - With support for converting from FDAAPI and from whatever the fuck True End uses