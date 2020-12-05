package com.jipthechip.fermentationmod.Events;

import com.jipthechip.fermentationmod.Blocks.BlockList;
import com.jipthechip.fermentationmod.Entities.MasherBlockEntity;
import com.jipthechip.fermentationmod.Items.ItemList;
import com.jipthechip.fermentationmod.Items.MashBucket;
import com.jipthechip.fermentationmod.Models.FermentableMap;
import com.jipthechip.fermentationmod.Sounds.SoundList;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.jipthechip.fermentationmod.Blocks.MasherBasinBlock.CONTAINS_ROD;
import static com.jipthechip.fermentationmod.Blocks.MasherBasinBlock.CONTAINS_WATER;

public class MasherBasinHandler {

    public static ActionResult handle(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult blockHitResult, BlockState state, BlockPos pos, BlockEntity blockEntity){
        MasherBlockEntity masherBlockEntity = (MasherBlockEntity) blockEntity;
        ArrayList<ItemStack> hands = (ArrayList<ItemStack>) playerEntity.getItemsHand();
        ItemStack mainHand = hands.get(0);

        if (mainHand == ItemStack.EMPTY) {
            System.out.println("Flavors: "+ Arrays.toString(masherBlockEntity.getFlavors()));
            System.out.println("Sugar Content: "+masherBlockEntity.getSugarContent());
            System.out.println("Volume: "+masherBlockEntity.getVolume());
            System.out.println("Items Stirred: "+ Arrays.toString(masherBlockEntity.getItemsStirred()));
            System.out.println("Inventory: "+masherBlockEntity.getInventory());

            if(playerEntity.isSneaking() && state.get(CONTAINS_ROD) && !state.get(CONTAINS_WATER)){
                world.setBlockState(pos, state.with(CONTAINS_ROD, false));
                playerEntity.setStackInHand(hand, new ItemStack(BlockList.MASHER_ROD.asItem()));
            }

            ItemStack itemStack = masherBlockEntity.removeItem();
            if(itemStack == ItemStack.EMPTY) return ActionResult.PASS;
            world.spawnEntity(new ItemEntity(world, (double)pos.getX() + 0.5, (double)pos.getY()+0.5, (double)pos.getZ()+ 0.5, itemStack));
            masherBlockEntity.markDirty();
            if(!world.isClient()) {
                masherBlockEntity.markDirty();
                masherBlockEntity.sync();
            }
            return ActionResult.SUCCESS;
        }
        // fill basin with water
        else if (mainHand.getItem() == Items.WATER_BUCKET && !state.get(CONTAINS_WATER)) {
            if(!playerEntity.isCreative()) playerEntity.setStackInHand(hand, new ItemStack(Items.BUCKET));
            world.setBlockState(pos, state.with(CONTAINS_WATER, true));
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1f, 1f);
            return ActionResult.SUCCESS;

        // empty water from basin
        } else if (mainHand.getItem() == Items.BUCKET && state.get(CONTAINS_WATER)) {
            if(masherBlockEntity.getStirProgress()){ // all items stirred, fill bucket with mash
                System.out.println(Arrays.toString(masherBlockEntity.getItemsStirred()));
                ItemStack itemStack = new ItemStack(ItemList.MASH_BUCKET);
                MashBucket.setNBT(itemStack, masherBlockEntity.getSugarContent(), masherBlockEntity.getFlavors(), masherBlockEntity.getColor());
                playerEntity.setStackInHand(hand, itemStack);
            } else if(!playerEntity.isCreative()) {
                playerEntity.setStackInHand(hand, new ItemStack(Items.WATER_BUCKET)); // fill bucket with water
            }

            List<ItemStack> inventory = masherBlockEntity.getInventory();
            System.out.println("Flavors: "+Arrays.toString(masherBlockEntity.getFlavors()));
            System.out.println("Sugar Content: "+masherBlockEntity.getSugarContent());
            System.out.println("Volume: "+masherBlockEntity.getVolume());
            System.out.println("Items Stirred: "+ Arrays.toString(masherBlockEntity.getItemsStirred()));
            System.out.println("Inventory: "+inventory);

            for(int i = inventory.size() - 1; i >= 0 ; i--){ // remove any unstirred items from basin and spawn them in the world
                Random random = new Random();
                ItemStack itemStack = masherBlockEntity.removeItem();
                if(itemStack == ItemStack.EMPTY) break;
                world.spawnEntity(new ItemEntity(world, (double) pos.getX() + random.nextDouble(), (double) pos.getY() + 2, (double) pos.getZ() + random.nextDouble(), itemStack));
            }
            world.setBlockState(pos, state.with(CONTAINS_WATER, false));
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1f, 1f);
            masherBlockEntity.resetData();

            if(!world.isClient()) {
                masherBlockEntity.markDirty();
                masherBlockEntity.sync();
            }
            System.out.println();
            return ActionResult.SUCCESS;
        }
        // insert rod into basin (haha)
        else if (mainHand.getItem() == BlockList.MASHER_ROD.asItem()) {
            if(!playerEntity.isCreative()) playerEntity.setStackInHand(hand, ItemStack.EMPTY);
            world.setBlockState(pos, state.with(CONTAINS_ROD, true));
            return ActionResult.SUCCESS;
        }
        // install crank
        else if (mainHand.getItem() == BlockList.MASHER_CRANK.asItem()) {
            if(!playerEntity.isCreative()) playerEntity.setStackInHand(hand, ItemStack.EMPTY);

            if(state.get(CONTAINS_ROD)) {
                world.setBlockState(pos.up(), BlockList.MASHER_CRANK.getPlacementState(new ItemPlacementContext(playerEntity, hand, mainHand, blockHitResult)));
                return ActionResult.SUCCESS;
            }
        }
        // add ingredient
        else if (FermentableMap.Fermentables.containsKey(mainHand.getItem()) && state.get(CONTAINS_WATER)) {
            // failed to add item
            if(! masherBlockEntity.addItem(mainHand.split(1))){
                mainHand.setCount(mainHand.getCount()+1);
                return ActionResult.PASS;
            }
            playerEntity.setStackInHand(hand, mainHand.copy());
            Random random = new Random();
            world.playSound(null, pos, SoundList.ADD_TO_MASHER_EVENT, SoundCategory.BLOCKS, 1f, 0.5f * random.nextFloat()+0.5f);
            if(!world.isClient()) {
                masherBlockEntity.markDirty();
                masherBlockEntity.sync();
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
