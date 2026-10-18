package net.justmili.corelibs.libs.api.events;

/*

Selected events inspired by Fabric, Forge/NeoForge and Architectury API.
No code will be copied directly from those sources, but made and maintained from scratch.

--------------------------------------------------------------------------------- IMPLEMENTED

ServerLifeCycleEvents
- STARTING
- STOPPING
- STOPPED
- SAVE_PRE
- SAVE_POST
- DATAPACK_RELOAD_PRE
- DATAPACK_RELOAD_POST
- DATAPACK_SYNC_CONTENTS

ServerTickEvents
- SERVER_PRE
- SERVER_POST
- LEVEL_PRE
- LEVEL_POST
- PLAYER_PRE
- PLAYER_POST
- ENTITY_PRE
- ENTITY_POST
- LIVING_ENTITY_PRE
- LIVING_ENTITY_POST

UseEvents
- ITEM_PRE (based on ServerTweak's ItemMixin)
- ITEM_POST (normal Fabric/Neo item use event)
- BLOCK_POST
- ENTITY_POST

ClientLifecycleEvents
- STARTED
- STOPPED

ClientTickEvents
- CLIENT_PRE
- CLIENT_POST
- LEVEL_PRE
- LEVEL_POST

--------------------------------------------------------------------------------- TO IMPLEMENT

ServerConnectionEvents # WAITING FOR NETWORKING
- JOIN_SERVER_OR_WORLD
- LEAVE_SERVER_OR_WORLD

ServerLevelEvents # TO HOOK UP
- LEVEL_LOAD
- LEVEL_UNLOAD
- CHUNK_LOAD
- CHUNK_UNLOAD

ClientConnectionEvents # WAITING FOR NETWORKING
- JOIN_SERVER_OR_WORLD
- LEAVE_SERVER_OR_WORLD

PlayerEvents # TO FIX/FINISH AND HOOK UP
- RESPAWN
- CLONE
- CRAFT_ITEM
- SMELT_ITEM
- DROP_ITEM
- PICKUP_ITEM_PRE
- PICKUP_ITEM_POST
- CHANGE_DIMENSION
- OPEN_MENU
- CLOSE_MENU
- FILL_BUCKET
- ATTACK_ENTITY
- KILL_OTHER_ENTITY_POST

PlayerAdvancementEvents (custom, do mixins)
- ALLOW_COMPLETION
- ALLOW_PROGRESS
- GRANT
- PROGRESS
- REVOKE

ChatEvents
- ALLOW_CHAT
- ALLOW_COMMAND
- ALLOW_SYSTEM
- MESSAGE_SEND
- MESSAGE_RECIEVE
- COMMAND_EXECUTE
- SYSTEM_BROADCAST

EntityEvents
- LOAD
- UNLOAD
- ENTER_CHUNK
- EQUIPMENT_CHANGE

BlockEntityEvents
- LOAD
- UNLOAD

LivingEntityEvents
- CHECK_SPAWN
- ALLOW_DAMAGE
- DAMAGE_POST
- ALLOW_DEATH
- DEATH_POST
- MOB_CONVERSION
- ANIMAL_TAME

EntitySleepEvents
- ALLOW_BED
- ALLOW_SET_SPAWN
- ALLOW_NEARBY_MONSTERS
- ALLOW_RESETTING_TIME
- SLEEP
- WAKE
- SET_BED_OCCUPATION_STATE
- MODIFT_WAKE_UP_POSITION

*/