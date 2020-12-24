package com.jipthechip.fermentationmod.Events;

import com.jipthechip.fermentationmod.Blocks.BlockList;
import com.jipthechip.fermentationmod.Entities.CarboyBlockEntity;
import com.jipthechip.fermentationmod.Entities.MasherBlockEntity;
import com.jipthechip.fermentationmod.Entities.MasonJarBlockEntity;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;


public class EventList {

    // Player Right Click on Block
    public static final UseBlockCallback useBlockCallback =
        ((playerEntity, world, hand, blockHitResult) -> {
            if (hand.equals(Hand.OFF_HAND)) return ActionResult.PASS; // prevent event from continuing for off hand

            BlockPos pos = blockHitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if(blockEntity != null){
                // Masher Entity Event
                if (blockEntity instanceof MasherBlockEntity) {
                    return MasherBasinHandler.handle(playerEntity, world, hand, blockHitResult, state, pos, blockEntity);
                }
                else if(blockEntity instanceof CarboyBlockEntity){
                    return CarboyHandler.handle(playerEntity, world, hand, blockHitResult, state, pos, blockEntity);
                }
                else if(blockEntity instanceof MasonJarBlockEntity){
                    return MasonJarHandler.handle(playerEntity, world, hand, blockHitResult, state, pos, blockEntity);
                }
            }
            // turn crank
            else if(block == BlockList.MASHER_CRANK){
                return MasherCrankHandler.handle(playerEntity, world, hand, blockHitResult, state, pos);
            }
            // turn rod (outside of basin)
            else if(block == BlockList.MASHER_ROD){
                return MasherRodHandler.handle(playerEntity, world, hand, blockHitResult, state, pos);
            }
            return ActionResult.PASS;
        });

    public static void registerEvents(){
        UseBlockCallback.EVENT.register(useBlockCallback);
    }
}
