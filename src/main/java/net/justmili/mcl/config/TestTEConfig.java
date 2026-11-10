package net.justmili.mcl.config;

import net.justmili.mcl.v2.lib.config.*;
import net.justmili.mcl.v2.lib.config.options.ConfigOption;

// Temporary test config for testing converting old config files
// Conversion example mod: https://modrinth.com/mod/true_end
// Note: this could be used later in actual True End
@Deprecated(forRemoval = true)
public class TestTEConfig {
    public static final MConfigBuilder client = new MConfigBuilder("true_end", null, ConfigTypes.CLIENT, FileTypes.JSON5, true);
    public static final MConfigBuilder common = new MConfigBuilder("true_end", null, ConfigTypes.COMMON, FileTypes.JSON5, true);
    public static ConfigOption<Boolean> enableFog;
    public static ConfigOption<Boolean> enableFlashingLights;
    public static ConfigOption<Boolean> enableCredits;
    public static ConfigOption<Boolean> enableClearInventoryAtEntry;
    public static ConfigOption<Boolean> enableRandomEvents;
    public static ConfigOption<Double> randomEventChance;
    public static ConfigOption<Double> entitySpawnChance;
    public static ConfigOption<Double> entityJumpscareChance;
    public static ConfigOption<Boolean> enableEntityReplies;
    public static ConfigOption<Boolean> enableDaytimeShifts;

    public static void initClient() {
        client.tryConvertOldConfig("TrueEnd_COMMON.json").create(root -> {

            root.addCategory("World Visuals", world -> {
                enableFog = world.addBoolOption("enableFog")
                    .tryGetOldConfigKeyValue("showFog")
                    .setDefault(true)
                    .addComment("Enables fog in Beyond The Dream dimension")
                    .buildOption();

                enableFlashingLights = world.addBoolOption("enableFlashingLights")
                    .tryGetOldConfigKeyValue("doFlashingLights")
                    .setDefault(true)
                    .addComment("Enables screen flashes for interactions with Seeping Reality biome")
                    .buildOption();
            }).addComment("World's visual effects options");

            enableCredits = root.addBoolOption("enableCredits")
                .tryGetOldConfigKeyValue("showCredits")
                .setDefault(true)
                .addComment("Enables whether mod credits will play after entering the exit portal from Beyond The Dream dimension")
                .buildOption();
        }).build();
    }

    public static void initCommon() {
        common.tryConvertOldConfig("TrueEnd_COMMON.json").create(root -> {

            enableClearInventoryAtEntry = root.addBoolOption("enableClearInventoryAtEntry")
                .tryGetOldConfigKeyValue("clearDreamItems")
                .setDefault(true)
                .addComment("Enables whether player's inventory will be cleared at entry to Beyond The Dream.")
                .addComment("Inventory will be partially restored at exit and anything obtained in Beyond The Dream will be erased from the inventory.")
                .buildOption();

            root.addCategory("Events", events -> {

                enableRandomEvents = events.addBoolOption("enableRandomEvents")
                    .tryGetOldConfigKeyValue("doRandomEvents")
                    .setDefault(true)
                    .buildOption();

                events.addCategory("Random Events", random -> {

                    randomEventChance = random.addDoubleOption("randomEventChance")
                        .tryGetOldConfigKeyValue("randomEventChance")
                        .setDefault(0.05)
                        .setRange(0.0, 1.0)
                        .buildOption();

                    entitySpawnChance = random.addDoubleOption("entitySpawnChance")
                        .tryGetOldConfigKeyValue("entitySpawnChance")
                        .setDefault(0.1)
                        .setRange(0.0, 1.0)
                        .buildOption();

                    entityJumpscareChance = random.addDoubleOption("entityJumpscareChance")
                        .setDefault(0.01)
                        .setRange(0.0, 1.0)
                        .buildOption();

                    enableEntityReplies = random.addBoolOption("enableEntityReplies")
                        .tryGetOldConfigKeyValue("doChatReplies")
                        .setDefault(true)
                        .buildOption();

                    enableDaytimeShifts = random.addBoolOption("enableDaytimeShifts")
                        .tryGetOldConfigKeyValue("doDaytimeChange")
                        .setDefault(true)
                        .buildOption();
                });
            });
        }).build();
    }
}