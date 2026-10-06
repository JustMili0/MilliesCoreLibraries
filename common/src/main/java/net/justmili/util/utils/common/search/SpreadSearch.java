package net.justmili.util.utils.common.search;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Spreads out from the origins one block at a time, through the 6 faces (no diagonals).
 * A BiPredicate decides if it can go from one block to the next, so the answer can depend on both blocks
 * (e.g. leaves only spreading further away from the logs).
 * <p>
 * No radius, only the predicate and maxSize stop it. Origins are not included in the result.
 * Only supports {@link #biSearch}.
 */
public class SpreadSearch implements SearchAlgorithm {

    @Override
    public List<BlockPos> search(Level level, BlockPos origin, Predicate<BlockState> stateMatch, int maxRadius, int maxSize) {
        return List.of();
    }

    @Override
    public List<BlockPos> biSearch(Level level, Collection<BlockPos> origins, BiPredicate<BlockState, BlockState> canSpread, int maxSize) {
        var found = new ArrayList<BlockPos>();
        var visited = new LongOpenHashSet();
        var queue = new ArrayDeque<BlockPos>();

        // Mark every origin as visited so the spread never walks back into them
        for (var pos : origins) {
            visited.add(pos.asLong());
            queue.add(pos);
        }

        while (!queue.isEmpty() && found.size() < maxSize) {
            var current = queue.poll();
            var currentState = level.getBlockState(current);

            for (var direction : Direction.values()) {
                if (found.size() >= maxSize) break;

                var next = current.relative(direction);
                if (!visited.add(next.asLong())) continue;
                if (!canSpread.test(currentState, level.getBlockState(next))) continue;

                found.add(next);
                queue.add(next);
            }
        }
        return found;
    }

    /**
     * Same as above but with a single origin. Not on {@link SearchAlgorithm}, so it can't be used through {@link SearchAlgorithms}.
     */
    public List<BlockPos> biSearch(Level level, BlockPos origin, BiPredicate<BlockState, BlockState> canSpread, int maxSize) {
        return biSearch(level, List.of(origin), canSpread, maxSize);
    }
}