package com.jipthechip.fermentationmod.Entities;

import com.jipthechip.fermentationmod.Utils.UtilList;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.world.biome.layer.type.SouthEastSamplingLayer;

import java.util.Arrays;
import java.util.Random;

import static com.google.common.primitives.Floats.max;

public class CarboyBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable {

    private float sugar_content;
    private float alcohol_content;
    private float [] flavors;
    private int color;

    private int tickCounter;
    private boolean fermenting;

    private float fermentation_alcohol_resistance;
    private float fermentation_rate;
    private float fermentation_efficiency;
    private float [] fermentation_flavor_change_rates;


    public CarboyBlockEntity() {
        super(BlockEntitiesList.CARBOY);
        sugar_content = 0;
        alcohol_content = 0;
        flavors = new float[7];
        Arrays.fill(flavors, 0);
        color = 0;
        fermenting = false;
        markDirty();
    }

    public void addMash(CompoundTag tag){
        sugar_content = tag.getFloat("sugar_content");
        flavors = UtilList.intArrayToFloat(tag.getIntArray("flavors"));
        color = tag.getInt("color");
        markDirty();
    }

    public void startFermentation(CompoundTag yeast){
        assert yeast != null;
        float[] yeast_resistances = UtilList.intArrayToFloat(yeast.getIntArray("resistances"));
        float[] yeast_speeds = UtilList.intArrayToFloat(yeast.getIntArray("speed"));
        float[] yeast_efficiencies = UtilList.intArrayToFloat(yeast.getIntArray("efficiency"));
        float[] yeast_flavors = UtilList.intArrayToFloat(yeast.getIntArray("flavors"));

        System.out.println("Yeast resistances: "+ Arrays.toString(yeast_resistances));
        System.out.println("Yeast speeds: "+ Arrays.toString(yeast_speeds));
        System.out.println("Yeast efficiencies: "+ Arrays.toString(yeast_efficiencies));

        fermentation_alcohol_resistance = getFermentationStat(yeast_resistances) + 0.03f;
        if(fermentation_alcohol_resistance <= alcohol_content) return; // don't start fermentation if the mixture has more alcohol than the yeast can handle
        fermentation_rate = getFermentationStat(yeast_speeds) + 1.0f;
        fermentation_efficiency = getFermentationStat(yeast_efficiencies) + 0.5f;
        fermentation_flavor_change_rates = yeast_flavors;

        System.out.println("Fermentation Starting");
        System.out.println("-----------------------");
        System.out.println("Alcohol Tolerance: "+fermentation_alcohol_resistance);
        System.out.println("Rate: "+fermentation_rate);
        System.out.println("Efficiency: "+fermentation_efficiency);
        System.out.println("Flavor Change per 100 ticks: "+ Arrays.toString(fermentation_flavor_change_rates));
        System.out.println("-----------------------");
        System.out.println("Sugar content: "+sugar_content);
        System.out.println("Alcohol content: "+alcohol_content);
        System.out.println("Flavors: "+Arrays.toString(flavors));
        System.out.println("-----------------------");
        System.out.println();
        fermenting = true;
        tickCounter = 0;
        markDirty();
    }

    private float getFermentationStat(float [] yeastStats){
        float stat = 0;
        for(int i = 0; i < yeastStats.length; i++){
            stat += yeastStats[i] * flavors[i];
        }
        return stat;
    }

    @Override
    public void tick() {
        if(!fermenting) return;
        if(tickCounter % 100 == 0){ // update every 100 ticks
            float sugarToConvert = Math.min(0.005f * fermentation_rate, sugar_content);
            float alcoholResult = sugarToConvert * fermentation_efficiency;
            for(int i = 0; i < flavors.length; i++){
                flavors[i] = Math.max(0.0f, flavors[i] + fermentation_flavor_change_rates[i]);
            }
            sugar_content -= sugarToConvert;
            alcohol_content += alcoholResult;
            System.out.println("Sugar content: "+sugar_content);
            System.out.println("Alcohol content: "+alcohol_content);
            System.out.println("Flavors: "+Arrays.toString(flavors));
            System.out.println();
            if(alcohol_content >= fermentation_alcohol_resistance || sugar_content <= 0){
                fermenting = false;
                tickCounter = 0;
                System.out.println("------------------------");
                System.out.println("Fermentation Complete.");
                System.out.println("------------------------");
                System.out.println("Sugar content: "+sugar_content);
                System.out.println("Alcohol content: "+alcohol_content);
                System.out.println("Flavors: "+Arrays.toString(flavors));
                System.out.println("------------------------");
                Random random = new Random();
                assert world != null;
                for (int i = 0; i < 20; i++) {
                    UtilList.sendParticlePacket(world, getPos(), random.nextDouble(), 0, random.nextDouble(), new Identifier("splash"));
                }
                return;
            }
            markDirty();
        }
        tickCounter++;
    }

