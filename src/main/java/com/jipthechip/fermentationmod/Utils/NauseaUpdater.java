package com.jipthechip.fermentationmod.Utils;

public interface NauseaUpdater {

    float getLastTranslationStrength();

    float getNextTranslationStrength();

    void setTranslationDistance(float distance);

    float getMaxRate();

    float getDerivative(float tickDelta);

    int getInitialDuration();
}
