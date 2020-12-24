package com.jipthechip.fermentationmod.mixin.NauseaMixin;

import com.jipthechip.fermentationmod.Utils.EntityAlcoholStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements EntityAlcoholStats {

    private static final TrackedData<Float> BAC = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.FLOAT);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker()V", at = @At("TAIL"))
    private void initTrackedAlcoholData(CallbackInfo ci){
        this.dataTracker.startTracking(BAC, 0.0f);
        //System.out.println("init BAC: "+((EntityAlcoholStats) this).getBAC());
    }

    @Inject(method = "writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("HEAD"), locals= LocalCapture.CAPTURE_FAILHARD)
    private void writeToNBT(CompoundTag tag, CallbackInfo ci){
        tag.putFloat("BAC", this.getBAC());
        //System.out.println("write BAC: "+((EntityAlcoholStats) this).getBAC());
    }

    @Inject(method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("HEAD"), locals= LocalCapture.CAPTURE_FAILHARD)
    private void readFromNBT(CompoundTag tag, CallbackInfo ci){
        this.setBAC(tag.getFloat("BAC"));
        //System.out.println("read BAC: "+((EntityAlcoholStats) this).getBAC());
    }

    @Override
    public float getBAC() {
        return this.dataTracker.get(BAC);
    }

    @Override
    public void setBAC(float bac) {
        this.dataTracker.set(BAC, MathHelper.clamp(bac, 0.0F, 1.0f));
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void tickInfo(CallbackInfo ci){
        //System.out.println("tick BAC: "+((EntityAlcoholStats) this).getBAC());
    }
}
