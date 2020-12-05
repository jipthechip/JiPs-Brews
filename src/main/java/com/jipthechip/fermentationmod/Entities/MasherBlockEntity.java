package com.jipthechip.fermentationmod.Entities;

import com.jipthechip.fermentationmod.Models.*;
import com.jipthechip.fermentationmod.Utils.UtilList;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;
import com.jipthechip.fermentationmod.Models.FermentableMap;

import java.util.Arrays;
import java.util.List;

import static com.jipthechip.fermentationmod.Utils.UtilList.*;

public class MasherBlockEntity extends BlockEntity implements BlockEntityClientSerializable {

    private static final int MAX_STACKS = 8;

    private float volume;
    private float sugar_content;
    private float [] flavors;
    private DefaultedList<ItemStack> inventory;
    private int [] items_stirred;

    public MasherBlockEntity() {
        super(BlockEntitiesList.MASHER);

        volume = 0;
        sugar_content = 0;

        flavors = new float[7];
        Arrays.fill(flavors, 0);

        inventory = DefaultedList.ofSize(MAX_STACKS, ItemStack.EMPTY);

        items_stirred = new int[MAX_STACKS];
        Arrays.fill(items_stirred, 0);

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
        items_stirred[MAX_STACKS-1] = 0;

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

        System.out.println("Added item: "+inventory);
        return true;
    }

    public ItemStack removeItem(){
        if (! this.containsItems() || items_stirred[MAX_STACKS - 1] > 0){
            System.out.println("Failed to remove item.");
            System.out.println("Item Stir Values: "+Arrays.toString(items_stirred));
            return ItemStack.EMPTY;
        }
        System.out.println("Item Stir Values: "+Arrays.toString(items_stirred));
        ItemStack itemStack = inventory.get(MAX_STACKS - 1);
        for(int i = MAX_STACKS - 2; i >= 0; i--){
            inventory.set(i + 1, inventory.get(i));
            items_stirred[i+1] = items_stirred[i];
        }
        inventory.set(0, ItemStack.EMPTY);
        items_stirred[0] = 0;

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

        System.out.println("Removed item: "+inventory);

        markDirty();

        /*System.out.println("volume: "+this.volume);
        System.out.println("sugar content: "+this.sugar_content);
        System.out.println("flavors: "+ Arrays.toString(this.flavors));*/

        return itemStack;
    }

    public List<ItemStack> getInventory(){
        return inventory;
    }

    public float getVolume() {
        return this.volume;
    }

    public boolean getStirProgress(){
        if(! containsItems()) return false;
        for(int i = 0; i < MAX_STACKS; i++){
            if(inventory.get(i) != ItemStack.EMPTY){
                if(items_stirred[i] < 10) return false;
            }
        }
        return true;
    }

    public boolean stir(){
        for(int i = 0; i < MAX_STACKS; i++){
            if(inventory.get(i) != ItemStack.EMPTY && items_stirred[i] < 10) items_stirred[i] = items_stirred[i] + 1;
        }
        if(getStirProgress()) return false;
        System.out.println("Stirred items.");
        System.out.println("Item Stir Values: "+Arrays.toString(items_stirred));
        return true;
    }
    public void resetData(){
        System.out.println("------------------------------");
        System.out.println("resetting data");
        System.out.println("------------------------------");

        volume = 0;
        sugar_content = 0;
        Arrays.fill(flavors, 0);
        inventory = DefaultedList.ofSize(MAX_STACKS, ItemStack.EMPTY);
        Arrays.fill(items_stirred, 0);

        System.out.println("Flavors: "+ Arrays.toString(flavors));
        System.out.println("Sugar Content: "+sugar_content);
        System.out.println("Volume: "+volume);
        System.out.println("Items Stirred: "+ Arrays.toString(items_stirred));
        System.out.println("Inventory: "+inventory);
    }

    public boolean containsItems(){
        return ! (inventory.get(MAX_STACKS - 1) == ItemStack.EMPTY);
    }

    public boolean isFull(){
        return ! inventory.contains(ItemStack.EMPTY);
    }

    public float getSugarContent(){
        return sugar_content;
    }

    public float [] getFlavors(){
        return flavors;
    }

    public int getColor(){
        if(getVolume() == 0) return MaterialColor.WATER.color;

        int color = UtilList.mixColors(10-items_stirred[0], MaterialColor.WATER.color, FermentableMap.Fermentables.get(getInventory().get(0).getItem()).getColor());
        for(int i = 1; i < getInventory().size(); i++){
            color = UtilList.mixColors(1, color, UtilList.mixColors(10-items_stirred[i], MaterialColor.WATER.color, FermentableMap.Fermentables.get(getInventory().get(i).getItem()).getColor()));
        }
        return color;
    }

    public int [] getItemsStirred(){
        return items_stirred;
    }

    // read
    @Override
    public void fromTag(BlockState blockState, CompoundTag compoundTag){
        super.fromTag(blockState, compoundTag);
        volume = compoundTag.getFloat("volume");
        sugar_content = compoundTag.getFloat("sugar_content");

        // code 1: doesn't work
        flavors = intArrayToFloat(compoundTag.getIntArray("flavors"));
        items_stirred = compoundTag.getIntArray("items_stirred");

        System.out.println("fromTag got: "+ Arrays.toString(items_stirred));

        Inventories.fromTag(compoundTag, inventory);
    }

    // write
    @Override
    public CompoundTag toTag(CompoundTag compoundTag){
        super.toTag(compoundTag);
        compoundTag.putFloat("volume", volume);
        compoundTag.putFloat("sugar_content", sugar_content);

        // code 1: doesn't work
        compoundTag.putIntArray("flavors", floatArrayToInt(flavors));
        compoundTag.putIntArray("items_stirred", items_stirred);

        System.out.println("toTag wrote: "+ Arrays.toString(items_stirred));

        Inventories.toTag(compoundTag, inventory);

        return compoundTag;
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        volume = compoundTag.getFloat("volume");
        sugar_content = compoundTag.getFloat("sugar_content");

        // code 1: doesn't work
        flavors = intArrayToFloat(compoundTag.getIntArray("flavors"));
        items_stirred = compoundTag.getIntArray("items_stirred");

        System.out.println("fromClientTag got: "+ Arrays.toString(items_stirred));
        System.out.println(System.identityHashCode(items_stirred));

        Inventories.fromTag(compoundTag, inventory);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        compoundTag.putFloat("volume", volume);
        compoundTag.putFloat("sugar_content", sugar_content);

        compoundTag.putIntArray("flavors", floatArrayToInt(flavors));
        compoundTag.putIntArray("items_stirred", items_stirred.clone());

        System.out.println("toClientTag wrote: "+ Arrays.toString(items_stirred.clone()));
        System.out.println(System.identityHashCode(items_stirred.clone()));

        Inventories.toTag(compoundTag, inventory);

        return compoundTag;
    }
}
