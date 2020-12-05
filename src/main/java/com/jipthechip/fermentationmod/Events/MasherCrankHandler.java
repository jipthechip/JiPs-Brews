package com.jipthechip.fermentationmod.Events;

import com.jipthechip.fermentationmod.Blocks.BlockList;
import com.jipthechip.fermentationmod.Entities.MasherBlockEntity;
import com.jipthechip.fermentationmod.Sounds.SoundList;
import com.jipthechip.fermentationmod.Utils.UtilList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

import static com.jipthechip.fermentationmod.Blocks.MasherBasinBlock.*;
import static com.jipthechip.fermentationmod.Blocks.MasherCrankBlock.CRANK_FACING;

public class MasherCrankHandler {

    public static ActionResult handle(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult blockHitResult, BlockState state, BlockPos pos){
        world.setBlockState(pos, state.with(CRANK_FACING, state.get(CRANK_FACING).rotateYClockwise()));

        BlockState belowState = world.getBlockState(pos.down());
        Block belowBlock = belowState.getBlock();
        if(belowBlock == BlockList.MASHER_BASIN){
            if(belowState.get(CONTAINS_ROD)){
                world.setBlockState(pos.down(), belowState.with(ROD_FACING, state.get(CRANK_FACING))); // turn rod with crank
                MasherBlockEntity masherBlockEntity = (MasherBlockEntity) world.getBlockEntity(pos.down());
                assert masherBlockEntity != null;
                if(belowState.get(CONTAINS_WATER)){
                    Random random = new Random();
                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 0.5f, 0.5f * random.nextFloat() + 0.5f);
                }
                if(masherBlockEntity.containsItems()) {
                    if (!masherBlockEntity.stir()) {
                        Random random = new Random();
                        for (int i = 0; i < 20; i++)
                            UtilList.sendParticlePacket(world, pos, random.nextDouble(), 0, random.nextDouble(), new Identifier("splash"));
                    }
                }
                if(!world.isClient()) {
                    masherBlockEntity.markDirty();
                    masherBlockEntity.sync();
                }
            }
        }
        return ActionResult.SUCCESS;
    }
}
