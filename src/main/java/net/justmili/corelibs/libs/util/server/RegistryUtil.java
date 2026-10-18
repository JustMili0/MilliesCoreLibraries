package net.justmili.corelibs.libs.util.server;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import java.util.Optional;

public class RegistryUtil {

    public static <T> HolderLookup.RegistryLookup<T> lookup(MinecraftServer server, ResourceKey<Registry<T>> registryKey) {
        return server.registryAccess().lookupOrThrow(registryKey);
    }

    public static <T> HolderLookup.RegistryLookup<T> lookup(ServerLevel level, ResourceKey<Registry<T>> registryKey) {
        return lookup(level.getServer(), registryKey);
    }

    public static <T> Optional<Holder.Reference<T>> get(MinecraftServer server, ResourceKey<Registry<T>> registryResource, ResourceKey<T> key) {
        return lookup(server, registryResource).get(key);
    }

    public static <T> Optional<Holder.Reference<T>> get(ServerLevel level, ResourceKey<Registry<T>> registryResource, ResourceKey<T> key) {
        return get(level.getServer(), registryResource, key);
    }

    public static <T> Holder.Reference<T> getOrThrow(MinecraftServer server, ResourceKey<Registry<T>> registryResource, ResourceKey<T> key) {
        return lookup(server, registryResource).getOrThrow(key);
    }

    public static <T> Holder.Reference<T> getOrThrow(ServerLevel level, ResourceKey<Registry<T>> registryResource, ResourceKey<T> key) {
        return getOrThrow(level.getServer(), registryResource, key);
    }
}
