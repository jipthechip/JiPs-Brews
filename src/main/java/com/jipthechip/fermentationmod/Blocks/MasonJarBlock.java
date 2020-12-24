package com.jipthechip.fermentationmod.Blocks;

//import com.jipthechip.fermentationmod.Entities.MasonJarBlockEntity;
import com.jipthechip.fermentationmod.Entities.MasonJarBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class MasonJarBlock extends Block implements BlockEntityProvider {

    //region VoxelShapes
    private static final VoxelShape SHAPE = Stream.of(
            Block.createCuboidShape(5, 1, 6, 6, 6, 10),
            Block.createCuboidShape(6, 6, 6, 6, 7, 10),
            Block.createCuboidShape(6, 6, 6, 10, 7, 6),
            Block.createCuboidShape(6, 6, 10, 10, 7, 10),
            Block.createCuboidShape(10, 6, 6, 10, 7, 10),
            Block.createCuboidShape(10, 1, 6, 11, 6, 10),
            Block.createCuboidShape(6, 1, 10, 10, 6, 11),
            Block.createCuboidShape(6, 0, 6, 10, 1, 10),
            Block.createCuboidShape(6, 1, 5, 10, 6, 6),
            Block.createCuboidShape(10, 0, 6, 11, 1, 10),
            Block.createCuboidShape(5, 0, 6, 6, 1, 10),
            Block.createCuboidShape(6, 0, 5, 10, 1, 6),
            Block.createCuboidShape(6, 0, 10, 10, 1, 11),
            Block.createCuboidShape(6, 7, 10, 10, 8, 11),
            Block.createCuboidShape(6, 7, 5, 10, 8, 6),
            Block.createCuboidShape(10, 7, 6, 11, 8, 10),
            Block.createCuboidShape(5, 7, 6, 6, 8, 10)
            ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();
    //endregion
    
    public static final BooleanProperty JAR_CONTAINS_WATER = BooleanProperty.of("contains_water");
    public static final BooleanProperty JAR_IS_COVERED = BooleanProperty.of("covered");
    
    public MasonJarBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getStateManager().getDefaultState()
                .with(JAR_CONTAINS_WATER, false)
                .with(JAR_IS_COVERED, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context){
        return this.getDefaultState()
                .with(JAR_CONTAINS_WATER, false)
                .with(JAR_IS_COVERED, false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager){
        stateManager.add(JAR_CONTAINS_WATER);
        stateManager.add(JAR_IS_COVERED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context){
        if(state.get(JAR_IS_COVERED)){
            return VoxelShapes.combineAndSimplify(SHAPE, Block.createCuboidShape(6, 8, 6, 10, 8, 10), BooleanBiFunction.OR);
        }
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new MasonJarBlockEntity();
    }
}