    public void resetData(){
        sugar_content = 0;
        flavors = new float[7];
        Arrays.fill(flavors, 0);
        color = 0;
        markDirty();
    }

    public float getAlcoholContent(){
        return this.alcohol_content;
    }

    public float getSugarContent(){
        return sugar_content;
    }

    public float [] getFlavors(){
        return flavors;
    }

    public int getColor(){
        if(color == 0) return Material.GLASS.getColor().color;
        return color;
    }

    public boolean isFermenting(){
        return fermenting;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag compoundTag) {
        super.fromTag(state, compoundTag);
        sugar_content = compoundTag.getFloat("sugar_content");
        alcohol_content = compoundTag.getFloat("alcohol_content");
        flavors = UtilList.intArrayToFloat(compoundTag.getIntArray("flavors"));
        color = compoundTag.getInt("color");
        tickCounter = compoundTag.getInt("tickCounter");
        fermenting = compoundTag.getBoolean("fermenting");
        fermentation_alcohol_resistance = compoundTag.getFloat("fermentation_alcohol_resistance");
        fermentation_rate = compoundTag.getFloat("fermentation_rate");
        fermentation_efficiency = compoundTag.getFloat("fermentation_efficiency");
        fermentation_flavor_change_rates = UtilList.intArrayToFloat(compoundTag.getIntArray("fermentation_flavor_change_rates"));
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        super.toTag(compoundTag);
        compoundTag.putFloat("sugar_content", sugar_content);
        compoundTag.putFloat("alcohol_content", alcohol_content);
        compoundTag.putIntArray("flavors", UtilList.floatArrayToInt(flavors));
        compoundTag.putInt("color", color);
        compoundTag.putInt("tickCounter", tickCounter);
        compoundTag.putBoolean("fermenting", fermenting);
        compoundTag.putFloat("fermentation_alcohol_resistance", fermentation_alcohol_resistance);
        compoundTag.putFloat("fermentation_rate", fermentation_rate);
        compoundTag.putFloat("fermentation_efficiency", fermentation_efficiency);
        compoundTag.putIntArray("fermentation_flavor_change_rates", UtilList.floatArrayToInt(flavors));
        return compoundTag;
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        sugar_content = compoundTag.getFloat("sugar_content");
        alcohol_content = compoundTag.getFloat("alcohol_content");
        flavors = UtilList.intArrayToFloat(compoundTag.getIntArray("flavors"));
        color = compoundTag.getInt("color");
        tickCounter = compoundTag.getInt("tickCounter");
        fermenting = compoundTag.getBoolean("fermenting");
        fermentation_alcohol_resistance = compoundTag.getFloat("fermentation_alcohol_resistance");
        fermentation_rate = compoundTag.getFloat("fermentation_rate");
        fermentation_efficiency = compoundTag.getFloat("fermentation_efficiency");
        fermentation_flavor_change_rates = UtilList.intArrayToFloat(compoundTag.getIntArray("fermentation_flavor_change_rates"));
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        compoundTag.putFloat("sugar_content", sugar_content);
        compoundTag.putFloat("alcohol_content", alcohol_content);
        compoundTag.putIntArray("flavors", UtilList.floatArrayToInt(flavors));
        compoundTag.putInt("color", color);
        compoundTag.putInt("tickCounter", tickCounter);
        compoundTag.putBoolean("fermenting", fermenting);
        compoundTag.putFloat("fermentation_alcohol_resistance", fermentation_alcohol_resistance);
        compoundTag.putFloat("fermentation_rate", fermentation_rate);
        compoundTag.putFloat("fermentation_efficiency", fermentation_efficiency);
        compoundTag.putIntArray("fermentation_flavor_change_rates", UtilList.floatArrayToInt(flavors));
        return compoundTag;
    }
}
