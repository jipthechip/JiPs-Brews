package com.jipthechip.fermentationmod.Items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemList {
    public static final Item FABRIC_ITEM = new FabricItem(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item BEER_MUG = new BeerMug(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item TUMBLER = new Tumbler(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item WINE_GLASS = new WineGlass(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item MASH_BUCKET = new MashBucket(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item YEAST_CULTURE = new YeastCulture(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item BEER_MUG_FILLED = new BeerMugFilled(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));

    public static void registerItems(){
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "fabric_item"), FABRIC_ITEM);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "beer_mug"), BEER_MUG);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "tumbler"), TUMBLER);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "wine_glass"), WINE_GLASS);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "mash_bucket"), MASH_BUCKET);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "yeast_culture"), YEAST_CULTURE);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "beer_mug_filled"), BEER_MUG_FILLED);
    }
}
