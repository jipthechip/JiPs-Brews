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

public class MasherRodBlock extends Block {

    //region VoxelShapes
    private final VoxelShape SHAPE = Stream.of(
            Block.createCuboidShape(7, 0, 7, 9, 15, 9),
            Block.createCuboidShape(7, 6, 3, 8, 8, 7),
            Block.createCuboidShape(8, 6, 9, 9, 8, 13),
            Block.createCuboidShape(3, 6, 8, 7, 8, 9),
            Block.createCuboidShape(9, 6, 7, 13, 8, 8),
            Block.createCuboidShape(8, 11, 3, 9, 13, 7),
            Block.createCuboidShape(7, 11, 9, 8, 13, 13),
            Block.createCuboidShape(3, 11, 7, 7, 13, 8),
            Block.createCuboidShape(9, 11, 8, 13, 13, 9)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();
    //endregion

    public static final DirectionProperty OUTSIDE_ROD_FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

    public MasherRodBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getStateManager().getDefaultState()
                .with(OUTSIDE_ROD_FACING, Direction.NORTH)
        );
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context){
        return this.getDefaultState()
                .with(OUTSIDE_ROD_FACING, context.getPlayerFacing());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager){
        stateManager.add(OUTSIDE_ROD_FACING);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context){
        return SHAPE;
    }

}
