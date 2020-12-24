package com.jipthechip.fermentationmod.Blocks;

import com.jipthechip.fermentationmod.Entities.MasherBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potions;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class MasherBasinBlock extends Block implements BlockEntityProvider {

    //region VoxelShapes
    public final VoxelShape SHAPE = Stream.of(
            Block.createCuboidShape(1, 1, 0, 15, 16, 1),
            Block.createCuboidShape(1, 0, 1, 15, 1, 15),
            Block.createCuboidShape(15, 1, 1, 16, 16, 15),
            Block.createCuboidShape(0, 1, 1, 1, 16, 15),
            Block.createCuboidShape(1, 1, 15, 15, 16, 16)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();
    //endregion

    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
    public static final DirectionProperty ROD_FACING = DirectionProperty.of("rod_facing", Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
    public static final BooleanProperty CONTAINS_ROD = BooleanProperty.of("contains_rod");
    public static final BooleanProperty CONTAINS_WATER = BooleanProperty.of("contains_water");

    public MasherBasinBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(CONTAINS_ROD, false)
                .with(ROD_FACING, Direction.NORTH)
                .with(CONTAINS_WATER, false)
        );
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context){
        return this.getDefaultState()
                .with(FACING, context.getPlayerFacing().getOpposite())
                .with(CONTAINS_ROD, false)
                .with(ROD_FACING, context.getPlayerFacing().getOpposite())
                .with(CONTAINS_WATER, false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager){
        stateManager.add(FACING)
                    .add(CONTAINS_ROD)
                    .add(ROD_FACING)
                    .add(CONTAINS_WATER);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new MasherBlockEntity();
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        BlockPos up = pos.up();
        if(world.getBlockState(up).getBlock() == BlockList.MASHER_CRANK){
            world.breakBlock(up, true, player); // break crank block
        }
        MasherBlockEntity masherBlockEntity = (MasherBlockEntity) world.getBlockEntity(pos);
        assert masherBlockEntity != null;

        if(masherBlockEntity.containsItems()) {
            ItemStack itemStack = masherBlockEntity.removeItem();
            while(itemStack != ItemStack.EMPTY) {
                world.spawnEntity(new ItemEntity(world, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, itemStack));
                itemStack = masherBlockEntity.removeItem();
            }
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context){
        return SHAPE;
    }
}
