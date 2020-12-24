package com.jipthechip.fermentationmod.Entities;

import com.jipthechip.fermentationmod.Items.YeastCulture;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

import java.util.Objects;
import java.util.Random;


public class MasonJarBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Tickable {

    int completionProgress;
    int completionGoal;
    int productionType;
    DefaultedList<ItemStack> generatedYeast;

    public MasonJarBlockEntity() {
        super(BlockEntitiesList.MASON_JAR);
        completionProgress = 0;
        completionGoal = 0;
        productionType = 0;
        generatedYeast = DefaultedList.ofSize(4, ItemStack.EMPTY);
        markDirty();
    }

    public boolean isComplete(){
        return completionProgress >= completionGoal;
    }

    public boolean isInProgress(){
        return productionType != 0;
    }

    @Override
    public void fromTag(BlockState blockState, CompoundTag compoundTag){
        super.fromTag(blockState, compoundTag);
        completionProgress = compoundTag.getInt("completionProgress");
        completionGoal = compoundTag.getInt("completionGoal");
        productionType = compoundTag.getInt("productionType");
    }

    // write
    @Override
    public CompoundTag toTag(CompoundTag compoundTag){
        super.toTag(compoundTag);
        compoundTag.putInt("completionProgress", completionProgress);
        compoundTag.putInt("completionGoal", completionGoal);
        compoundTag.putInt("productionType", productionType);
        return compoundTag;
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        completionProgress = compoundTag.getInt("completionProgress");
        completionGoal = compoundTag.getInt("completionGoal");
        productionType = compoundTag.getInt("productionType");
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        compoundTag.putInt("completionProgress", completionProgress);
        compoundTag.putInt("completionGoal", completionGoal);
        compoundTag.putInt("productionType", productionType);
        return compoundTag;
    }

    public void setCompletionGoal(int goal){
        completionGoal = goal;
    }

    public void setProductionType(int type){
        productionType = type;
    }

    public void resetData(){
        completionProgress = 0;
        completionGoal = 0;
        productionType = 0;
        markDirty();
    }

    @Override
    public void tick() {
        if (productionType == 0) {
            return;
        }
        if (completionProgress == 0){
            switch (productionType) {
                case 1: // produce random low-stat yeast
                    completionGoal = 200;
                    break;
                case 2: // produce copy of inputted yeast
                    completionGoal = 201;
                    break;
                case 3: // breed 2 yeast cultures together
                    completionGoal = 202;
                    break;
            }
        }
        if(completionProgress < completionGoal){
            completionProgress++;
        }else{
            if(! Objects.requireNonNull(this.getWorld()).isClient){
                switch(productionType){
                    case 1: // produce random low-stat yeast
                        Random random = new Random();
                        int type = random.nextInt(2);
                        ItemStack stack;
                        if(type == 0){
                            System.out.println("making low tier yeast");
                            stack = YeastCulture.generateLowTierYeast();
                        }else {
                            System.out.println("making mid tier yeast");
                            stack = YeastCulture.generateMidTierYeast();
                        }
                        this.getWorld().spawnEntity(new ItemEntity(world, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5,
                                stack));
                        break;
                    case 2: // produce copy of inputted yeast
                        break;
                    case 3: // breed 2 yeast cultures together
                        break;
                }
            }

            productionType = 0;
            completionProgress = 0;
            completionGoal = 0;
        }
    }
}
