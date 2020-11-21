package com.jipthechip.fermentationmod.Models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FlavorProfile {
    private float[] flavors;

    public FlavorProfile(float earthy, float malty, float fruity, float herbal, float bitter, float sweet, float sour){
        float sum = earthy + malty + fruity + herbal + bitter + sweet + sour;
        flavors = new float[]{earthy / sum, malty/sum, fruity/sum, herbal/sum, bitter/sum, sweet/sum, sour/sum};
    }

    public float[] getFlavors(){
        return flavors;
    }
}
