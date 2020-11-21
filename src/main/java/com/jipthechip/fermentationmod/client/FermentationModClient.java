package com.jipthechip.fermentationmod.client;

import com.jipthechip.fermentationmod.Blocks.BlockColorList;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FermentationModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockColorList.registerBlockColors();
    }
}
