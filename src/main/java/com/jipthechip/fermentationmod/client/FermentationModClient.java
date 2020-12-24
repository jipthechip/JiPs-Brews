package com.jipthechip.fermentationmod.client;

import com.jipthechip.fermentationmod.Blocks.BlockColorList;
import com.jipthechip.fermentationmod.Blocks.BlockList;
import com.jipthechip.fermentationmod.Items.ItemColorList;
import com.jipthechip.fermentationmod.Particles.ParticleList;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class FermentationModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(BlockList.CARBOY, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockList.AIRLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockList.MASON_JAR, RenderLayer.getTranslucent());
        BlockColorList.registerBlockColors();
        ItemColorList.registerItemColors();
        ParticleList.registerParticles();
    }
}
