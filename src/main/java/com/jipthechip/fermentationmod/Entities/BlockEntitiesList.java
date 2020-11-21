package com.jipthechip.fermentationmod.Entities;

import com.jipthechip.fermentationmod.Blocks.BlockList;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockEntitiesList {
    public static BlockEntityType<TestBlockEntity> TEST_BLOCK_ENTITY;
    public static BlockEntityType<MasherBlockEntity> MASHER;

    public static void registerBlockEntities(){
        TEST_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("fermentationmod", "test_block_entity"), BlockEntityType.Builder.create(TestBlockEntity::new, BlockList.FABRIC_BLOCK).build(null));

        MASHER = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("fermentationmod", "masher"), BlockEntityType.Builder.create(MasherBlockEntity::new, BlockList.MASHER_BASIN).build(null));

    }
}
