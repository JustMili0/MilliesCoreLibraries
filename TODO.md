### Notes
- Keep TODO updated
- Update mod version for every major change (stonecutter.properties.toml and CoreLibs.java)
- A lot of classes such as utils etc. can be copied from JustMili's other projects

# FIRST-PRIORITY TODO (EARLY BETA)
- [EVERYONE] Finish all the events
- [MILLIE] Rewrite config from `v1.config` to `v2.config`
  - Support for more entry types (TODO: Color, Lists [List, Set])
  - Fix, rework and improve priority config synchronization
  - [APPELS] Add full TOML support (including for old file conversion)
- [MILLIE] Finish reworking data generation utils
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

# TODO (RELEASE BETA)
- [REQUIRES CONFIG AND SCREEN LIB FINISHED] Add a proper config screen for Core Libs for our config
- Complete utils by adding as much functionality as possible by turning repetitive code from across multiple projects into utils

# TODO (FULL RELEASE)
- Code own version of Fabric Data Attachments API
  - With support for converting from FDAAPI and from whatever the fuck True End uses
- Finish `docs.lumynitystudios.net` for more documentation