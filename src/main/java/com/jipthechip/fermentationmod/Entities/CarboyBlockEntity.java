package com.jipthechip.fermentationmod.Entities;

import com.jipthechip.fermentationmod.Utils.UtilList;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

import java.util.Arrays;

public class CarboyBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable {

    private float sugar_content;
    private float [] flavors;
    private int color;

    public CarboyBlockEntity() {
        super(BlockEntitiesList.CARBOY);
        sugar_content = 0;
        flavors = new float[7];
        Arrays.fill(flavors, 0);
        color = 0;
        markDirty();
    }

    public void addMash(CompoundTag tag){
        sugar_content = tag.getFloat("sugar_content");
        flavors = UtilList.intArrayToFloat(tag.getIntArray("flavors"));
        color = tag.getInt("color");
        markDirty();
    }

    @Override
    public void tick() {

    }

    public void resetData(){
        sugar_content = 0;
        flavors = new float[7];
        Arrays.fill(flavors, 0);
        color = 0;
        markDirty();
    }

    public float getSugarContent(){
        return sugar_content;
    }

    public float [] getFlavors(){
        return flavors;
    }

    public int getColor(){
        return color;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag compoundTag) {
        super.fromTag(state, compoundTag);
        sugar_content = compoundTag.getFloat("sugar_content");
        flavors = UtilList.intArrayToFloat(compoundTag.getIntArray("flavors"));
        color = compoundTag.getInt("color");
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        super.toTag(compoundTag);
        compoundTag.putFloat("sugar_content", sugar_content);
        compoundTag.putIntArray("flavors", UtilList.floatArrayToInt(flavors));
        compoundTag.putInt("color", color);
        return compoundTag;
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        sugar_content = compoundTag.getFloat("sugar_content");
        flavors = UtilList.intArrayToFloat(compoundTag.getIntArray("flavors"));
        color = compoundTag.getInt("color");
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        compoundTag.putFloat("sugar_content", sugar_content);
        compoundTag.putIntArray("flavors", UtilList.floatArrayToInt(flavors));
        compoundTag.putInt("color", color);
        return compoundTag;
    }
}
