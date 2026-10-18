### Notes
- Keep TODO and README on the latest main branch you're working on
- A lot of classes such as utils etc. can be copied from JustMili's other projects

# FIRST-PRIORITY TODO (0.0.2a)
- [APPELS] Finish all the events
- [MILLIE] Finish unfinished utils so you can actually run the dev env
- [MILLIE] Finish reworking data generation utils
- [BLUSPRING] Port `build-logic` for Stonecutter because it's still based on Arch Loom

# TODO (0.0.2a)
- Expand on config
  - Rework priority config syncing and make COMMON_SERVER_PRIORITY override client's in-memory values instead of on-file values
  - Finish config sync across loaders
  - Rewrite using CODECs if possible, wherever possible
  - Add enum list/selector support, add ARGB color picker support
- Include `Fabric Language Kotlin` in build (will be mostly used for utils)
- Rework most util classes, attempt optimizations, add a bit more functionality to all
- Add javadocs, comments and write full documentation

# TODO (0.0.1b)
- Finish Config
  - Add full TOML support
- [REQUIRES CONFIG] Redo Config Screen to be more like HTML+CSS - highly customizable
  - Should allow adding images
  - It will be absolute pain to make, but it'll be so worth it
- Complete utils by adding as much functionality as possible by turning repetitive code from across multiple projects into utils