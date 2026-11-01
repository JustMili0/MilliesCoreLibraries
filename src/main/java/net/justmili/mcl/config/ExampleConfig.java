// TODO: after finishing v2 config lib make example config account for EVERYTHING
package net.justmili.mcl.config;

import net.justmili.mcl.v2.lib.config.*;

public class ExampleConfig {
    public static MConfigBuilder clientJson = createBuilder(ConfigTypes.CLIENT, FileTypes.JSON);
    public static MConfigBuilder serverJson = createBuilder(ConfigTypes.SERVER, FileTypes.JSON);
    public static MConfigBuilder commonJson = createBuilder(ConfigTypes.COMMON, FileTypes.JSON);
    public static MConfigBuilder commonClientJson = createBuilder(ConfigTypes.COMMON_CLIENT_PRIORITY, FileTypes.JSON);
    public static MConfigBuilder commonServerJson = createBuilder(ConfigTypes.COMMON_SERVER_PRIORITY, FileTypes.JSON);

    public static MConfigBuilder clientJson5 = createBuilder(ConfigTypes.CLIENT, FileTypes.JSON5);
    public static MConfigBuilder serverJson5 = createBuilder(ConfigTypes.SERVER, FileTypes.JSON5);
    public static MConfigBuilder commonJson5 = createBuilder(ConfigTypes.COMMON, FileTypes.JSON5);
    public static MConfigBuilder commonClientJson5 = createBuilder(ConfigTypes.COMMON_CLIENT_PRIORITY, FileTypes.JSON5);
    public static MConfigBuilder commonServerJson5 = createBuilder(ConfigTypes.COMMON_SERVER_PRIORITY, FileTypes.JSON5);

    public static MConfigBuilder clientProps = createBuilder(ConfigTypes.CLIENT, FileTypes.PROPERTIES);
    public static MConfigBuilder serverProps = createBuilder(ConfigTypes.SERVER, FileTypes.PROPERTIES);
    public static MConfigBuilder commonProps = createBuilder(ConfigTypes.COMMON, FileTypes.PROPERTIES);
    public static MConfigBuilder commonClientProps = createBuilder(ConfigTypes.COMMON_CLIENT_PRIORITY, FileTypes.PROPERTIES);
    public static MConfigBuilder commonServerProps = createBuilder(ConfigTypes.COMMON_SERVER_PRIORITY, FileTypes.PROPERTIES);

    // TODO: Options here

    // Function made only for purpose of this example
    private static MConfigBuilder createBuilder(ConfigTypes env, FileTypes ext) {
        return new MConfigBuilder("examplemod", null, env, ext, true);
    }

    public static void initClient() {

    }

    public static void initServer() {

    }

    public static void initCommon() {

    }

    public static void initCommonClient() {

    }

    public static void initCommonServer() {

    }
}