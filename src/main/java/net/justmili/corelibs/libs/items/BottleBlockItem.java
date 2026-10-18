package net.justmili.corelibs.libs.items;

import net.justmili.corelibs.libs.util.common.EntityUtil;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class BottleBlockItem extends BlockItem {

    public BottleBlockItem(Block block, Properties properties) {
        super(block, properties.craftRemainder(Items.GLASS_BOTTLE).stacksTo(16));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var result = super.useOn(context);
        if (!result.consumesAction()) return result;

        var player = context.getPlayer();
        if (player == null || player.getAbilities().instabuild) return result;

        EntityUtil.useHeldWithResult(player, context.getHand(), Items.GLASS_BOTTLE, false, 0);

        return result;
    }
}
