package com.jipthechip.fermentationmod.Entities;

import com.jipthechip.fermentationmod.Blocks.BlockList;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockEntitiesList {
    public static BlockEntityType<TestBlockEntity> TEST_BLOCK_ENTITY;
    public static BlockEntityType<MasherBlockEntity> MASHER;
    public static BlockEntityType<CarboyBlockEntity> CARBOY;
    public static BlockEntityType<MasonJarBlockEntity> MASON_JAR;

    public static void registerBlockEntities(){
        TEST_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("fermentationmod", "test_block_entity"), BlockEntityType.Builder.create(TestBlockEntity::new, BlockList.FABRIC_BLOCK).build(null));

        MASHER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("fermentationmod", "masher"), BlockEntityType.Builder.create(MasherBlockEntity::new, BlockList.MASHER_BASIN).build(null));

        CARBOY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("fermentationmod", "carboy"), BlockEntityType.Builder.create(CarboyBlockEntity::new, BlockList.CARBOY).build(null));

        MASON_JAR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("fermentationmod", "mason_jar"), BlockEntityType.Builder.create(MasonJarBlockEntity::new, BlockList.MASON_JAR).build(null));

    }
}
