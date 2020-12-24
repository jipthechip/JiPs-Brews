package com.jipthechip.fermentationmod.Events;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.jipthechip.fermentationmod.Blocks.MasherRodBlock.OUTSIDE_ROD_FACING;

public class MasherRodHandler {

    public static ActionResult handle(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult blockHitResult, BlockState state, BlockPos pos) {
        world.setBlockState(pos, state.with(OUTSIDE_ROD_FACING, state.get(OUTSIDE_ROD_FACING).rotateYClockwise())); // turn rod
        return ActionResult.SUCCESS;
    }
}
