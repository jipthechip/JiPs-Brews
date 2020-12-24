package com.jipthechip.fermentationmod.Items;

import com.jipthechip.fermentationmod.Utils.EntityAlcoholStats;
import com.jipthechip.fermentationmod.Utils.UtilList;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class AlcoholicDrink extends PotionItem {
    public AlcoholicDrink(Settings settings) {
        super(settings);
    }

    public static ItemStack setNBT(ItemStack itemStack, float alcohol_content, float sugarContent, float[] flavors, int color){
        CompoundTag tag;
        if(itemStack.hasTag()) tag = itemStack.getTag();
        else {
            tag = new CompoundTag();
        }
        assert tag != null;
        tag.putFloat("alcohol_content", alcohol_content);
        tag.putFloat("sugar_content", sugarContent);
        tag.putIntArray("flavors", UtilList.floatArrayToInt(flavors));
        tag.putInt("color", color);

        itemStack.setTag(tag);

        return itemStack;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        System.out.println("called finishusing");
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }

        if (!world.isClient) {
            List<StatusEffectInstance> list = PotionUtil.getPotionEffects(stack);
            Iterator var6 = list.iterator();

            while(var6.hasNext()) {
                StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var6.next();
                if (statusEffectInstance.getEffectType().isInstant()) {
                    statusEffectInstance.getEffectType().applyInstantEffect(playerEntity, playerEntity, user, statusEffectInstance.getAmplifier(), 1.0D);
                } else {
                    user.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
                }
            }
        }

        ItemStack initialStack = stack.copy();
        if (playerEntity != null) {
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!playerEntity.abilities.creativeMode) {
                stack.decrement(1);
            }
        }

        if (playerEntity == null || !playerEntity.abilities.creativeMode) {
            System.out.println("first if");
            System.out.println("registry name: "+stack.getTranslationKey());
            if (stack.isEmpty()) {
                if(initialStack.getItem() == ItemList.BEER_MUG_FILLED){
                    // increase BAC of user by 0.02 for every 0.05 alcohol percentage
                    float currentBAC = ((EntityAlcoholStats) user).getBAC();
                    ((EntityAlcoholStats) user).setBAC(currentBAC + (getAlcoholContent(stack)/(0.05f))*(0.02f));
                    return new ItemStack(ItemList.BEER_MUG);
                }
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (playerEntity != null) {
                System.out.println("registry name: "+getTranslationKey(stack));
                if(initialStack.getItem() == ItemList.BEER_MUG_FILLED){
                    // increase BAC of user by 0.02 for every 0.05 alcohol percentage
                    float currentBAC = ((EntityAlcoholStats) user).getBAC();
                    ((EntityAlcoholStats) user).setBAC(currentBAC + (getAlcoholContent(stack)/(0.05f))*(0.02f));
                    playerEntity.inventory.insertStack(new ItemStack(ItemList.BEER_MUG));
                }else{
                    playerEntity.inventory.insertStack(new ItemStack(Items.GLASS_BOTTLE));
                }
            }
        }

        return stack;
    }

    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks){
    }

    public float getAlcoholContent(ItemStack stack){
        return stack.getTag().getFloat("alcohol_content");
    }
}
