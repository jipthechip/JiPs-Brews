package com.jipthechip.fermentationmod.Blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockList {

    private static Material CLEAR_MATERIAL = new FabricMaterialBuilder(MaterialColor.CLEAR).lightPassesThrough().build();
    private static Material METAL_MATERIAL = new FabricMaterialBuilder(MaterialColor.IRON).build();


    public static final Block FABRIC_BLOCK = new FabricBlock(FabricBlockSettings.of(METAL_MATERIAL).hardness(4.0f));

    public static final Block MASHER_BASIN = new MasherBasinBlock(FabricBlockSettings.of(METAL_MATERIAL).hardness(3.0f).breakByHand(false).breakByTool(FabricToolTags.PICKAXES, 0).requiresTool());

    public static final Block MASHER_CRANK = new MasherCrankBlock(FabricBlockSettings.of(METAL_MATERIAL).hardness(0.5f).breakByHand(true).breakByTool(FabricToolTags.PICKAXES));

    public static final Block MASHER_ROD = new MasherRodBlock(FabricBlockSettings.of(METAL_MATERIAL).hardness(0.5f).breakByHand(true).breakByTool(FabricToolTags.PICKAXES));

    public static final Block CARBOY = new CarboyBlock(FabricBlockSettings.of(CLEAR_MATERIAL).hardness(1.0f).breakByHand(true).breakByTool(FabricToolTags.PICKAXES));

    public static final Block AIRLOCK = new AirlockBlock(FabricBlockSettings.of(CLEAR_MATERIAL).hardness(0.5f).breakByHand(true).breakByTool(FabricToolTags.PICKAXES));

    public static final Block MASON_JAR = new MasonJarBlock(FabricBlockSettings.of(CLEAR_MATERIAL).hardness(0.5f).breakByHand(true).breakByTool(FabricToolTags.PICKAXES));

    public static void registerBlocks(){
        Registry.register(Registry.BLOCK, new Identifier("fermentationmod", "fabric_block"), FABRIC_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "fabric_block"), new BlockItem(FABRIC_BLOCK, new Item.Settings().group(ItemGroup.MISC)));

        Registry.register(Registry.BLOCK, new Identifier("fermentationmod", "masher_basin"), MASHER_BASIN);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "masher_basin"), new BlockItem(MASHER_BASIN, new Item.Settings().group(ItemGroup.MISC).maxCount(1)));

        Registry.register(Registry.BLOCK, new Identifier("fermentationmod", "masher_crank"), MASHER_CRANK);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "masher_crank"), new BlockItem(MASHER_CRANK, new Item.Settings().group(ItemGroup.MISC).maxCount(1)));

        Registry.register(Registry.BLOCK, new Identifier("fermentationmod", "masher_rod"), MASHER_ROD);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "masher_rod"), new BlockItem(MASHER_ROD, new Item.Settings().group(ItemGroup.MISC).maxCount(1)));

        Registry.register(Registry.BLOCK, new Identifier("fermentationmod", "carboy"), CARBOY);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "carboy"), new BlockItem(CARBOY, new Item.Settings().group(ItemGroup.MISC).maxCount(1)));

        Registry.register(Registry.BLOCK, new Identifier("fermentationmod", "airlock"), AIRLOCK);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "airlock"), new BlockItem(AIRLOCK, new Item.Settings().group(ItemGroup.MISC).maxCount(16)));

        Registry.register(Registry.BLOCK, new Identifier("fermentationmod", "mason_jar"), MASON_JAR);
        Registry.register(Registry.ITEM, new Identifier("fermentationmod", "mason_jar"), new BlockItem(MASON_JAR, new Item.Settings().group(ItemGroup.MISC).maxCount(16)));
    }
}
