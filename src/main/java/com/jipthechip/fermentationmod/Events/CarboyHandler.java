package com.jipthechip.fermentationmod.Events;

import com.jipthechip.fermentationmod.Blocks.BlockList;
import com.jipthechip.fermentationmod.Entities.CarboyBlockEntity;
import com.jipthechip.fermentationmod.Items.AlcoholicDrink;
import com.jipthechip.fermentationmod.Items.ItemList;
import com.jipthechip.fermentationmod.Items.MashBucket;
import com.jipthechip.fermentationmod.Sounds.SoundList;
import com.jipthechip.fermentationmod.Utils.UtilList;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;

import static com.jipthechip.fermentationmod.Blocks.CarboyBlock.CONTAINS_LIQUID;
import static com.jipthechip.fermentationmod.Blocks.CarboyBlock.HAS_AIRLOCK;

public class CarboyHandler {
    public static ActionResult handle(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult blockHitResult, BlockState state, BlockPos pos, BlockEntity blockEntity){
        CarboyBlockEntity carboyBlockEntity = (CarboyBlockEntity) blockEntity;
        ArrayList<ItemStack> hands = (ArrayList<ItemStack>) playerEntity.getItemsHand();
        ItemStack mainHand = hands.get(0);

        System.out.println("mainhand empty: "+(mainHand == ItemStack.EMPTY));
        System.out.println("has_airlock: "+state.get(HAS_AIRLOCK));
        System.out.println("player sneaking: "+playerEntity.isSneaking());
        if(mainHand.getItem() == ItemList.MASH_BUCKET && !state.get(CONTAINS_LIQUID)){
            CompoundTag tag = mainHand.getTag();
            assert tag != null;
            carboyBlockEntity.addMash(tag);
            world.setBlockState(pos, state.with(CONTAINS_LIQUID, true));
            playerEntity.setStackInHand(hand, new ItemStack(Items.BUCKET));
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1f, 1f);
            return ActionResult.SUCCESS;
        }else if(mainHand.getItem() == Items.BUCKET && state.get(CONTAINS_LIQUID)){
            ItemStack stack = new ItemStack(ItemList.MASH_BUCKET);
            MashBucket.setNBT(stack, carboyBlockEntity.getSugarContent(), carboyBlockEntity.getFlavors(), carboyBlockEntity.getColor());
            playerEntity.setStackInHand(hand, stack);
            carboyBlockEntity.resetData();
            world.setBlockState(pos, state.with(CONTAINS_LIQUID, false));
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1f, 1f);
            return ActionResult.SUCCESS;
        }else if(mainHand.getItem() == BlockList.AIRLOCK.asItem() && !state.get(HAS_AIRLOCK) && !playerEntity.isSneaking()){
            System.out.println("adding airlock");
            mainHand.split(1);
            playerEntity.setStackInHand(hand, mainHand.copy());
            world.setBlockState(pos, state.with(HAS_AIRLOCK, true));
            world.playSound(null, pos, SoundList.CORK_CLOSE_EVENT, SoundCategory.BLOCKS, 1f, 1f);
            return ActionResult.SUCCESS;
        }else if(mainHand == ItemStack.EMPTY && state.get(HAS_AIRLOCK) && playerEntity.isSneaking()){
            System.out.println("removing airlock");
            playerEntity.setStackInHand(hand, new ItemStack(BlockList.AIRLOCK.asItem()));
            world.setBlockState(pos, state.with(HAS_AIRLOCK, false));
            world.playSound(null, pos, SoundList.CORK_OPEN_EVENT, SoundCategory.BLOCKS, 1f, 1f);
            return ActionResult.SUCCESS;
        }else if(mainHand.getItem() == ItemList.YEAST_CULTURE && state.get(CONTAINS_LIQUID) && state.get(HAS_AIRLOCK) && !carboyBlockEntity.isFermenting()){
            CompoundTag tag = mainHand.getTag();
            assert tag != null;
            carboyBlockEntity.startFermentation(tag);
            mainHand.split(1);
            playerEntity.setStackInHand(hand, mainHand.copy());
            return ActionResult.SUCCESS;
        }else if(mainHand.getItem() == ItemList.BEER_MUG && state.get(CONTAINS_LIQUID)){
            mainHand.split(1);
            playerEntity.setStackInHand(hand, mainHand.copy());
            ItemStack filledBeerMug = new ItemStack(ItemList.BEER_MUG_FILLED);
            AlcoholicDrink.setNBT(filledBeerMug, carboyBlockEntity.getAlcoholContent(), carboyBlockEntity.getSugarContent(), carboyBlockEntity.getFlavors(), carboyBlockEntity.getColor());
            if(((ArrayList<ItemStack>) playerEntity.getItemsHand()).get(hand.ordinal()) == ItemStack.EMPTY){
                playerEntity.setStackInHand(hand, filledBeerMug);
            }else{
                playerEntity.giveItemStack(filledBeerMug);
            }
            System.out.println("Alcohol content: "+filledBeerMug.getTag().getFloat("alcohol_content"));
            System.out.println("Sugar content: "+filledBeerMug.getTag().getFloat("sugar_content"));
            System.out.println("Flavors: "+ Arrays.toString(UtilList.intArrayToFloat(filledBeerMug.getTag().getIntArray("flavors"))));
            System.out.println("Color: "+filledBeerMug.getTag().getInt("color"));

        }
        return ActionResult.PASS;
    }
}
