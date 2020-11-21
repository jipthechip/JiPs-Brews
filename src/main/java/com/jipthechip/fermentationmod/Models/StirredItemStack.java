package com.jipthechip.fermentationmod.Models;

import net.minecraft.item.ItemStack;

public class StirredItemStack {

    public static final StirredItemStack EMPTY = new StirredItemStack(ItemStack.EMPTY);

    private ItemStack itemStack;
    private boolean stirred;

    public StirredItemStack(ItemStack itemStack){
        this.itemStack = itemStack;
        this.stirred = false;
    }

    public void setStirred(){
        this.stirred = true;
    }

    public ItemStack getItemStack(){
        return this.itemStack;
    }
}
