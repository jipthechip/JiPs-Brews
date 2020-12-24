package com.jipthechip.fermentationmod.Items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;

public class BeerMugFilled extends AlcoholicDrink{
    public BeerMugFilled(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }
}
