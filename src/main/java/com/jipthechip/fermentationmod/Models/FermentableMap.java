package com.jipthechip.fermentationmod.Models;

import net.minecraft.block.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class FermentableMap {
    public static final Map<Item, Fermentable> Fermentables = new HashMap<Item, Fermentable>();

    public static void initializeFermentableMap(){

        Fermentables.put(Items.WHEAT, new Fermentable(0xffdcbb65, 0.125f, 0.6f, 0.5f,
                new FlavorProfile(1,8, 0,0,0,1, 0)));

        Fermentables.put(Items.BREAD, new Fermentable(0xffbc8927, 0.125f, 0.6f, 0.5f,
                new FlavorProfile(1,8, 0,0,0,1, 0)));

        Fermentables.put(Items.SUGAR, new Fermentable(0xffffffff,0.125f, 0.95f, 0.8f,
                new FlavorProfile(0,0, 0,0,0, 10, 0)));

        Fermentables.put(Items.SUGAR_CANE, new Fermentable(0xffb7ea7c,0.125f, 0.7f, 0.5f,
                new FlavorProfile(0,0, 0,3,0, 7, 0)));

        Fermentables.put(Items.MELON_SLICE, new Fermentable(0xffBF3123,0.125f, 0.0625f, 0.2f,
                new FlavorProfile(0,0, 9,0,0, 1, 0)));

        Fermentables.put(Items.GLISTERING_MELON_SLICE, new Fermentable(0xffc94908,0.125f, 0.0625f, 0.6f,
                new FlavorProfile(4,0, 5,0,0, 1, 0)));

        Fermentables.put( Items.APPLE, new Fermentable(0xffffe063,0.125f, 0.125f, 0.7f,
                new FlavorProfile(0,0, 5,0,0, 3, 2)));

        Fermentables.put(Items.GOLDEN_APPLE, new Fermentable(0xffeccb45,0.125f, 0.1f, 0.8f,
                new FlavorProfile(3,0, 3,0,0, 2, 2)));

        Fermentables.put(Items.ENCHANTED_GOLDEN_APPLE, new Fermentable(0xffeaee57,0.125f, 0.075f, 0.9f,
                new FlavorProfile(7,0, 1,0,0, 1, 1)));

        Fermentables.put(Items.POTATO, new Fermentable(0xfffff8ad, 0.125f, 0.25f, 0.4f,
                new FlavorProfile(4,3, 0,2,1, 0, 0)));

        Fermentables.put(Items.BAKED_POTATO, new Fermentable(0xfffff8ad, 0.125f, 0.25f, 0.6f,
                new FlavorProfile(2,5, 0,2,0, 1, 0)));

        Fermentables.put(Items.POISONOUS_POTATO, new Fermentable(0xddff99, 0.125f, 0.25f, 0.8f,
                new FlavorProfile(2,2, 0,2,4, 0, 0)));

        Fermentables.put(Items.BEETROOT, new Fermentable(0xffa4272b, 0.125f, 0.1f, 0.4f,
                new FlavorProfile(4,0, 0,1,5, 0, 0)));

        Fermentables.put(Items.CARROT, new Fermentable(0xffff8e09, 0.125f, 0.1f, 0.3f,
                new FlavorProfile(2,0, 0,4,2, 2, 0)));

        Fermentables.put(Items.GOLDEN_CARROT, new Fermentable(0xffffcc33,0.125f, 0.1f, 0.7f,
                new FlavorProfile(4,0, 0,2,2, 2, 0)));

        Fermentables.put(Items.PUMPKIN, new Fermentable(0xffE38A1D,0.125f, 0.08f, 0.3f,
                new FlavorProfile(3,0, 0,1,2, 4, 0)));

        Fermentables.put(Items.PUMPKIN_PIE, new Fermentable(0xffd88b50,0.375f, 0.4f, 0.3f,
                new FlavorProfile(1,3, 0,1,1, 4, 0)));

        Fermentables.put(Items.CHORUS_FRUIT, new Fermentable(0xff8E678D, 0.125f, 0.125f, 0.7f,
                new FlavorProfile(0,0, 4,0,0, 2, 4)));

        Fermentables.put(Items.KELP, new Fermentable(0xff5C8332,0.125f, 0.05f, 0.3f,
                new FlavorProfile(0,0, 0,5,5, 0, 0)));

        Fermentables.put(Items.DRIED_KELP, new Fermentable(0xff3C3324, 0.125f, 0.1f, 0.6f,
                new FlavorProfile(0,0, 0,5,5, 0, 0)));

        Fermentables.put(Items.CAKE, new Fermentable(0xffa77d61, 0.5f, 0.6f, 0.7f,
                new FlavorProfile(0,3, 0,0,0, 7, 0)));

        Fermentables.put(Items.COOKIE, new Fermentable(0xffbc6e37,0.125f, 0.6f, 0.8f,
                new FlavorProfile(0,3, 0,0,1, 6, 0)));

        Fermentables.put(Items.HONEY_BOTTLE, new Fermentable(0xffffd32d,0.125f, 0.9f, 0.8f,
                new FlavorProfile(0,0, 0,1,0, 9, 0)));

        Fermentables.put(Items.SWEET_BERRIES, new Fermentable(0xffdf467e,0.125f, 0.125f, 0.8f,
                new FlavorProfile(0,0, 2,0,0, 3, 5)));

        Fermentables.put(Items.AIR, new Fermentable(MaterialColor.WATER.color,0, 0, 0,
                new FlavorProfile(0,0, 0,0,0, 0, 0)));
    }
}
