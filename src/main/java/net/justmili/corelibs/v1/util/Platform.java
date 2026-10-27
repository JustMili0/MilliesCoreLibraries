package net.justmili.corelibs.v1.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.SharedConstants;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class Platform {
    private static final FabricLoader FABRIC = FabricLoader.getInstance();
    public static final String PLATFORM = "fabric";
    public static final String GAME_VERSION = SharedConstants.getCurrentVersion().getId();
    public static final boolean IS_DEV_ENV = FABRIC.isDevelopmentEnvironment();

    public static EnvType getGameEnv() {
        return FABRIC.getEnvironmentType();
    }

    public static Path getGameDir() {
        return FABRIC.getGameDir();
    }

    public static Path getConfigDir() {
        return FABRIC.getConfigDir();
    }

    public static Path getModsDir() {
        return Path.of(getGameDir().toString(), "mods");
    }

    public static boolean isModLoaded(String modId) {
        return FABRIC.isModLoaded(modId);
    }

    public static Collection<ModContainer> getMods() {
        return FABRIC.getAllMods();
    }

    public static Optional<ModContainer> getMod(String modId) {
        return FABRIC.getModContainer(modId);
    }

    public static Collection<String> getModIds() {
        return getMods().stream().map(mod -> mod.getMetadata().getId()).toList();
    }

    public static int getModCount() {
        return getMods().size();
    }
}
