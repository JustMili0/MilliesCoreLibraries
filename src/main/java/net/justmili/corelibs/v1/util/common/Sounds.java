package net.justmili.corelibs.v1.util.common;

import net.minecraft.world.level.block.SoundType;

public class Sounds {

    public static float randomPitch(SoundType sound) {
        return sound.getPitch() * 0.8f;
    }

    public static float randomVolume(SoundType sound) {
        return (sound.getVolume() + 1f) / 2f;
    }
}