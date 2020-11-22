package com.jipthechip.fermentationmod.Particles;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.Objects;
import java.util.Random;

public class ParticleList {
    public static final Identifier PLAY_BLOCK_PARTICLE_PACKET_ID = new Identifier("fermentationmod", "particle");

    public static void registerParticles(){
        ClientSidePacketRegistry.INSTANCE.register(PLAY_BLOCK_PARTICLE_PACKET_ID,
                (packetContext, attachedData) -> {

                    BlockPos pos = attachedData.readBlockPos();
                    double particleX = attachedData.readDouble();
                    double particleY = attachedData.readDouble();
                    double particleZ = attachedData.readDouble();
                    Identifier particleTypeIdentifier = attachedData.readIdentifier();

                    packetContext.getTaskQueue().execute(() -> MinecraftClient.getInstance().particleManager.addParticle(
                        (DefaultParticleType) Objects.requireNonNull(Registry.PARTICLE_TYPE.get(particleTypeIdentifier)),
                            pos.getX() + particleX, pos.getY() + particleY, pos.getZ() + particleZ,
                            0.0D, 0.0D, 0.0D
                    ));
                });
    }
}
