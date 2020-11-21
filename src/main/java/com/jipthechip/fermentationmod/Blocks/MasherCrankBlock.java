package com.jipthechip.fermentationmod.Blocks;


import net.fabricmc.fabric.api.util.BooleanFunction;
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

public class MasherCrankBlock extends Block {

    private final VoxelShape SHAPE_N = Stream.of(
            Block.createCuboidShape(7, 1, 1, 9, 2, 9),
            Block.createCuboidShape(7, 0, 7, 9, 1, 9),
            Block.createCuboidShape(7, 2, 1, 9, 9, 3),
            Block.createCuboidShape(7, 9, 1, 9, 10, 3)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();
    private final VoxelShape SHAPE_S = Stream.of(
            Block.createCuboidShape(7, 1, 7, 9, 2, 15),
            Block.createCuboidShape(7, 0, 7, 9, 1, 9),
            Block.createCuboidShape(7, 2, 13, 9, 9, 15),
            Block.createCuboidShape(7, 9, 13, 9, 10, 15)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();
    private final VoxelShape SHAPE_E = Stream.of(
            Block.createCuboidShape(7, 1, 7, 15, 2, 9),
            Block.createCuboidShape(7, 0, 7, 9, 1, 9),
            Block.createCuboidShape(13, 2, 7, 15, 9, 9),
            Block.createCuboidShape(13, 9, 7, 15, 10, 9)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();
    private final VoxelShape SHAPE_W = Stream.of(
            Block.createCuboidShape(1, 1, 7, 9, 2, 9),
            Block.createCuboidShape(7, 0, 7, 9, 1, 9),
            Block.createCuboidShape(1, 2, 7, 3, 9, 9),
            Block.createCuboidShape(1, 9, 7, 3, 10, 9)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();

    public static final DirectionProperty CRANK_FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

    public MasherCrankBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getStateManager().getDefaultState()
                .with(CRANK_FACING, Direction.NORTH)
        );
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context){
        return this.getDefaultState()
                .with(CRANK_FACING, context.getPlayerFacing().rotateYClockwise());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager){
        stateManager.add(CRANK_FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context){
        switch(state.get(CRANK_FACING)){
            case NORTH:
                return SHAPE_N;
            case SOUTH:
                return SHAPE_S;
            case EAST:
                return SHAPE_E;
            default:
                return SHAPE_W;
        }
    }
}