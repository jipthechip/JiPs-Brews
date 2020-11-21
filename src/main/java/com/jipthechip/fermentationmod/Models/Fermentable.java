package com.jipthechip.fermentationmod.Models;

public class Fermentable {

    int color;
    float volume;
    float sugar_content;
    float flavor_intensity;
    FlavorProfile flavorProfile;

    public Fermentable(int color, float volume, float sugar_content, float flavor_intensity, FlavorProfile flavorProfile){
        this.color = color;
        this.volume = volume;
        this.sugar_content = sugar_content;
        this.flavor_intensity = flavor_intensity;
        this.flavorProfile = flavorProfile;
    }

    public int getColor(){
        return color;
    }

    public float getVolume(){
        return volume;
    }

    public float getSugarContent(){
        return sugar_content;
    }

    public float getFlavorIntensity(){
        return flavor_intensity;
    }

    public FlavorProfile getFlavorProfile(){
        return flavorProfile;
    }
}
