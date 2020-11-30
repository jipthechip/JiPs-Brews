package com.jipthechip.fermentationmod.client;

import com.jipthechip.fermentationmod.Blocks.BlockColorList;
import com.jipthechip.fermentationmod.Items.ItemColorList;
import com.jipthechip.fermentationmod.Particles.ParticleList;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class FermentationModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockColorList.registerBlockColors();
        ItemColorList.registerItemColors();
        ParticleList.registerParticles();
    }
}
