package net.justmili.util.utils.common.search;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Searches connected blocks using depth-first traversal.
 * Blocks are searched by following each connected path before moving to the next.
 * <p>
 * Diagonals count as connected and origin is not included in the result.
 * Results are not sorted by distance, so if the group is bigger than maxSize use {@link BreadthFirstSearch}
 * to get the closest blocks.
 */
public class DepthFirstSearch implements SearchAlgorithm {

    @Override
    public List<BlockPos> search(Level level, BlockPos origin, Predicate<BlockState> stateMatch, int maxRadius, int maxSize) {
        var found = new ArrayList<BlockPos>();
        var visited = new LongOpenHashSet();
        var stack = new ArrayDeque<BlockPos>();
        var bounds = new BoundingBox(origin).inflatedBy(maxRadius);

        visited.add(origin.asLong());
        stack.push(origin);

        while (!stack.isEmpty() && found.size() < maxSize) {
            var current = stack.pop();

            for (var neighbor : withinClosed(current, 1)) {
                if (found.size() >= maxSize) break;
                if (!visited.add(neighbor.asLong()) || !bounds.isInside(neighbor)) continue; // Don't search the same block twice or go outside the search radius
                if (!stateMatch.test(level.getBlockState(neighbor))) continue;

                var next = neighbor.immutable();
                found.add(next);
                stack.push(next);
            }
        }
        return found;
    }

    @Override
    public List<BlockPos> biSearch(Level level, Collection<BlockPos> origins, BiPredicate<BlockState, BlockState> canSpread, int maxSize) {
        return List.of();
    }
}