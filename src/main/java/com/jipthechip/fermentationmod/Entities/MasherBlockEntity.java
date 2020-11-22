package com.jipthechip.fermentationmod.Entities;

import com.jipthechip.fermentationmod.Models.*;
import com.jipthechip.fermentationmod.Utils.UtilList;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import com.jipthechip.fermentationmod.Models.FermentableMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.jipthechip.fermentationmod.Utils.UtilList.*;

public class MasherBlockEntity extends BlockEntity implements BlockEntityClientSerializable {

    private static final int MAX_STACKS = 8;

    private int stir_progress;
    private float volume;
    private float sugar_content;
    private float [] flavors;
    private DefaultedList<ItemStack> inventory;
    private boolean [] items_stirred;

    public MasherBlockEntity() {
        super(BlockEntitiesList.MASHER);

        stir_progress = 0;
        volume = 0;
        sugar_content = 0;

        flavors = new float[7];
        Arrays.fill(flavors, 0);

        inventory = DefaultedList.ofSize(MAX_STACKS, ItemStack.EMPTY);

        items_stirred = new boolean[MAX_STACKS];
        Arrays.fill(items_stirred, false);

        markDirty();
    }

    public boolean addItem(ItemStack itemStack){
        Item item = itemStack.getItem();
        if(!FermentableMap.Fermentables.containsKey(item) || isFull()) {
            System.out.println("list full: "+!inventory.contains(ItemStack.EMPTY)+ " item not in map: "+!FermentableMap.Fermentables.containsKey(item));
            return false;
        }

        Fermentable fermentable = FermentableMap.Fermentables.get(item);
        float volume = fermentable.getVolume();

        if(this.volume + volume > 1.0f) {
            System.out.println(this.volume + " " + volume + " volume too big");
            return false;
        }

        for(int i = 1; i < MAX_STACKS; i++){
            inventory.set(i-1, inventory.get(i));
            items_stirred[i-1] = items_stirred[i];
        }
        inventory.set(MAX_STACKS-1,itemStack);
        items_stirred[MAX_STACKS-1] = false;

        float sugar_content = fermentable.getSugarContent();
        float flavor_intensity = fermentable.getFlavorIntensity();
        float [] flavors = fermentable.getFlavorProfile().getFlavors().clone();

        float total_sugar = volume * sugar_content;
        this.sugar_content += total_sugar;
        for(int i = 0; i < flavors.length; i++){
            flavors[i] *= volume * flavor_intensity;
            this.flavors[i] += flavors[i];
        }
        this.volume += volume;
        markDirty();
        System.out.println("volume: "+this.volume);
        System.out.println("sugar content: "+this.sugar_content);
        System.out.println("flavors: "+ Arrays.toString(this.flavors));
        return true;
    }

    public ItemStack removeItem(){
        if (! this.containsItems() || items_stirred[MAX_STACKS - 1]) return ItemStack.EMPTY;
        System.out.println(inventory);
        ItemStack itemStack = inventory.get(MAX_STACKS - 1);
        for(int i = MAX_STACKS - 2; i >= 0; i--){
            inventory.set(i + 1, inventory.get(i));
            items_stirred[i+1] = items_stirred[i];
        }
        inventory.set(0, ItemStack.EMPTY);

        if (inventory.get(MAX_STACKS - 1) == ItemStack.EMPTY) this.stir_progress = 0;
        System.out.println(inventory);
        Fermentable fermentable = FermentableMap.Fermentables.get(itemStack.getItem());

        float volume = fermentable.getVolume();
        float sugar_content = fermentable.getSugarContent();
        float flavor_intensity = fermentable.getFlavorIntensity();
        float [] flavors = fermentable.getFlavorProfile().getFlavors().clone();

        float total_sugar = volume * sugar_content;
        this.sugar_content -= total_sugar;
        for(int i = 0; i < flavors.length; i++){
            flavors[i] *= volume * flavor_intensity;
            this.flavors[i] -= flavors[i];
        }
        this.volume -= volume;
        if (inventory.get(MAX_STACKS - 1) == ItemStack.EMPTY) this.sugar_content = 0;

        markDirty();

        System.out.println("volume: "+this.volume);
        System.out.println("sugar content: "+this.sugar_content);
        System.out.println("flavors: "+ Arrays.toString(this.flavors));

        return itemStack;
    }

    public List<ItemStack> getInventory(){
        return inventory;
    }

    public float getVolume() {
        return this.volume;
    }

    public int getStirProgress(){
        return stir_progress;
    }

    public boolean stir(){
        if(stir_progress >= 10) return false;
        stir_progress++;
        Arrays.fill(items_stirred, true);
        return true;
    }
    public void resetStirProgress(){
        stir_progress = 0;
    }

    public boolean containsItems(){
        return ! (inventory.get(MAX_STACKS - 1) == ItemStack.EMPTY);
    }

    public boolean isFull(){
        return ! inventory.contains(ItemStack.EMPTY);
    }

    // read
    @Override
    public void fromTag(BlockState blockState, CompoundTag compoundTag){
        super.fromTag(blockState, compoundTag);
        stir_progress = compoundTag.getInt("stir_progress");
        volume = compoundTag.getFloat("volume");
        sugar_content = compoundTag.getFloat("sugar_content");
        flavors = intArrayToFloat(compoundTag.getIntArray("flavors"));
        items_stirred = byteArrayToBoolean(compoundTag.getByteArray("stirred_items"));

        Inventories.fromTag(compoundTag, inventory);

        markDirty();
    }

    // write
    @Override
    public CompoundTag toTag(CompoundTag compoundTag){
        super.toTag(compoundTag);
        compoundTag.putInt("stir_progress", stir_progress);
        compoundTag.putFloat("volume", volume);
        compoundTag.putFloat("sugar_content", sugar_content);
        compoundTag.putIntArray("flavors", floatArrayToInt(flavors));
        compoundTag.putByteArray("stirred_items", booleanArrayToByte(items_stirred));

        Inventories.toTag(compoundTag, inventory);

        markDirty();
        return compoundTag;
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        stir_progress = compoundTag.getInt("stir_progress");
        volume = compoundTag.getFloat("volume");
        sugar_content = compoundTag.getFloat("sugar_content");
        flavors = intArrayToFloat(compoundTag.getIntArray("flavors"));
        items_stirred = byteArrayToBoolean(compoundTag.getByteArray("stirred_items"));

        Inventories.fromTag(compoundTag, inventory);

        markDirty();
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        compoundTag.putInt("stir_progress", stir_progress);
        compoundTag.putFloat("volume", volume);
        compoundTag.putFloat("sugar_content", sugar_content);
        compoundTag.putIntArray("flavors", floatArrayToInt(flavors));
        compoundTag.putByteArray("stirred_items", booleanArrayToByte(items_stirred));

        Inventories.toTag(compoundTag, inventory);

        markDirty();
        return compoundTag;
    }
}
