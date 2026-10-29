package net.justmili.mcl.v1.lib.util.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class LevelUtil {

    public static void setAir(Level level, BlockPos pos, int updateFlags) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), updateFlags);
    }

    public static void setAir(Level level, int x, int y, int z, int updateFlags) {
        level.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), updateFlags);
    }

    public static void setAir(Level level, Vec3 pos, int updateFlags) {
        level.setBlock(BlockPos.containing(pos.x, pos.y, pos.z), Blocks.AIR.defaultBlockState(), updateFlags);
    }

    public static void setAir(Level level, BlockPos pos) {
        setAir(level, pos, Block.UPDATE_ALL);
    }

    public static void setAir(Level level, int x, int y, int z) {
        setAir(level, x, y, z, Block.UPDATE_ALL);
    }

    public static void setAir(Level level, Vec3 pos) {
        setAir(level, pos, Block.UPDATE_ALL);
    }
}