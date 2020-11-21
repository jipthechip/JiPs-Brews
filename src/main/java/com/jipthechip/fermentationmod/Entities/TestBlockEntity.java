package com.jipthechip.fermentationmod.Entities;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

import java.util.Arrays;

public class TestBlockEntity extends BlockEntity implements BlockEntityClientSerializable {

    public boolean on;

    public TestBlockEntity(BlockEntityType<?> type) {
        super(type);
        this.on = false;
        markDirty();
    }

    public TestBlockEntity(){
        super(BlockEntitiesList.TEST_BLOCK_ENTITY);
        this.on = false;
        markDirty();
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data){
        System.out.println("SyncedBlockEvent type: "+type+" data: "+data);
        return false;
    }



    // read
    @Override
    public void fromTag(BlockState blockState, CompoundTag compoundTag){
        super.fromTag(blockState, compoundTag);
        System.out.println(this.getPos() + " fromTag read: "+ compoundTag.getBoolean("isOn"));
        this.on = compoundTag.getBoolean("isOn");
    }

    // write
    @Override
    public CompoundTag toTag(CompoundTag compoundTag){
        super.toTag(compoundTag);
        compoundTag.putBoolean("isOn", this.on);
        System.out.println(this.getPos() + " toTag wrote: "+this.on);
        return compoundTag;
    }


    // read
    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        boolean isOn = compoundTag.getBoolean("isOn");
        System.out.println(this.getPos() + " fromClientTag read: "+isOn);
        this.on = isOn;
    }

    // write
    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        compoundTag.putBoolean("isOn", this.on);
        System.out.println(this.getPos() + " toClientTag wrote: "+this.on);
        return compoundTag;
    }

    public void toggleOn(){
        this.on = !this.on;
    }

    public boolean isOn(){
        return this.on;
    }
}
