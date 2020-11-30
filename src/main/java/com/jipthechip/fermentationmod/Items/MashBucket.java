package com.jipthechip.fermentationmod.Items;

import com.jipthechip.fermentationmod.Utils.UtilList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class MashBucket extends Item{
    public MashBucket(Settings settings) {
        super(settings);
    }

    public static ItemStack setNBT(ItemStack itemStack, float sugarContent, float[] flavors, int color){
        CompoundTag tag;
        if(itemStack.hasTag()) tag = itemStack.getTag();
        else {
            tag = new CompoundTag();
        }
        assert tag != null;
        tag.putFloat("sugar_content", sugarContent);
        tag.putIntArray("flavors", UtilList.floatArrayToInt(flavors));
        tag.putInt("color", color);

        itemStack.setTag(tag);

        return itemStack;
    }

    public static float getSugarContent(ItemStack itemStack){
        CompoundTag tag = itemStack.getTag();
        if(tag == null) return 0;
        return tag.getFloat("sugar_content");
    }

    public static float [] getFlavors(ItemStack itemStack){
        CompoundTag tag = itemStack.getTag();
        if(tag == null) return new float[]{0,0,0,0,0,0,0,0};
        return UtilList.intArrayToFloat(tag.getIntArray("flavors"));
    }

    public static int getColor(ItemStack itemStack){
        CompoundTag tag = itemStack.getTag();
        if(tag == null) return 0xFFFFFFFF;
        return tag.getInt("color");
    }

}
