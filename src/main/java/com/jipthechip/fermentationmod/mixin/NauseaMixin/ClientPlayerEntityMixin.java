package com.jipthechip.fermentationmod.mixin.NauseaMixin;

import com.jipthechip.fermentationmod.Utils.NauseaUpdater;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements NauseaUpdater {

    private static final float BASE_MAX_TRANSLATION_RATE = 0.04f;
    private static final float BASE_STARTUP_RATE = 0.0013334f;

    private float max_translation_rate;
    private float startup_rate;

    private float lastTranslationStrength = 0;
    private float nextTranslationStrength = 0;
    private int initialDuration = 0;
    private boolean initializedNausea = false;
    private float translationDistance = 1.0f;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "updateNausea()V", at = @At(value = "TAIL"))
    private void updateTranslation(CallbackInfo ci){
        lastTranslationStrength = nextTranslationStrength;

        // initialize the effect
        if(this.hasStatusEffect(StatusEffects.NAUSEA) && !initializedNausea) {
            System.out.println("starting nausea");
            initialDuration = this.getStatusEffect(StatusEffects.NAUSEA).getDuration();
            initializedNausea = true;
            max_translation_rate = BASE_MAX_TRANSLATION_RATE;
            startup_rate = BASE_STARTUP_RATE;
            translationDistance = 1.0f;
            System.out.println("max_translation_rate: "+max_translation_rate);
            System.out.println("startup_rate: "+startup_rate);
        }
        // calculate the translation rate when the effect is starting up or running normally
        else if (this.hasStatusEffect(StatusEffects.NAUSEA) && this.getStatusEffect(StatusEffects.NAUSEA).getDuration() > 60) {
            this.nextTranslationStrength = (float)Math.pow(initialDuration - this.getStatusEffect(StatusEffects.NAUSEA).getDuration(), 2.0f) * startup_rate;
            if(this.nextTranslationStrength - this.lastTranslationStrength >= max_translation_rate){
                this.nextTranslationStrength = this.lastTranslationStrength + max_translation_rate;
            }
        }
        // calculate the translation rate when the effect is cooling down/stopping
        else {
            if(this.hasStatusEffect(StatusEffects.NAUSEA)){
                if(this.translationDistance > 1.0E-5){
                    // find out which direction is closer to 0 and translate in that direction
                    if(nextTranslationStrength % (2.0f * Math.PI) >= Math.PI){
                        this.nextTranslationStrength = this.lastTranslationStrength + max_translation_rate;
                    }else{
                        this.nextTranslationStrength = this.lastTranslationStrength - max_translation_rate;
                    }
                }
            }
            // if the effect is no longer active, reset the data
            else if(initializedNausea){
                System.out.println("nausea effect no longer active");
                resetData();
            }
        }
    }

    // the translation value from the last tick
    public float getLastTranslationStrength(){
        return lastTranslationStrength;
    }

    // the translation value of the next tick
    public float getNextTranslationStrength(){
        return nextTranslationStrength;
    }

    // set the current distance of the translation
    public void setTranslationDistance(float distance) {
        if(this.translationDistance > 1.0E-5 || distance < this.translationDistance) this.translationDistance = distance;
    }

    // get the maximum translation speed
    public float getMaxRate(){
        return max_translation_rate;
    }

    // calculate the derivative of the translation when starting up/running normally
    public float getDerivative(float tickDelta){
        if(! this.hasStatusEffect(StatusEffects.NAUSEA)) return 0.0f;
        float duration = this.getStatusEffect(StatusEffects.NAUSEA).getDuration() - tickDelta;
        return getStatusEffect(StatusEffects.NAUSEA).getDuration() > 60 ? Math.min(2.0f*(initialDuration - duration)* startup_rate, max_translation_rate) : (nextTranslationStrength % (2.0f * Math.PI) >= Math.PI ? max_translation_rate : -1.0f * max_translation_rate);
    }

    // the duration of the effect when it starts
    public int getInitialDuration(){
        return this.initialDuration;
    }

    // reset data when the effect stops
    private void resetData(){
        initializedNausea = false;
        initialDuration = 0;
        translationDistance = 0;
        lastTranslationStrength = 0;
        nextTranslationStrength = 0;
    }
}
