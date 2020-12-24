package com.jipthechip.fermentationmod.Items;

import com.jipthechip.fermentationmod.Utils.UtilList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public class YeastCulture extends Item {

    private static final int BASE_HUE = 0xffFFDF89;
    private static final int [] FLAVOR_HUES = new int[]{
            0xff895200, // earthy
            0xffdcbb65, // malty
            0xffD3749F, // fruity
            0xff2A8E54, // herbal
            0xff85A301, // bitter
            0xffffffff, // sweet
            0xffBFFF00  // sour
    };

    public YeastCulture(Settings settings) {
        super(settings);
    }

    public static ItemStack setNBT(ItemStack itemStack, float[] resistances, float[] speed, float[] efficiency, float[] flavors){
        CompoundTag tag;
        if(itemStack.hasTag()) tag = itemStack.getTag();
        else {
            tag = new CompoundTag();
        }
        assert tag != null;
        tag.putIntArray("resistances", UtilList.floatArrayToInt(resistances));
        tag.putIntArray("speed", UtilList.floatArrayToInt(speed));
        tag.putIntArray("efficiency", UtilList.floatArrayToInt(efficiency));
        tag.putIntArray("flavors", UtilList.floatArrayToInt(flavors));
        tag.putInt("color", getColor(flavors, 0));
        tag.putInt("outline_color", getColor(flavors, 1));

        itemStack.setTag(tag);

        return itemStack;
    }

    public static int getColor(float[] flavors, int layer){
        int [] flavorWeights = new int[flavors.length];
        Arrays.fill(flavorWeights, 0);

        int flavorWeightCount = 0;
        for(int i = 0; i < flavors.length; i++){
            if(flavors[i] == 0) continue;
            flavorWeights[i] = (int) Math.ceil(flavors[i] * 50);
            flavorWeightCount++;
        }

        int color = BASE_HUE;
        for(int i = 0; i < flavorWeights.length; i++){
            if(flavorWeights[i] <= 0) continue;

            int weight;
            if(layer == 0){
                if(flavorWeights[i] > 8){
                    weight = 10;
                }
                else if(flavorWeights[i] >= 6){
                    weight = 7;
                }
                else {
                    weight = 7;
                }
            }else {
                if(flavorWeights[i] > 8){
                    weight = 10;
                }
                else if(flavorWeights[i] > 4){
                    weight = 10;
                }else{
                    weight = 0;
                }
                System.out.println("flavor weight: "+flavorWeights[i]);
                System.out.println("outline color weight: "+weight);
            }
            color = UtilList.mixColors(10-weight, color, FLAVOR_HUES[i]);
        }

        return color;
    }

    public static ItemStack generateLowTierYeast(){
        ItemStack yeast = new ItemStack(ItemList.YEAST_CULTURE);
        Random random = new Random();

        float [] resistance = generateYeastStats(random.nextInt(2)+1, 0.0f, 0.006f, random.nextInt(2)+1, 0.0f, -0.003f);
        float [] speed = generateYeastStats(random.nextInt(2)+1, 0.0f, 0.015f, random.nextInt(2)+1, 0.0f, -0.015f);
        float [] efficiency = generateYeastStats(random.nextInt(2)+1, 0.0f, 0.015f, random.nextInt(2)+1, 0.0f, -0.015f);
        float [] flavor = generateYeastStats(random.nextInt(2)+1, 0.0f, 0.0003f, random.nextInt(2)+1, 0.0f, -0.0003f);

        System.out.println(Arrays.toString(resistance));
        System.out.println(Arrays.toString(speed));
        System.out.println(Arrays.toString(efficiency));
        System.out.println(Arrays.toString(flavor));

        return setNBT(yeast, resistance, speed, efficiency, flavor);
    }

    public static ItemStack generateMidTierYeast(){
        ItemStack yeast = new ItemStack(ItemList.YEAST_CULTURE);
        Random random = new Random();

        float [] resistance = generateYeastStats(random.nextInt(2)+1, 0.15f, 0.035f, random.nextInt(2), 0.0f, -0.015f);
        float [] speed = generateYeastStats(random.nextInt(2)+1, 0.03f, 0.07f, random.nextInt(2), 0.0f, -0.03f);
        float [] efficiency = generateYeastStats(random.nextInt(2)+1, 0.15f, 0.035f, random.nextInt(2), 0.0f, -0.015f);
        float [] flavor = generateYeastStats(random.nextInt(2)+1, 0.08f, 0.16f, random.nextInt(2), 0.0f, -0.07f);

        System.out.println(Arrays.toString(resistance));
        System.out.println(Arrays.toString(speed));
        System.out.println(Arrays.toString(efficiency));
        System.out.println(Arrays.toString(flavor));

        return setNBT(yeast, resistance, speed, efficiency, flavor);
    }

    // each stat/bonus is per .1 units of flavor except for the flavor stat, which is per 100 ticks
    public static float [] generateYeastStats(int posCount, float posMin, float posMax, int negCount, float negMin, float negMax){

        assert posCount >= 0 && posCount <= 7 && posMin >= 0.0f && posMin <= 1.0f && posMax >= 0.0f && posMax <= 1.0f &&
                negCount >= 0 && negCount <= 7 && negMin <= 0.0f && negMin >= -1.0f && negMax <= 0.0f && negMax >= -1.0f &&
                negCount + posCount <= 7;

        Random random = new Random();

        int [] posStatIndices = new int[posCount];

        for(int i = 0; i < posCount; i++){
            int index = random.nextInt(7);
            while(UtilList.intArrayContains(posStatIndices, index)){
                index = random.nextInt(7);
            }
            posStatIndices[i] = index;
        }

        int [] negStatIndices = new int[negCount];

        for(int i = 0; i < negCount; i++){
            int index = random.nextInt(7);
            while(UtilList.intArrayContains(negStatIndices, index) || UtilList.intArrayContains(posStatIndices, index)){
                index = random.nextInt(7);
            }
            negStatIndices[i] = index;
        }

        float [] stats = new float[7];
        Arrays.fill(stats, 0.0f);
        for (int statIndex : posStatIndices) {
            stats[statIndex] = random.nextFloat() * (posMax - posMin) + posMin;
        }
        for (int statIndex : negStatIndices) {
            stats[statIndex] = random.nextFloat() * (negMax - negMin) + negMin;
        }

        return stats;
    }
}
