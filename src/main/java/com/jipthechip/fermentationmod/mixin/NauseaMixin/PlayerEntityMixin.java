package com.jipthechip.fermentationmod.mixin.NauseaMixin;

import com.jipthechip.fermentationmod.Utils.EntityAlcoholStats;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /*@Inject(method = "tick()V", at = @At("HEAD"))
    private void tickInfo(CallbackInfo ci){
        System.out.println("playerentity tick BAC: "+((EntityAlcoholStats) this).getBAC());
    }*/
}
