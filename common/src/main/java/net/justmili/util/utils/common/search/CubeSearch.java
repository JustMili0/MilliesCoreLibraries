package net.justmili.util.utils.common.search;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Searches every block within a cube around the origin.
 * Matching blocks are returned closest to the origin first.
 * <p>
 * Blocks don't have to be connected, and origin IS included in the result if it matches.
 * Every block in the cube gets checked, so keep the radius small as block count grows cubically.
 */
public class CubeSearch implements SearchAlgorithm {

    @Override
    public List<BlockPos> search(Level level, BlockPos origin, Predicate<BlockState> stateMatch, int maxRadius, int maxSize) {
        var found = new ArrayList<BlockPos>();

        for (var pos : withinClosed(origin, maxRadius)) {
            if (stateMatch.test(level.getBlockState(pos))) found.add(pos.immutable());
        }

        // Sort by distance so the closest blocks are returned first
        found.sort(Comparator.comparingDouble(pos -> pos.distSqr(origin)));
        return found.size() > maxSize? new ArrayList<>(found.subList(0, maxSize)) : found;
    }

    @Override
    public List<BlockPos> biSearch(Level level, Collection<BlockPos> origins, BiPredicate<BlockState, BlockState> canSpread, int maxSize) {
        return List.of();
    }
}