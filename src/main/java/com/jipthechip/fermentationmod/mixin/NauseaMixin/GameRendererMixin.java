package com.jipthechip.fermentationmod.mixin.NauseaMixin;

import com.jipthechip.fermentationmod.Utils.NauseaUpdater;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private MinecraftClient client;
    @Shadow private float zoom;

    @ModifyVariable(method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V", at = @At(value = "STORE"), name = "f")
    private float changeF(float f){
        return f;
    }

    @ModifyVariable(method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V", at = @At(value = "STORE"), name = "g")
    private float changeG(float g){
        assert client.player != null;
        if(client.player.hasStatusEffect(StatusEffects.NAUSEA)){
            if(client.player.getStatusEffect(StatusEffects.NAUSEA).getAmplifier() <= 1.0f){
                return 1.0f; // apply no scaling/rotation when the level is 1 or less
            }else if(client.player.getStatusEffect(StatusEffects.NAUSEA).getAmplifier() <= 2.0f){
                return (float)Math.sqrt(g); // apply limited scaling when the level is 2
            }
        }
        // otherwise apply full scaling
        return g;

    }

    // rotation speed
    @ModifyVariable(method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V", at = @At(value = "STORE"), name = "i")
    private int changeI(int i){
        assert client.player != null;
        if(!client.player.hasStatusEffect(StatusEffects.NAUSEA))return i;
        int amplifier = Objects.requireNonNull(client.player.getStatusEffect(StatusEffects.NAUSEA)).getAmplifier();
        i += Math.max(0.0f, amplifier - 3.0f) * i; // increase rotation speed by i for every level after 3
        return i;
    }

    @Inject(method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/util/math/MatrixStack$Entry;getModel()Lnet/minecraft/util/math/Matrix4f;", ordinal = 1), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void translateCamera(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci, boolean bl, Camera camera, MatrixStack matrixStack){
        assert client.player != null;

        NauseaUpdater nauseaUpdater = (NauseaUpdater) client.player;

        // get the next and last translation values from the updater, and
        // calculate what the translation value should be on this frame
        float t = MathHelper.lerp(tickDelta, nauseaUpdater.getLastTranslationStrength(), nauseaUpdater.getNextTranslationStrength());

        if(t > 0.0f){
            int amplifier = -1;
            int duration = 0;
            if(client.player.hasStatusEffect(StatusEffects.NAUSEA)){
                amplifier = Objects.requireNonNull(client.player.getStatusEffect(StatusEffects.NAUSEA)).getAmplifier();
                duration = client.player.getStatusEffect(StatusEffects.NAUSEA).getDuration();
            }
            // slowly increase the translation rate on startup
            float startupModifier = (nauseaUpdater.getDerivative(tickDelta)) / nauseaUpdater.getMaxRate();
            float radians = (t)*(startupModifier);

            float translation = (float)( -1.0f * Math.cos(radians) + 1.0f)/2.0f;

            // all the various levels of the effect
            if(amplifier == 0){
                // apply head moving side to side and zoom camera 0.5 units
                matrixStack.translate(Math.sin(translation *2.0f*Math.PI)/4.0f, 0.0f, 0.0f);
                this.zoom = 1.0f + translation * 0.5f;
            }else if(amplifier == 1){
                // apply head moving side to side and zoom camera 1.0 units
                matrixStack.translate(Math.sin(translation *2.0f*Math.PI)/3.0f, 0.0f, 0.0f);
                this.zoom = 1.0f + translation;
            } else if(amplifier >= 2 && nauseaUpdater.getInitialDuration() >= 80.0f) {
                // apply camera moving in a circular motion and zoom camera 1.0 units

                // calculations for the x and y points of the circle
                float x = (float) Math.sin(translation * 2.0f * Math.PI);
                float y = (float) Math.sqrt(1.0f - x * x);

                if (amplifier < 3) {
                    // decrease radius of circle
                    x *= 0.5;
                    y *= 0.5;
                }

                // check if the effect is starting up or cooling down, and apply appropriate scaling to the radius of the circle
                if (duration <= 60.0f) {
                    float cooldownRadius = Math.max(0.0f, (duration - 50.0f - tickDelta) / 10.0f);
                    x *= cooldownRadius;
                    y *= cooldownRadius;
                }
                else if (duration > nauseaUpdater.getInitialDuration() - 20.0f) {
                    float startupRadius = Math.max(0.0f, (nauseaUpdater.getInitialDuration() - (duration - tickDelta))) / 20.0f;
                    x *= startupRadius;
                    y *= startupRadius;
                }

                // translate the top or bottom half of the circle depending on the derivative of x
                if (2.0f * Math.PI * Math.cos(translation * 2.0f * Math.PI) >= 0) {
                    matrixStack.translate(x, y, 0.0f);
                } else {
                    matrixStack.translate(x, -1.0f * y, 0.0f);
                }

                // zoom the camera
                this.zoom = 1.0f + translation;
            }

            // tell the nausea updater the distance of the translation so it knows when it needs to stop
            if(client.player.hasStatusEffect(StatusEffects.NAUSEA)){
                System.out.println("duration: "+duration);
                // starts slightly ahead of the updater because this needs to be set before the updater checks the
                // translation distance
                if(duration < 60.0f){
                    ((NauseaUpdater) client.player).setTranslationDistance(translation);
                }
            }
        }
    }

    // rotation axis
    /*@ModifyVariable(method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V", at = @At(value = "STORE"), ordinal = 0)
    private Vector3f changeXrotation(Vector3f vector3f){
        int amplifier = Objects.requireNonNull(client.player.getStatusEffect(StatusEffects.NAUSEA)).getAmplifier();
        return new Vector3f(0, MathHelper.SQUARE_ROOT_OF_TWO/2, MathHelper.SQUARE_ROOT_OF_TWO/2);
    }*/

    /*@Redirect(method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/util/math/Vector3f;getDegreesQuaternion(F)Lnet/minecraft/util/math/Quaternion;", ordinal = 0))
    private Quaternion changeRotationQ(Vector3f vector3f, float angle){
        assert client.player != null;
        int amplifier = Objects.requireNonNull(client.player.getStatusEffect(StatusEffects.NAUSEA)).getAmplifier();
        return vector3f.getDegreesQuaternion(angle * amplifier);
    }

    @Redirect(method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/util/math/Vector3f;getDegreesQuaternion(F)Lnet/minecraft/util/math/Quaternion;", ordinal = 1))
    private Quaternion changeRotationConjQ(Vector3f vector3f, float angle){
        assert client.player != null;
        int amplifier = Objects.requireNonNull(client.player.getStatusEffect(StatusEffects.NAUSEA)).getAmplifier();
        return vector3f.getDegreesQuaternion(angle * amplifier);
    }*/
}