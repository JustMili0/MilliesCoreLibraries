package net.justmili.util.utils.common.search;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public enum SearchAlgorithms {
    BREADTH_FIRST_SEARCH(new BreadthFirstSearch()),
    DEPTH_FIRST_SEARCH(new DepthFirstSearch()),
    SPREAD_SEARCH(new SpreadSearch()),
    CUBE_SEARCH(new CubeSearch());

    private final SearchAlgorithm algorithm;

    SearchAlgorithms(SearchAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public List<BlockPos> search(Level level, BlockPos origin, Predicate<BlockState> stateMatch, int maxRadius, int maxSize) {
        return algorithm.search(level, origin, stateMatch, maxRadius, maxSize);
    }

    public List<BlockPos> biSearch(Level level, Collection<BlockPos> origins, BiPredicate<BlockState, BlockState> canSpread, int maxSize) {
        return algorithm.biSearch(level, origins, canSpread, maxSize);
    }
}