package net.justmili.corelibs.v2.config;

import com.mojang.serialization.Codec;
import net.justmili.corelibs.v2.config.flags.ConfigTypes;
import net.justmili.corelibs.v2.config.flags.FileTypes;
import net.justmili.corelibs.v2.config.flags.OptionFlags;
import net.justmili.corelibs.v2.config.items.CategoryItem;
import net.justmili.corelibs.v2.config.items.CommentItem;
import net.justmili.corelibs.v2.config.items.ConfigOption;

import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MConfigBuilder {
    private final MConfigLoader loader;

    public MConfigBuilder(String modId, String customFileName, ConfigTypes configType, FileTypes fileType, boolean createSubDirectory) {
        this.loader = new MConfigLoader(modId, customFileName, configType, fileType, createSubDirectory);
    }

    public MConfigBuilder(String modId, ConfigTypes configType, FileTypes fileType, boolean createSubDirectory) {
        this.loader = new MConfigLoader(modId, null, configType, fileType, createSubDirectory);
    }

    public MConfigBuilder(String modId, String customFileName, ConfigTypes configType, FileTypes fileType) {
        this.loader = new MConfigLoader(modId, customFileName, configType, fileType, false);
    }

    public MConfigBuilder(String modId, ConfigTypes configType, FileTypes fileType) {
        this.loader = new MConfigLoader(modId, null, configType, fileType, false);
    }

    public ConfigBuilder create() {
        return new ConfigBuilder(loader, loader.root());
    }

    public static class ConfigBuilder {
        private final MConfigLoader loader;
        private final CategoryItem category;

        public ConfigBuilder(MConfigLoader loader, CategoryItem category) {
            this.loader = loader;
            this.category = category;
        }

        public ConfigBuilder addComment(String comment) {
            category.add(new CommentItem(comment));
            return this;
        }

        public CategoryBuilder addCategory(String name, Consumer<ConfigBuilder> content) {
            var child = new CategoryItem(name, null);
            category.add(child);
            content.accept(new ConfigBuilder(loader, child));
            return new CategoryBuilder(loader, category, child);
        }

        public <T> OptionBuilder addOption(String key, T value, Consumer<T> setter, OptionFlags... flags) {
            return register(key, value, codecFor(value), candidate -> true, value instanceof Boolean? "Allowed values: true, false" : null, setter, flags);
        }

        public OptionBuilder addOption(String key, String value, int charLimit, Consumer<String> setter, OptionFlags... flags) {
            return register(key, value, Codec.STRING, candidate -> candidate.length() <= charLimit, "Maximum length: " + charLimit, setter, flags);
        }

        public <T extends Comparable<T>> OptionBuilder addOption(String key, T value, T min, T max, Consumer<T> setter, OptionFlags... flags) {
            return register(key, value, codecFor(value), candidate -> candidate.compareTo(min) >= 0 && candidate.compareTo(max) <= 0, "Allowed range: " + min + "-" + max, setter, flags);
        }

        // TODO: Add Enum support

        // TODO: Add List<?>/Set support

        // TODO: Add (ARGB) color picker support

        public void build() {
            loader.buildOrLoad();
        }

        private <T> OptionBuilder register(String key, T value, Codec<T> codec, Predicate<T> validator, String restriction, Consumer<T> setter, OptionFlags[] flags) {
            var option = new ConfigOption<>(key, value, codec, validator, restriction, setter, flags);
            category.add(option);
            return new OptionBuilder(loader, category, option);
        }

        @SuppressWarnings("unchecked")
        private static <T> Codec<T> codecFor(T value) {
            Codec<?> codec;
            if (value instanceof String) codec = Codec.STRING;
            else if (value instanceof Integer) codec = Codec.INT;
            else if (value instanceof Long) codec = Codec.LONG;
            else if (value instanceof Float) codec = Codec.FLOAT;
            else if (value instanceof Double) codec = Codec.DOUBLE;
            else if (value instanceof Boolean) codec = Codec.BOOL;
            else throw new IllegalArgumentException("Unsupported config option type: " + value.getClass().getName());

            return (Codec<T>) codec;
        }
    }

    public static class CategoryBuilder extends ConfigBuilder {
        private final CategoryItem child;

        public CategoryBuilder(MConfigLoader loader, CategoryItem category, CategoryItem child) {
            super(loader, category);
            this.child = child;
        }

        public CategoryBuilder comment(String comment) {
            child.comment(comment);
            return this;
        }
    }

    public static class OptionBuilder extends ConfigBuilder {
        private final ConfigOption<?> option;

        public OptionBuilder(MConfigLoader loader, CategoryItem category, ConfigOption<?> option) {
            super(loader, category);
            this.option = option;
        }

        public OptionBuilder comment(String comment) {
            option.comment(comment);
            return this;
        }

        public void attachImage(Path path) {
            // TODO: Add optional attaching image paths for use in v2.configscreen
        }
    }
}