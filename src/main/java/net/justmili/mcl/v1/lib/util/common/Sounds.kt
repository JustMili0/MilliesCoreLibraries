package net.justmili.mcl.v1.lib.util.common

import net.minecraft.world.level.block.SoundType

object Sounds {

    @JvmStatic fun randomPitch(sound: SoundType) = sound.getPitch() * 0.8f
    @JvmStatic fun randomVolume(sound: SoundType) = (sound.getVolume() + 1f) / 2f
}