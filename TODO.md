### Notes
- Keep TODO and README on the latest main branch you're working on
- A lot of classes such as utils etc. can be copied from JustMili's other projects

# TODO
- (LIBS UTIL) Finish reworking data generation utils
- [BLUSPRING'S JOB] (API NETWORK) Add cross-loader networking
- [REQUIRES NETWORKING] (API EVENTS) Replicate every major useful Fabric/Forge event, sprinkle in some custom ones too and keep them grouped similarly to Architectury
- (LIBS CONFIG) Finish Config
  - Make COMMON_SERVER_PRIORITY override client's in-memory values instead of on-file values
  - Finish config sync across loaders
  - Rewrite using CODECs if possible, wherever possible
  - Add enum list/selector support, add ARGB color picker support
  - Add full TOML support
- (LIBS UTIL) Properly rework and finish all the util classes and give each even more functionality
- (GRADLE) Switch to stonecutter template, port everything accordingly
- [REQUIRES CONFIG] (LIBS CONF. SCREEN) Redo Config Screen to be more like HTML+CSS - highly customizable
  - Should allow adding images
  - It will be absolute pain to make, but it'll be so worth it
- [REQUIRES EVENTS] (API REG) Add cross-loader registries

- Add javadocs, comments and write full documentation