package com.jipthechip.fermentationmod.Blocks;

import com.jipthechip.fermentationmod.Entities.CarboyBlockEntity;
import com.jipthechip.fermentationmod.Entities.MasherBlockEntity;
import com.jipthechip.fermentationmod.Entities.TestBlockEntity;
import com.jipthechip.fermentationmod.Models.FermentableMap;
import com.jipthechip.fermentationmod.Utils.UtilList;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.color.block.BlockColorProvider;

import javax.rmi.CORBA.Util;

public class BlockColorList {

    private static final BlockColorProvider FABRIC_BLOCKCOLOR =
            (state, view, pos, tintIndex) -> {
                assert view != null;
                BlockEntity blockEntity = view.getBlockEntity(pos);
                assert blockEntity != null;
                TestBlockEntity testBlockEntity = (TestBlockEntity) blockEntity;
                System.out.println("Checking blockcolor");
                if(testBlockEntity.isOn()){
                    //System.out.println("testBlockEntity is on");
                    return 0x00008000;
                }
                //System.out.println("testBlockEntity is off");
                return 0;
            };

    private static final BlockColorProvider MASHER_BASIN_BLOCKCOLOR =
            (state, view, pos, tintIndex) -> {
                assert view != null;
                MasherBlockEntity masherBlockEntity = (MasherBlockEntity) view.getBlockEntity(pos);
                if(masherBlockEntity == null) return 0;
                return masherBlockEntity.getColor();
            };

    private static final BlockColorProvider CARBOY_BLOCKCOLOR =
            (state, view, pos, tintIndex) -> {
                assert view != null;
                BlockEntity blockEntity = view.getBlockEntity(pos);
                CarboyBlockEntity carboyBlockEntity = (CarboyBlockEntity) blockEntity;
                if (carboyBlockEntity == null) return 0;
                if(tintIndex == 2) return Material.WATER.getColor().color;
                return carboyBlockEntity.getColor();
            };

    private static final BlockColorProvider AIRLOCK_BLOCKCOLOR =
            (state, view, pos, tintIndex) -> {
                assert view != null;
                return Material.WATER.getColor().color;
            };

    public static void registerBlockColors(){
        ColorProviderRegistry.BLOCK.register(FABRIC_BLOCKCOLOR, BlockList.FABRIC_BLOCK);
        ColorProviderRegistry.BLOCK.register(MASHER_BASIN_BLOCKCOLOR, BlockList.MASHER_BASIN);
        ColorProviderRegistry.BLOCK.register(CARBOY_BLOCKCOLOR, BlockList.CARBOY);
        ColorProviderRegistry.BLOCK.register(AIRLOCK_BLOCKCOLOR, BlockList.AIRLOCK);
    }
}
