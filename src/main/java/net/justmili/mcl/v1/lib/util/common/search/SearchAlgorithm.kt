package net.justmili.mcl.v1.lib.util.common.search

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * Common interface for block searches. [search] finds blocks around one origin, [biSearch] spreads out from one or more origins.
 * Algorithms only implement what makes sense for them, the other one just returns an empty list.
 */
interface SearchAlgorithm {

    /**
     * Iterates a cube around center, center included. The returned pos is reused between iterations; call .immutable() before storing it.
     */
    fun withinClosed(center: BlockPos, radius: Int): Iterable<BlockPos> = BlockPos.betweenClosed(center.offset(-radius, -radius, -radius), center.offset(radius, radius, radius))

    /**
     * Finds blocks around origin that pass stateMatch. Whether origin itself can be in the result depends on the algorithm.
     * 
     * @param maxRadius how far from origin to search, as a cube
     * @param maxSize   max amount of blocks to return
     */
    fun search(level: Level, origin: BlockPos, stateMatch: Predicate<BlockState>, maxRadius: Int, maxSize: Int): MutableList<BlockPos> = mutableListOf()

    /**
     * Spreads out from origins, canSpread decides if the search can go from one block (from) onto its neighbor (to). There's no radius, only canSpread and maxSize stop it. Origins are not included in the result.
     * 
     * @param maxSize max amount of blocks to return
     */
    fun biSearch(level: Level, origins: MutableCollection<BlockPos>, canSpread: BiPredicate<BlockState, BlockState>, maxSize: Int): MutableList<BlockPos> = mutableListOf()
}