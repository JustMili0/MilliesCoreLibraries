package net.justmili.mcl.v1.lib.util;

import net.justmili.mcl.v1.lib.util.common.*;
import org.slf4j.Logger;

public class ModUtil {

    public enum VersionType {
        EXPERIMENTAL("Experimental build! Possibly unstable."),
        EARLY_DEV_ALPHA("Dev-only build! Possibly unstable."),
        EARLY_DEV_BETA("Dev-only build! Possibly unstable."),
        ALPHA("Dev/Playtester build!"),
        BETA("Dev/Playtester build!"),
        PRERELEASE("Dev/Playtester build!"),
        SUPPORTER_RELEASE("Supporter-only build! Do not redistribute."),
        PUBLIC_RELEASE("");

        private final String warning;

        VersionType(String warning) {
            this.warning = warning;
        }

        public String getWarning() {
            return warning;
        }

        public boolean hasWarning() {
            return !warning.isEmpty();
        }
    }

    public static void specialInitMessage(Logger logger, String modName, String modId, String modVersion, VersionType versionType) {
        if (versionType.hasWarning()) {
            logger.info("Initializing {} ({}) version {} ({})", modName, modId, modVersion, versionType.getWarning());
        } else {
            logger.info("Initializing {} ({}) version {}", modName, modId, modVersion);
        }
    }
}