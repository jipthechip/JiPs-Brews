package com.jipthechip.fermentationmod.Items;

import com.jipthechip.fermentationmod.Blocks.BlockList;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FabricItem extends Item {
    public FabricItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand){
        player.playSound(SoundEvents.BLOCK_WOOL_BREAK, 1.0F, 1.0F);

        Vec3d lookVector = lookVector(player);
        Vec3d cameraPosVec = player.getCameraPosVec(1.0F);

        double totalDistance = 0;

        BlockPos initialBlockPos = vec3dToBlockPos(cameraPosVec);
        BlockPos lastBlockPos = initialBlockPos;

        while(totalDistance <= 10){
            cameraPosVec = cameraPosVec.add(lookVector.multiply(.1));
            totalDistance += .1;
            BlockPos pos = vec3dToBlockPos(cameraPosVec);
            if(!blockPosEqual(pos, lastBlockPos)){
                if(world.getBlockState(pos).getMaterial() != Material.AIR
                        && world.getBlockState(pos).getMaterial() != Material.WATER
                        && world.getBlockState(pos).getMaterial() != Material.LAVA
                        && !blockPosEqual(lastBlockPos, initialBlockPos)){
                    //player.sendMessage(new LiteralText("Found block at "+pos.toString()), false);
                    world.setBlockState(lastBlockPos, BlockList.FABRIC_BLOCK.getDefaultState());
                    break;
                }else{
                    lastBlockPos = pos;
                }
            }
        }

        return TypedActionResult.success(player.getStackInHand(hand));
    }

    // gets a player's look vector
    Vec3d lookVector(PlayerEntity player) {
        return Vec3d.fromPolar(player.getPitch(1.0F), player.getYaw(1.0F));
    }

    // converts a Vec3d to a BlockPos
    BlockPos vec3dToBlockPos(Vec3d vec){
        return new BlockPos((int)Math.floor(vec.getX()), (int)Math.floor(vec.getY()), (int)Math.floor(vec.getZ()));
    }

    // checks if 2 BlockPos objects are the same
    boolean blockPosEqual(BlockPos pos1, BlockPos pos2){
        return (pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY() && pos1.getZ() == pos2.getZ());
    }
}
