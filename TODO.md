### Notes
- Keep TODO updated
- Update mod version for every major change (stonecutter.properties.toml and CoreLibs.java)
- A lot of classes such as utils etc. can be copied from JustMili's other projects

# FIRST-PRIORITY TODO (EARLY BETA)
- [EVERYONE] Finish all the events
- [MILLIE] Rewrite config from `v1.config` to `v2.config`
  - Re-read config values after `/reload`
  - Support for more entry types (TODO: Color, Lists [List, Set])
  - Support for converting old config file(s) and their entries into new ones
    - Add `.tryConvertFromOldFile(Path oldPath, Boolean deleteOldFile)` (path starts from config directory) to main builder
    - Add `.tryGetValueFromOldKey(String oldKey)` (from convert file tries to grab value from key and set it) to option builders
    - Delete old config file if deleteOldFile bool is true
    - Config Lib should be able to grab values from files it supports even if it wasn't from the same lib. Skip unsupported (yaml/yml, conf, txt etc.)
  - Fix, rework and improve priority config synchronization
- [MILLIE] Finish reworking data generation utils
- [EVERYONE] Port everything to all versions
  - Additionally exclude some files from being included in some jars at all

# TODO (EARLY BETA)
- Rework most util classes, attempt optimizations, add a bit more functionality to all
- Add Javadocs, proper comments, write documentation
  - For documentation create `docs.lumynitystudios.net`
  - [MILLIE] Maybe hire someone to write it all🧍‍♀️
- ADD AN ICON

# TODO (RELEASE ALPHA)
- [APPELS] Finish Config
  - Add full TOML support
- Add a Screen lib for easy screen creation 
  - Easy enabling/disabling and adding screen elements
  - Similar builder layout to Config Lib

# TODO (RELEASE BETA)
- [REQUIRES CONFIG AND SCREEN LIB FINISHED] Add a proper config screen for Core Libs for our config
- Complete utils by adding as much functionality as possible by turning repetitive code from across multiple projects into utils

# TODO (FULL RELEASE)
- Code own version of Fabric Data Attachments API
  - With support for converting from FDAAPI and from whatever the fuck True End uses