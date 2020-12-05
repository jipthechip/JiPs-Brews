package com.jipthechip.fermentationmod.Items;

import com.jipthechip.fermentationmod.Blocks.BlockList;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Material;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.Registry;


public class ItemColorList {

    private static final ItemColorProvider MASH_COLOR =
        ((stack, tintIndex) -> {
            CompoundTag tag = stack.getTag();
            assert tag != null;
            if(tintIndex == 1) return MashBucket.getColor(stack);
            return 0xFFFFFF;
         });

    private static final ItemColorProvider AIRLOCK_COLOR =
            ((stack, tintIndex) -> Material.WATER.getColor().color);

    public static void registerItemColors(){

        ColorProviderRegistry.ITEM.register(MASH_COLOR, ItemList.MASH_BUCKET);
        ColorProviderRegistry.ITEM.register(AIRLOCK_COLOR, BlockList.AIRLOCK.asItem());
    }
}
