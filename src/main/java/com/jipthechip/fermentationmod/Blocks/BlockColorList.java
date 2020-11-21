package com.jipthechip.fermentationmod.Blocks;

import com.jipthechip.fermentationmod.Entities.MasherBlockEntity;
import com.jipthechip.fermentationmod.Entities.TestBlockEntity;
import com.jipthechip.fermentationmod.Models.FermentableMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.color.block.BlockColorProvider;

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
                BlockEntity blockEntity = view.getBlockEntity(pos);
                assert blockEntity != null;
                MasherBlockEntity masherBlockEntity = (MasherBlockEntity) blockEntity;

                if(masherBlockEntity.getVolume() == 0) return MaterialColor.WATER.color;

                int color = FermentableMap.Fermentables.get(masherBlockEntity.getInventory().get(0).getItem()).getColor();
                for(int i = 1; i < masherBlockEntity.getInventory().size(); i++){
                    color = mixColors(1, color, FermentableMap.Fermentables.get(masherBlockEntity.getInventory().get(i).getItem()).getColor());
                }

                return mixColors(10-masherBlockEntity.getStirProgress(), MaterialColor.WATER.color, color);
            };

    private static int mixColors(int weight, int color1, int color2){

        if(weight == 0){
            return color2;
        }

        int color1_a = (color1 & 0xff000000) >> 24;
        int color1_r = (color1 & 0x00ff0000) >> 16;
        int color1_g = (color1 & 0x0000ff00) >> 8;
        int color1_b = color1 & 0x000000ff;

        int color2_a = (color2 & 0xff000000) >> 24;
        int color2_r = (color2 & 0x00ff0000) >> 16;
        int color2_g = (color2 & 0x0000ff00) >> 8;
        int color2_b = color2 & 0x000000ff;

        int result_a = color2_a;
        int result_r = color2_r;
        int result_g = color2_g;
        int result_b = color2_b;

        for(int i = 0; i < weight; i++){
            result_a = (result_a + color1_a)/2;
            result_r = (result_r + color1_r)/2;
            result_g = (result_g + color1_g)/2;
            result_b = (result_b + color1_b)/2;
        }
        result_a = result_a << 24;
        result_r = result_r << 16;
        result_g = result_g << 8;

        return result_a + result_r + result_g + result_b;
    }

    public static void registerBlockColors(){
        ColorProviderRegistry.BLOCK.register(FABRIC_BLOCKCOLOR, BlockList.FABRIC_BLOCK);
        ColorProviderRegistry.BLOCK.register(MASHER_BASIN_BLOCKCOLOR, BlockList.MASHER_BASIN);
    }
}
