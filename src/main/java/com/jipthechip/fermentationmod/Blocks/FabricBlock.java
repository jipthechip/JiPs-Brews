package com.jipthechip.fermentationmod.Blocks;

import com.jipthechip.fermentationmod.Entities.TestBlockEntity;
import com.jipthechip.fermentationmod.Utils.UtilList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class FabricBlock extends Block implements BlockEntityProvider {

    //public static final BooleanProperty TOGGLED = BooleanProperty.of("toggled");

    public FabricBlock(Settings settings) {
        super(settings);
        //setDefaultState(getStateManager().getDefaultState().with(TOGGLED, false));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new TestBlockEntity();
    }


    /*
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager){
        stateManager.add(TOGGLED);
    }
     */
    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity playerEntity) {
        Random random = new Random();
        UtilList.sendParticlePacket(world, pos, random.nextDouble(), random.nextDouble(), random.nextDouble(), new Identifier("angry_villager"));
    }
}
