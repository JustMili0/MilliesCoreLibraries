package net.justmili.mcl.v2.lib.util.common;

import net.justmili.mcl.CoreLibs;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;

public class FileIO {
    // read-only
    public static final StandardOpenOption[] READ_ONLY = { StandardOpenOption.READ };
    // read, create the file if it doesn't exist
    public static final StandardOpenOption[] READ_AND_CREATE = { StandardOpenOption.CREATE, StandardOpenOption.READ };
    // write, also change the file length so if the file is shorter the file doesn't become invalid. yes that's a thing.
    public static final StandardOpenOption[] WRITE_ONLY = { StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING };
    // create the file if it doesn't exist, then above
    public static final StandardOpenOption[] WRITE_AND_CREATE = { StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING };

    public interface Writing {
        void accept(BufferedWriter writer) throws IOException;
    }

    public static boolean write(Path path, Writing writing) {
        try {
            var parent = path.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);

            try (var writer = Files.newBufferedWriter(path)) {
                writing.accept(writer);
            }
            return true;
        } catch (IOException e) {
            CoreLibs.LOGGER.error("Failed to write '{}': {}", path, e.getMessage());
            return false;
        }
    }
}