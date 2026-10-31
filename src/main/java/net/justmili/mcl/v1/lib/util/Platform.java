package net.justmili.mcl.v1.lib.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.SharedConstants;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

public class Platform {
    private static final FabricLoader LOADER = FabricLoader.getInstance();
    public static final String PLATFORM = "fabric";
    public static final String GAME_VERSION = SharedConstants.getCurrentVersion().getId();
    public static final boolean IS_DEV_ENV = LOADER.isDevelopmentEnvironment();
    public static final EnvType GAME_ENV = LOADER.getEnvironmentType();
    public static final Path GAME_DIR = LOADER.getGameDir();
    public static final Path CONFIG_DIR = LOADER.getConfigDir();
    public static final Path MODS_DIR = Path.of(GAME_DIR.toString(), "mods");
    public static final Collection<ModContainer> MODS = LOADER.getAllMods();
    public static final Collection<String> MOD_IDS = MODS.stream().map(mod -> mod.getMetadata().getId()).toList();
    public static final int MOD_COUNT = MODS.size();

    public static boolean isModLoaded(String modId) {
        return LOADER.isModLoaded(modId);
    }

    public static Optional<ModContainer> getMod(String modId) {
        return LOADER.getModContainer(modId);
    }
}
