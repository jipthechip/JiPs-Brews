package com.jipthechip.fermentationmod.Events;

import com.jipthechip.fermentationmod.Entities.MasherBlockEntity;
import com.jipthechip.fermentationmod.Entities.MasonJarBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

import static com.jipthechip.fermentationmod.Blocks.MasonJarBlock.JAR_CONTAINS_WATER;
import static com.jipthechip.fermentationmod.Blocks.MasonJarBlock.JAR_IS_COVERED;

public class MasonJarHandler {

    public static ActionResult handle(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult blockHitResult, BlockState state, BlockPos pos, BlockEntity blockEntity){

        MasonJarBlockEntity masonJarBlockEntity = (MasonJarBlockEntity) blockEntity;
        ArrayList<ItemStack> hands = (ArrayList<ItemStack>) playerEntity.getItemsHand();
        ItemStack mainHand = hands.get(0);

        if(mainHand.getItem() == Items.PAPER && !state.get(JAR_IS_COVERED)){
            mainHand.split(1);
            playerEntity.setStackInHand(hand, mainHand.copy());
            world.setBlockState(pos, state.with(JAR_IS_COVERED, true));
            return ActionResult.SUCCESS;
        }else if(mainHand == ItemStack.EMPTY && state.get(JAR_IS_COVERED) && playerEntity.isSneaking()){
            System.out.println("removing paper");
            playerEntity.setStackInHand(hand, new ItemStack(Items.PAPER));
            world.setBlockState(pos, state.with(JAR_IS_COVERED, false));
            return ActionResult.SUCCESS;
        }else if(mainHand.getItem() == Items.POTION && !state.get(JAR_CONTAINS_WATER)){
            CompoundTag tag = mainHand.getTag();
            assert tag != null;
            if (tag.getString("Potion").equals("minecraft:water")){
                playerEntity.setStackInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
                world.setBlockState(pos, state.with(JAR_CONTAINS_WATER, true));
                return ActionResult.SUCCESS;
            }
        }else if(mainHand.getItem() == Items.SUGAR && state.get(JAR_CONTAINS_WATER) && ! masonJarBlockEntity.isInProgress() ){
            mainHand.split(1);
            playerEntity.setStackInHand(hand, mainHand.copy());
            masonJarBlockEntity.setProductionType(1);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
