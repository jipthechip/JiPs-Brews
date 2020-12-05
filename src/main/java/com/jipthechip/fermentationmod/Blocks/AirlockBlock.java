package com.jipthechip.fermentationmod.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.stream.Stream;

public class AirlockBlock extends Block {

    //region VoxelShapes
    private final VoxelShape SHAPE_N = Stream.of(
            Block.createCuboidShape(7, 0, 7, 9, 1, 9),
            Block.createCuboidShape(9, 2, 7, 10, 3, 9),
            Block.createCuboidShape(10, 3, 7, 11, 7, 9),
            Block.createCuboidShape(7, 4, 7, 9, 6, 9),
            Block.createCuboidShape(4, 4, 7, 6, 6, 9),
            Block.createCuboidShape(4, 6, 7, 5, 7, 9),
            Block.createCuboidShape(4, 7, 7, 5, 8, 9),
            Block.createCuboidShape(5, 8, 7, 7, 9, 9),
            Block.createCuboidShape(7, 9, 7, 8, 10, 9),
            Block.createCuboidShape(9, 7, 7, 10, 8, 9),
            Block.createCuboidShape(5, 6, 7, 6, 7, 9),
            Block.createCuboidShape(7, 6, 7, 9, 7, 9),
            Block.createCuboidShape(7, 1, 7, 8, 2, 9),
            Block.createCuboidShape(8, 1, 7, 9, 2, 9),
            Block.createCuboidShape(8, 2, 7, 9, 3, 9),
            Block.createCuboidShape(6, 3, 7, 7, 4, 9)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();
    private final VoxelShape SHAPE_S = Stream.of(
            Block.createCuboidShape(7, 0, 7, 9, 1, 9),
            Block.createCuboidShape(6, 2, 7, 7, 3, 9),
            Block.createCuboidShape(5, 3, 7, 6, 7, 9),
            Block.createCuboidShape(7, 4, 7, 9, 6, 9),
            Block.createCuboidShape(10, 4, 7, 12, 6, 9),
            Block.createCuboidShape(11, 6, 7, 12, 7, 9),
            Block.createCuboidShape(11, 7, 7, 12, 8, 9),
            Block.createCuboidShape(9, 8, 7, 11, 9, 9),
            Block.createCuboidShape(8, 9, 7, 9, 10, 9),
            Block.createCuboidShape(6, 7, 7, 7, 8, 9),
            Block.createCuboidShape(10, 6, 7, 11, 7, 9),
            Block.createCuboidShape(7, 6, 7, 9, 7, 9),
            Block.createCuboidShape(8, 1, 7, 9, 2, 9),
            Block.createCuboidShape(7, 1, 7, 8, 2, 9),
            Block.createCuboidShape(7, 2, 7, 8, 3, 9),
            Block.createCuboidShape(9, 3, 7, 10, 4, 9)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();
    private final VoxelShape SHAPE_E = Stream.of(
            Block.createCuboidShape(7, 0, 7, 9, 1, 9),
            Block.createCuboidShape(7, 2, 9, 9, 3, 10),
            Block.createCuboidShape(7, 3, 10, 9, 7, 11),
            Block.createCuboidShape(7, 4, 7, 9, 6, 9),
            Block.createCuboidShape(7, 4, 4, 9, 6, 6),
            Block.createCuboidShape(7, 6, 4, 9, 7, 5),
            Block.createCuboidShape(7, 7, 4, 9, 8, 5),
            Block.createCuboidShape(7, 8, 5, 9, 9, 7),
            Block.createCuboidShape(7, 9, 7, 9, 10, 8),
            Block.createCuboidShape(7, 7, 9, 9, 8, 10),
            Block.createCuboidShape(7, 6, 5, 9, 7, 6),
            Block.createCuboidShape(7, 6, 7, 9, 7, 9),
            Block.createCuboidShape(7, 1, 7, 9, 2, 8),
            Block.createCuboidShape(7, 1, 8, 9, 2, 9),
            Block.createCuboidShape(7, 2, 8, 9, 3, 9),
            Block.createCuboidShape(7, 3, 6, 9, 4, 7)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();
    private final VoxelShape SHAPE_W = Stream.of(
            Block.createCuboidShape(7, 0, 7, 9, 1, 9),
            Block.createCuboidShape(7, 2, 6, 9, 3, 7),
            Block.createCuboidShape(7, 3, 5, 9, 7, 6),
            Block.createCuboidShape(7, 4, 7, 9, 6, 9),
            Block.createCuboidShape(7, 4, 10, 9, 6, 12),
            Block.createCuboidShape(7, 6, 11, 9, 7, 12),
            Block.createCuboidShape(7, 7, 11, 9, 8, 12),
            Block.createCuboidShape(7, 8, 9, 9, 9, 11),
            Block.createCuboidShape(7, 9, 8, 9, 10, 9),
            Block.createCuboidShape(7, 7, 6, 9, 8, 7),
            Block.createCuboidShape(7, 6, 10, 9, 7, 11),
            Block.createCuboidShape(7, 6, 7, 9, 7, 9),
            Block.createCuboidShape(7, 1, 8, 9, 2, 9),
            Block.createCuboidShape(7, 1, 7, 9, 2, 8),
            Block.createCuboidShape(7, 2, 7, 9, 3, 8),
            Block.createCuboidShape(7, 3, 9, 9, 4, 10)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();
    //endregion

    public static final DirectionProperty AIRLOCK_FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

    public AirlockBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getStateManager().getDefaultState()
                .with(AIRLOCK_FACING, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context){
        return this.getDefaultState()
                .with(AIRLOCK_FACING, context.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager){
        stateManager.add(AIRLOCK_FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context){
        switch(state.get(AIRLOCK_FACING)){
            case NORTH: return SHAPE_N;
            case SOUTH: return SHAPE_S;
            case EAST: return SHAPE_E;
            default: return SHAPE_W;
        }
    }
}
