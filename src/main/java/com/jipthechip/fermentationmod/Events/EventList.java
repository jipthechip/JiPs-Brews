package com.jipthechip.fermentationmod.Events;

import com.jipthechip.fermentationmod.Blocks.BlockList;
import com.jipthechip.fermentationmod.Entities.MasherBlockEntity;
import com.jipthechip.fermentationmod.Entities.TestBlockEntity;
import com.jipthechip.fermentationmod.Models.FermentableMap;
import com.jipthechip.fermentationmod.Utils.UtilList;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.jipthechip.fermentationmod.Blocks.MasherBasinBlock.*;
import static com.jipthechip.fermentationmod.Blocks.MasherCrankBlock.CRANK_FACING;
import static com.jipthechip.fermentationmod.Blocks.MasherRodBlock.OUTSIDE_ROD_FACING;

public class EventList {

    // Player Right Click on Block
    public static final UseBlockCallback useBlockCallback =
        ((playerEntity, world, hand, blockHitResult) -> {
            //playerEntity.sendMessage(new LiteralText("Right Clicked On "+world.getBlockState(blockHitResult.getBlockPos()).getBlock().getClass().toString()), false);
            if (hand.equals(Hand.OFF_HAND)) return ActionResult.PASS;
            BlockPos pos = blockHitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if(blockEntity != null){
                if (blockEntity instanceof TestBlockEntity) {
                    TestBlockEntity testBlockEntity = (TestBlockEntity) blockEntity;
                    testBlockEntity.toggleOn();
                    testBlockEntity.markDirty();
                    //testBlockEntity.sync();

                    System.out.println("Pos: " + testBlockEntity.getPos());
                    System.out.println("hand: " + hand);
                    System.out.println("testBlockEntity set to " + testBlockEntity.isOn());

                    return ActionResult.SUCCESS;
                }
                // Masher Entity Event
                else if (blockEntity instanceof MasherBlockEntity) {
                    MasherBlockEntity masherBlockEntity = (MasherBlockEntity) blockEntity;
                    ArrayList<ItemStack> hands = (ArrayList<ItemStack>) playerEntity.getItemsHand();
                    ItemStack mainHand = hands.get(0);

                    if (mainHand == ItemStack.EMPTY) {
                        ItemStack itemStack = masherBlockEntity.removeItem();
                        if(itemStack == ItemStack.EMPTY) return ActionResult.PASS;
                        world.spawnEntity(new ItemEntity(world, (double)pos.getX() + 0.5, (double)pos.getY()+0.5, (double)pos.getZ()+ 0.5, itemStack));
                        masherBlockEntity.markDirty();
                        if(!world.isClient()) {
                            masherBlockEntity.markDirty();
                            masherBlockEntity.sync();
                        }
                        return ActionResult.SUCCESS;
                    }else if (mainHand.getItem() == Items.WATER_BUCKET && !state.get(CONTAINS_WATER)) { // fill basin with water (need to cancel water place event)
                        if(!playerEntity.isCreative()) playerEntity.setStackInHand(hand, new ItemStack(Items.BUCKET));
                        world.setBlockState(pos, state.with(CONTAINS_WATER, true));
                        return ActionResult.SUCCESS;
                    } else if (mainHand.getItem() == Items.BUCKET && state.get(CONTAINS_WATER)) { // empty water from basin (need to cancel water pick up event)
                        if(!playerEntity.isCreative()) playerEntity.setStackInHand(hand, new ItemStack(Items.WATER_BUCKET));

                        List<ItemStack> inventory = masherBlockEntity.getInventory();
                        for(int i = inventory.size() - 1; i >= 0 ; i--){
                            if(inventory.get(i) == ItemStack.EMPTY) break;
                            world.spawnEntity(new ItemEntity(world, (double)pos.getX() + 0.5, (double)pos.getY()+1.5, (double)pos.getZ()+ 0.5, inventory.get(i)));
                            masherBlockEntity.removeItem();
                        }
                        masherBlockEntity.resetStirProgress();
                        if(!world.isClient()) {
                            masherBlockEntity.markDirty();
                            masherBlockEntity.sync();
                        }

                        world.setBlockState(pos, state.with(CONTAINS_WATER, false));
                        return ActionResult.SUCCESS;
                    } else if (mainHand.getItem() == BlockList.MASHER_ROD.asItem()) { // insert rod into basin (haha)
                        if(!playerEntity.isCreative()) playerEntity.setStackInHand(hand, ItemStack.EMPTY);
                        world.setBlockState(pos, state.with(CONTAINS_ROD, true));
                        return ActionResult.SUCCESS;

                    } else if (mainHand.getItem() == BlockList.MASHER_CRANK.asItem()) {
                        if(!playerEntity.isCreative()) playerEntity.setStackInHand(hand, ItemStack.EMPTY);
                        world.setBlockState(pos.up(), BlockList.MASHER_CRANK.getPlacementState(new ItemPlacementContext(playerEntity, hand, mainHand, blockHitResult)));
                        return ActionResult.SUCCESS;
                    }else if (FermentableMap.Fermentables.containsKey(mainHand.getItem()) && state.get(CONTAINS_WATER)) { // add ingredient
                        if(! masherBlockEntity.addItem(mainHand.split(1))){
                            mainHand.setCount(mainHand.getCount()+1);
                            return ActionResult.PASS;
                        }
                        if(!world.isClient()) {
                            masherBlockEntity.markDirty();
                            masherBlockEntity.sync();
                        }
                        return ActionResult.SUCCESS;
                    }
                }
            }else if(block == BlockList.MASHER_CRANK){
                world.setBlockState(pos, state.with(CRANK_FACING, state.get(CRANK_FACING).rotateYClockwise())); // turn crank

                BlockState belowState = world.getBlockState(pos.down());
                Block belowBlock = belowState.getBlock();
                if(belowBlock == BlockList.MASHER_BASIN){
                    if(belowState.get(CONTAINS_ROD)){
                        world.setBlockState(pos.down(), belowState.with(ROD_FACING, state.get(CRANK_FACING))); // set rod to turn with crank
                        MasherBlockEntity masherBlockEntity = (MasherBlockEntity) world.getBlockEntity(pos.down());
                        assert masherBlockEntity != null;
                        if(masherBlockEntity.containsItems()) if(masherBlockEntity.stir() && masherBlockEntity.getStirProgress() == 10) {
                            Random random = new Random();
                            for(int i = 0; i < 20; i ++) UtilList.sendParticlePacket(world, pos, random.nextDouble(), 0, random.nextDouble(), new Identifier("splash"));
                        }
                        if(!world.isClient()) {
                            masherBlockEntity.markDirty();
                            masherBlockEntity.sync();
                        }
                    }
                }
                return ActionResult.SUCCESS;
            }else if(block == BlockList.MASHER_ROD){
                System.out.println(state.get(OUTSIDE_ROD_FACING));
                world.setBlockState(pos, state.with(OUTSIDE_ROD_FACING, state.get(OUTSIDE_ROD_FACING).rotateYClockwise())); // turn rod
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });

    public static void registerEvents(){
        UseBlockCallback.EVENT.register(useBlockCallback);
    }
}
