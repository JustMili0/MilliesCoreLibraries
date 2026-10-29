package net.justmili.mcl.v1.lib.util.common;

import net.justmili.mcl.CoreLibs;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileIO {

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