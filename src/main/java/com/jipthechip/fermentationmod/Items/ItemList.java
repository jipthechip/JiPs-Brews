package com.jipthechip.fermentationmod.Items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemList {
    public static final Item FABRIC_ITEM = new FabricItem(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item BEER_MUG = new BeerMug(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item TUMBLER = new Tumbler(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item WINE_GLASS = new WineGlass(new FabricItemSettings().group(ItemGroup.MISC));

    public static void registerItems(){
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "fabric_item"), FABRIC_ITEM);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "beer_mug"), BEER_MUG);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "tumbler"), TUMBLER);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "wine_glass"), WINE_GLASS);
    }
}
