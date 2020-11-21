package com.jipthechip.fermentationmod.Blocks;

import com.jipthechip.fermentationmod.Entities.TestBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
}
