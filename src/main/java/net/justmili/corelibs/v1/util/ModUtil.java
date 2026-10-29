package net.justmili.corelibs.v1.util;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.justmili.corelibs.CoreLibs;
import net.justmili.corelibs.v1.util.common.*;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

@SuppressWarnings({"UnstableApiUsage"})
public class ModUtil {

    public static void markEOS(String modName, String modId, boolean noFurtherVersionSupport, boolean shouldNotifyChat) {
        if (!noFurtherVersionSupport) return;
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            CoreLibs.LOGGER.warn("""
                  \nYou are using the last version of "{}" ({}) that supports Minecraft {}!
                  This means that mod is no longer developed for this Minecraft version.
                  It is recommended you update to a newer Minecraft version for a more up-to-date experience.
                """, modName, modId, server.getServerVersion());
        });

        if (!shouldNotifyChat) return;
        var notified = FdaUtil.createPersistent(ResourceUtil.parse(modId, "notified_of_eos"), false, Codec.BOOL);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
            notifyOnce(handler.player, notified, """
                  You are using the last version of "%s" that supports Minecraft %s!
                  This means that mod is no longer developed for this Minecraft version.
                  It is recommended you update to a newer Minecraft version for a more up-to-date experience.
                
                  This message will not appear in chat again.
                """.formatted(modName, server.getServerVersion())));
    }

    public static void markEOD(String modName, String modId, boolean noFurtherDevelopment, boolean shouldNotifyChat) {
        if (!noFurtherDevelopment) return;
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            // TODO: Expand on message
            CoreLibs.LOGGER.warn("""
                  Mod "{}" is no longer in development!
                """, modName);
        });

        if (!shouldNotifyChat) return;
        var notified = FdaUtil.createPersistent(ResourceUtil.parse(modId, "notified_of_eod"), false, Codec.BOOL);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            // TODO: Expand on message
            notifyOnce(handler.player, notified, """
                  Mod "%s" is no longer in development!
                  This message will not appear in chat again.
                """.formatted(modName));
        });
    }

    private static void notifyOnce(ServerPlayer player, AttachmentType<Boolean> notified, String message) {
        if (FdaUtil.getBool(player, notified)) return;
        Text.sendOkTo(player, message.strip());
        FdaUtil.set(player, notified, true);
    }

    public enum VersionBuildType {
        CORELIBS_INDEV("Integrated or WIP build!"), // CORELIBS: Temporary build flag
        EXPERIMENTAL("Experimental build! Possibly unstable."),
        EARLY_DEV_ALPHA("Dev-only build! Possibly unstable."),
        EARLY_DEV_BETA("Dev-only build! Possibly unstable."),
        ALPHA("Dev/Playtester build!"),
        BETA("Dev/Playtester build!"),
        PRERELEASE("Dev/Playtester build!"),
        SUPPORTER_RELEASE("Supporter-only build! Do not redistribute."),
        PUBLIC_RELEASE("");

        private final String warning;

        VersionBuildType(String warning) {
            this.warning = warning;
        }

        public String getWarning() {
            return warning;
        }

        public boolean hasWarning() {
            return !warning.isEmpty();
        }
    }

    public static void specialInitMessage(Logger logger, String modName, String modId, String modVersion, VersionBuildType buildType) {
        if (buildType.hasWarning()) {
            logger.info("Initializing {} ({}) version {} ({})", modName, modId, modVersion, buildType.getWarning());
        } else {
            logger.info("Initializing {} ({}) version {}", modName, modId, modVersion);
        }
    }
}