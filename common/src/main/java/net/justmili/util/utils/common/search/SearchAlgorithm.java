package net.justmili.util.utils.common.search;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Common interface for block searches.
 * {@link #search} finds blocks around one origin, {@link #biSearch} spreads out from one or more origins.
 * Algorithms only implement what makes sense for them, the other one just returns an empty list.
 */
public interface SearchAlgorithm {

    /**
     * Iterates a cube around center, center included. The returned pos is reused between iterations; call .immutable() before storing it.
     */
    default Iterable<BlockPos> withinClosed(BlockPos center, int radius) {
        return BlockPos.betweenClosed(center.offset(-radius, -radius, -radius), center.offset(radius, radius, radius));
    }

    /**
     * Finds blocks around origin that pass stateMatch.
     * Whether origin itself can be in the result depends on the algorithm.
     *
     * @param maxRadius how far from origin to search, as a cube
     * @param maxSize max amount of blocks to return
     */
    List<BlockPos> search(Level level, BlockPos origin, Predicate<BlockState> stateMatch, int maxRadius, int maxSize);

    /**
     * Spreads out from origins, canSpread decides if the search can go from one block (from) onto its neighbour (to).
     * There's no radius, only canSpread and maxSize stop it. Origins are not included in the result.
     *
     * @param maxSize max amount of blocks to return
     */
    List<BlockPos> biSearch(Level level, Collection<BlockPos> origins, BiPredicate<BlockState, BlockState> canSpread, int maxSize);
}