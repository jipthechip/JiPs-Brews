package com.jipthechip.fermentationmod.Blocks;

import com.jipthechip.fermentationmod.Entities.CarboyBlockEntity;
import com.jipthechip.fermentationmod.Entities.MasherBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
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

import static com.jipthechip.fermentationmod.Blocks.BlockList.AIRLOCK;

public class CarboyBlock extends Block implements BlockEntityProvider {

    //region VoxelShapes
    private final VoxelShape SHAPE_N = Stream.of(
            Block.createCuboidShape(13, 19, 7, 14, 21, 9),
            Block.createCuboidShape(11, 18, 7, 13, 19, 9),
            Block.createCuboidShape(10, 21, 7, 13, 22, 9),
            Block.createCuboidShape(13, 18, 7, 14, 19, 9),
            Block.createCuboidShape(13, 21, 7, 14, 22, 9),
            Block.createCuboidShape(11, 17, 5, 12, 18, 11),
            Block.createCuboidShape(4, 17, 5, 5, 18, 11),
            Block.createCuboidShape(4, 17, 4, 5, 18, 5),
            Block.createCuboidShape(11, 17, 4, 12, 18, 5),
            Block.createCuboidShape(3, 16, 12, 4, 17, 13),
            Block.createCuboidShape(12, 16, 12, 13, 17, 13),
            Block.createCuboidShape(3, 16, 3, 4, 17, 4),
            Block.createCuboidShape(2, 15, 2, 3, 16, 3),
            Block.createCuboidShape(13, 15, 2, 14, 16, 3),
            Block.createCuboidShape(13, 15, 13, 14, 16, 14),
            Block.createCuboidShape(2, 15, 13, 3, 16, 14),
            Block.createCuboidShape(12, 16, 3, 13, 17, 4),
            Block.createCuboidShape(4, 17, 11, 5, 18, 12),
            Block.createCuboidShape(11, 17, 11, 12, 18, 12),
            Block.createCuboidShape(5, 17, 11, 11, 18, 12),
            Block.createCuboidShape(5, 17, 4, 11, 18, 5),
            Block.createCuboidShape(5, 18, 6, 6, 20, 10),
            Block.createCuboidShape(5, 18, 10, 6, 20, 11),
            Block.createCuboidShape(10, 18, 10, 11, 20, 11),
            Block.createCuboidShape(10, 18, 5, 11, 20, 6),
            Block.createCuboidShape(5, 18, 5, 6, 20, 6),
            Block.createCuboidShape(10, 18, 6, 11, 20, 10),
            Block.createCuboidShape(6, 18, 5, 10, 20, 6),
            Block.createCuboidShape(6, 18, 10, 10, 20, 11),
            Block.createCuboidShape(6, 20, 7, 7, 23, 9),
            Block.createCuboidShape(7, 20, 6, 9, 23, 7),
            Block.createCuboidShape(9, 20, 6, 10, 23, 7),
            Block.createCuboidShape(6, 20, 6, 7, 23, 7),
            Block.createCuboidShape(6, 20, 9, 7, 23, 10),
            Block.createCuboidShape(9, 20, 9, 10, 23, 10),
            Block.createCuboidShape(7, 20, 9, 9, 23, 10),
            Block.createCuboidShape(9, 20, 7, 10, 23, 9),
            Block.createCuboidShape(4, 16, 12, 12, 17, 13),
            Block.createCuboidShape(4, 16, 3, 12, 17, 4),
            Block.createCuboidShape(3, 16, 4, 4, 17, 12),
            Block.createCuboidShape(12, 16, 4, 13, 17, 12),
            Block.createCuboidShape(13, 15, 3, 14, 16, 13),
            Block.createCuboidShape(2, 15, 3, 3, 16, 13),
            Block.createCuboidShape(14, 15, 2, 15, 16, 14),
            Block.createCuboidShape(14, 0, 2, 15, 1, 14),
            Block.createCuboidShape(2, 15, 1, 14, 16, 2),
            Block.createCuboidShape(1, 15, 2, 2, 16, 14),
            Block.createCuboidShape(2, 15, 14, 14, 16, 15),
            Block.createCuboidShape(2, 0, 1, 14, 1, 2),
            Block.createCuboidShape(1, 0, 2, 2, 1, 14),
            Block.createCuboidShape(2, 0, 14, 14, 1, 15),
            Block.createCuboidShape(3, 15, 2, 13, 16, 3),
            Block.createCuboidShape(3, 15, 13, 13, 16, 14),
            Block.createCuboidShape(2, 0, 2, 14, 1, 14),
            Block.createCuboidShape(1, 1, 14, 2, 15, 15),
            Block.createCuboidShape(14, 1, 14, 15, 15, 15),
            Block.createCuboidShape(14, 1, 1, 15, 15, 2),
            Block.createCuboidShape(1, 1, 1, 2, 15, 2),
            Block.createCuboidShape(2, 1, 14, 14, 15, 15),
            Block.createCuboidShape(1, 1, 2, 2, 15, 14),
            Block.createCuboidShape(14, 1, 2, 15, 15, 14),
            Block.createCuboidShape(2, 1, 1, 14, 15, 2)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();

    private final VoxelShape SHAPE_S = Stream.of(
            Block.createCuboidShape(2, 19, 7, 3, 21, 9),
            Block.createCuboidShape(3, 18, 7, 5, 19, 9),
            Block.createCuboidShape(3, 21, 7, 6, 22, 9),
            Block.createCuboidShape(2, 18, 7, 3, 19, 9),
            Block.createCuboidShape(2, 21, 7, 3, 22, 9),
            Block.createCuboidShape(4, 17, 5, 5, 18, 11),
            Block.createCuboidShape(11, 17, 5, 12, 18, 11),
            Block.createCuboidShape(11, 17, 11, 12, 18, 12),
            Block.createCuboidShape(4, 17, 11, 5, 18, 12),
            Block.createCuboidShape(12, 16, 3, 13, 17, 4),
            Block.createCuboidShape(3, 16, 3, 4, 17, 4),
            Block.createCuboidShape(12, 16, 12, 13, 17, 13),
            Block.createCuboidShape(13, 15, 13, 14, 16, 14),
            Block.createCuboidShape(2, 15, 13, 3, 16, 14),
            Block.createCuboidShape(2, 15, 2, 3, 16, 3),
            Block.createCuboidShape(13, 15, 2, 14, 16, 3),
            Block.createCuboidShape(3, 16, 12, 4, 16, 13),
            Block.createCuboidShape(11, 17, 4, 12, 18, 5),
            Block.createCuboidShape(4, 17, 4, 5, 18, 5),
            Block.createCuboidShape(5, 17, 4, 11, 18, 5),
            Block.createCuboidShape(5, 17, 11, 11, 18, 12),
            Block.createCuboidShape(10, 18, 6, 11, 20, 10),
            Block.createCuboidShape(10, 18, 5, 11, 20, 6),
            Block.createCuboidShape(5, 18, 5, 6, 20, 6),
            Block.createCuboidShape(5, 18, 10, 6, 20, 11),
            Block.createCuboidShape(10, 18, 10, 11, 20, 11),
            Block.createCuboidShape(5, 18, 6, 6, 20, 10),
            Block.createCuboidShape(6, 18, 10, 10, 20, 11),
            Block.createCuboidShape(6, 18, 5, 10, 20, 6),
            Block.createCuboidShape(9, 20, 7, 10, 23, 9),
            Block.createCuboidShape(7, 20, 9, 9, 23, 10),
            Block.createCuboidShape(6, 20, 9, 7, 23, 10),
            Block.createCuboidShape(9, 20, 9, 10, 23, 10),
            Block.createCuboidShape(9, 20, 6, 10, 23, 7),
            Block.createCuboidShape(6, 20, 6, 7, 23, 7),
            Block.createCuboidShape(7, 20, 6, 9, 23, 7),
            Block.createCuboidShape(6, 20, 7, 7, 23, 9),
            Block.createCuboidShape(4, 16, 3, 12, 17, 4),
            Block.createCuboidShape(4, 16, 12, 12, 17, 13),
            Block.createCuboidShape(12, 16, 4, 13, 17, 12),
            Block.createCuboidShape(3, 16, 4, 4, 17, 12),
            Block.createCuboidShape(2, 15, 3, 3, 16, 13),
            Block.createCuboidShape(13, 15, 3, 14, 16, 13),
            Block.createCuboidShape(1, 15, 2, 2, 16, 14),
            Block.createCuboidShape(1, 0, 2, 2, 1, 14),
            Block.createCuboidShape(2, 15, 14, 14, 16, 15),
            Block.createCuboidShape(14, 15, 2, 15, 16, 14),
            Block.createCuboidShape(2, 15, 1, 14, 16, 2),
            Block.createCuboidShape(2, 0, 14, 14, 1, 15),
            Block.createCuboidShape(14, 0, 2, 15, 1, 14),
            Block.createCuboidShape(2, 0, 1, 14, 1, 2),
            Block.createCuboidShape(3, 15, 13, 13, 16, 14),
            Block.createCuboidShape(3, 15, 2, 13, 16, 3),
            Block.createCuboidShape(2, 0, 2, 14, 1, 14),
            Block.createCuboidShape(14, 1, 1, 15, 15, 2),
            Block.createCuboidShape(1, 1, 1, 2, 15, 2),
            Block.createCuboidShape(1, 1, 14, 2, 15, 15),
            Block.createCuboidShape(14, 1, 14, 15, 15, 15),
            Block.createCuboidShape(2, 1, 1, 14, 15, 2),
            Block.createCuboidShape(14, 1, 2, 15, 15, 14),
            Block.createCuboidShape(1, 1, 2, 2, 15, 14),
            Block.createCuboidShape(2, 1, 14, 14, 15, 15)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();

    private final VoxelShape SHAPE_E = Stream.of(
            Block.createCuboidShape(7, 19, 13, 9, 21, 14),
            Block.createCuboidShape(7, 18, 11, 9, 19, 13),
            Block.createCuboidShape(7, 21, 10, 9, 22, 13),
            Block.createCuboidShape(7, 18, 13, 9, 19, 14),
            Block.createCuboidShape(7, 21, 13, 9, 22, 14),
            Block.createCuboidShape(5, 17, 11, 11, 18, 12),
            Block.createCuboidShape(5, 17, 4, 11, 18, 5),
            Block.createCuboidShape(11, 17, 4, 12, 18, 5),
            Block.createCuboidShape(11, 17, 11, 12, 18, 12),
            Block.createCuboidShape(3, 16, 3, 4, 17, 4),
            Block.createCuboidShape(3, 16, 12, 4, 17, 13),
            Block.createCuboidShape(12, 16, 3, 13, 17, 4),
            Block.createCuboidShape(13, 15, 2, 14, 16, 3),
            Block.createCuboidShape(13, 15, 13, 14, 16, 14),
            Block.createCuboidShape(2, 15, 13, 3, 16, 14),
            Block.createCuboidShape(2, 15, 2, 3, 16, 3),
            Block.createCuboidShape(12, 16, 12, 13, 16, 13),
            Block.createCuboidShape(4, 17, 4, 5, 18, 5),
            Block.createCuboidShape(4, 17, 11, 5, 18, 12),
            Block.createCuboidShape(4, 17, 5, 5, 18, 11),
            Block.createCuboidShape(11, 17, 5, 12, 18, 11),
            Block.createCuboidShape(6, 18, 5, 10, 20, 6),
            Block.createCuboidShape(5, 18, 5, 6, 20, 6),
            Block.createCuboidShape(5, 18, 10, 6, 20, 11),
            Block.createCuboidShape(10, 18, 10, 11, 20, 11),
            Block.createCuboidShape(10, 18, 5, 11, 20, 6),
            Block.createCuboidShape(6, 18, 10, 10, 20, 11),
            Block.createCuboidShape(10, 18, 6, 11, 20, 10),
            Block.createCuboidShape(5, 18, 6, 6, 20, 10),
            Block.createCuboidShape(7, 20, 6, 9, 23, 7),
            Block.createCuboidShape(9, 20, 7, 10, 23, 9),
            Block.createCuboidShape(9, 20, 9, 10, 23, 10),
            Block.createCuboidShape(9, 20, 6, 10, 23, 7),
            Block.createCuboidShape(6, 20, 6, 7, 23, 7),
            Block.createCuboidShape(6, 20, 9, 7, 23, 10),
            Block.createCuboidShape(6, 20, 7, 7, 23, 9),
            Block.createCuboidShape(7, 20, 9, 9, 23, 10),
            Block.createCuboidShape(3, 16, 4, 4, 17, 12),
            Block.createCuboidShape(12, 16, 4, 13, 17, 12),
            Block.createCuboidShape(4, 16, 3, 12, 17, 4),
            Block.createCuboidShape(4, 16, 12, 12, 17, 13),
            Block.createCuboidShape(3, 15, 13, 13, 16, 14),
            Block.createCuboidShape(3, 15, 2, 13, 16, 3),
            Block.createCuboidShape(2, 15, 14, 14, 16, 15),
            Block.createCuboidShape(2, 0, 14, 14, 1, 15),
            Block.createCuboidShape(14, 15, 2, 15, 16, 14),
            Block.createCuboidShape(2, 15, 1, 14, 16, 2),
            Block.createCuboidShape(1, 15, 2, 2, 16, 14),
            Block.createCuboidShape(14, 0, 2, 15, 1, 14),
            Block.createCuboidShape(2, 0, 1, 14, 1, 2),
            Block.createCuboidShape(1, 0, 2, 2, 1, 14),
            Block.createCuboidShape(13, 15, 3, 14, 16, 13),
            Block.createCuboidShape(2, 15, 3, 3, 16, 13),
            Block.createCuboidShape(2, 0, 2, 14, 1, 14),
            Block.createCuboidShape(1, 1, 1, 2, 15, 2),
            Block.createCuboidShape(1, 1, 14, 2, 15, 15),
            Block.createCuboidShape(14, 1, 14, 15, 15, 15),
            Block.createCuboidShape(14, 1, 1, 15, 15, 2),
            Block.createCuboidShape(1, 1, 2, 2, 15, 14),
            Block.createCuboidShape(2, 1, 1, 14, 15, 2),
            Block.createCuboidShape(2, 1, 14, 14, 15, 15),
            Block.createCuboidShape(14, 1, 2, 15, 15, 14)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();

    private final VoxelShape SHAPE_W = Stream.of(
            Block.createCuboidShape(7, 19, 2, 9, 21, 3),
            Block.createCuboidShape(7, 18, 3, 9, 19, 5),
            Block.createCuboidShape(7, 21, 3, 9, 22, 6),
            Block.createCuboidShape(7, 18, 2, 9, 19, 3),
            Block.createCuboidShape(7, 21, 2, 9, 22, 3),
            Block.createCuboidShape(5, 17, 4, 11, 18, 5),
            Block.createCuboidShape(5, 17, 11, 11, 18, 12),
            Block.createCuboidShape(4, 17, 11, 5, 18, 12),
            Block.createCuboidShape(4, 17, 4, 5, 18, 5),
            Block.createCuboidShape(12, 16, 12, 13, 17, 13),
            Block.createCuboidShape(12, 16, 3, 13, 17, 4),
            Block.createCuboidShape(3, 16, 12, 4, 17, 13),
            Block.createCuboidShape(2, 15, 13, 3, 16, 14),
            Block.createCuboidShape(2, 15, 2, 3, 16, 3),
            Block.createCuboidShape(13, 15, 2, 14, 16, 3),
            Block.createCuboidShape(13, 15, 13, 14, 16, 14),
            Block.createCuboidShape(3, 16, 3, 4, 16, 4),
            Block.createCuboidShape(11, 17, 11, 12, 18, 12),
            Block.createCuboidShape(11, 17, 4, 12, 18, 5),
            Block.createCuboidShape(11, 17, 5, 12, 18, 11),
            Block.createCuboidShape(4, 17, 5, 5, 18, 11),
            Block.createCuboidShape(6, 18, 10, 10, 20, 11),
            Block.createCuboidShape(10, 18, 10, 11, 20, 11),
            Block.createCuboidShape(10, 18, 5, 11, 20, 6),
            Block.createCuboidShape(5, 18, 5, 6, 20, 6),
            Block.createCuboidShape(5, 18, 10, 6, 20, 11),
            Block.createCuboidShape(6, 18, 5, 10, 20, 6),
            Block.createCuboidShape(5, 18, 6, 6, 20, 10),
            Block.createCuboidShape(10, 18, 6, 11, 20, 10),
            Block.createCuboidShape(7, 20, 9, 9, 23, 10),
            Block.createCuboidShape(6, 20, 7, 7, 23, 9),
            Block.createCuboidShape(6, 20, 6, 7, 23, 7),
            Block.createCuboidShape(6, 20, 9, 7, 23, 10),
            Block.createCuboidShape(9, 20, 9, 10, 23, 10),
            Block.createCuboidShape(9, 20, 6, 10, 23, 7),
            Block.createCuboidShape(9, 20, 7, 10, 23, 9),
            Block.createCuboidShape(7, 20, 6, 9, 23, 7),
            Block.createCuboidShape(12, 16, 4, 13, 17, 12),
            Block.createCuboidShape(3, 16, 4, 4, 17, 12),
            Block.createCuboidShape(4, 16, 12, 12, 17, 13),
            Block.createCuboidShape(4, 16, 3, 12, 17, 4),
            Block.createCuboidShape(3, 15, 2, 13, 16, 3),
            Block.createCuboidShape(3, 15, 13, 13, 16, 14),
            Block.createCuboidShape(2, 15, 1, 14, 16, 2),
            Block.createCuboidShape(2, 0, 1, 14, 1, 2),
            Block.createCuboidShape(1, 15, 2, 2, 16, 14),
            Block.createCuboidShape(2, 15, 14, 14, 16, 15),
            Block.createCuboidShape(14, 15, 2, 15, 16, 14),
            Block.createCuboidShape(1, 0, 2, 2, 1, 14),
            Block.createCuboidShape(2, 0, 14, 14, 1, 15),
            Block.createCuboidShape(14, 0, 2, 15, 1, 14),
            Block.createCuboidShape(2, 15, 3, 3, 16, 13),
            Block.createCuboidShape(13, 15, 3, 14, 16, 13),
            Block.createCuboidShape(2, 0, 2, 14, 1, 14),
            Block.createCuboidShape(14, 1, 14, 15, 15, 15),
            Block.createCuboidShape(14, 1, 1, 15, 15, 2),
            Block.createCuboidShape(1, 1, 1, 2, 15, 2),
            Block.createCuboidShape(1, 1, 14, 2, 15, 15),
            Block.createCuboidShape(14, 1, 2, 15, 15, 14),
            Block.createCuboidShape(2, 1, 14, 14, 15, 15),
            Block.createCuboidShape(2, 1, 1, 14, 15, 2),
            Block.createCuboidShape(1, 1, 2, 2, 15, 14)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();

    private final VoxelShape SHAPE_N_AIRLOCK = Stream.of(
            Block.createCuboidShape(7, 22, 7, 9, 23, 9),
            Block.createCuboidShape(9, 24, 7, 10, 25, 9),
            Block.createCuboidShape(10, 25, 7, 11, 29, 9),
            Block.createCuboidShape(7, 26, 7, 9, 28, 9),
            Block.createCuboidShape(4, 26, 7, 6, 28, 9),
            Block.createCuboidShape(4, 28, 7, 5, 29, 9),
            Block.createCuboidShape(4, 29, 7, 5, 30, 9),
            Block.createCuboidShape(5, 30, 7, 7, 31, 9),
            Block.createCuboidShape(7, 31, 7, 8, 32, 9),
            Block.createCuboidShape(9, 29, 7, 10, 30, 9),
            Block.createCuboidShape(5, 28, 7, 6, 29, 9),
            Block.createCuboidShape(7, 28, 7, 9, 29, 9),
            Block.createCuboidShape(7, 23, 7, 8, 24, 9),
            Block.createCuboidShape(8, 23, 7, 9, 24, 9),
            Block.createCuboidShape(8, 24, 7, 9, 25, 9),
            Block.createCuboidShape(6, 25, 7, 7, 26, 9)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();
    private final VoxelShape SHAPE_S_AIRLOCK = Stream.of(
            Block.createCuboidShape(7, 22, 7, 9, 23, 9),
            Block.createCuboidShape(6, 24, 7, 7, 25, 9),
            Block.createCuboidShape(5, 25, 7, 6, 29, 9),
            Block.createCuboidShape(7, 26, 7, 9, 28, 9),
            Block.createCuboidShape(10, 26, 7, 12, 28, 9),
            Block.createCuboidShape(11, 28, 7, 12, 29, 9),
            Block.createCuboidShape(11, 29, 7, 12, 30, 9),
            Block.createCuboidShape(9, 30, 7, 11, 31, 9),
            Block.createCuboidShape(8, 31, 7, 9, 32, 9),
            Block.createCuboidShape(6, 29, 7, 7, 30, 9),
            Block.createCuboidShape(10, 28, 7, 11, 29, 9),
            Block.createCuboidShape(7, 28, 7, 9, 29, 9),
            Block.createCuboidShape(8, 23, 7, 9, 24, 9),
            Block.createCuboidShape(7, 23, 7, 8, 24, 9),
            Block.createCuboidShape(7, 24, 7, 8, 25, 9),
            Block.createCuboidShape(9, 25, 7, 10, 26, 9)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();
    private final VoxelShape SHAPE_E_AIRLOCK = Stream.of(
            Block.createCuboidShape(7, 22, 7, 9, 23, 9),
            Block.createCuboidShape(7, 24, 9, 9, 25, 10),
            Block.createCuboidShape(7, 25, 10, 9, 29, 11),
            Block.createCuboidShape(7, 26, 7, 9, 28, 9),
            Block.createCuboidShape(7, 26, 4, 9, 28, 6),
            Block.createCuboidShape(7, 28, 4, 9, 29, 5),
            Block.createCuboidShape(7, 29, 4, 9, 30, 5),
            Block.createCuboidShape(7, 30, 5, 9, 31, 7),
            Block.createCuboidShape(7, 31, 7, 9, 32, 8),
            Block.createCuboidShape(7, 29, 9, 9, 30, 10),
            Block.createCuboidShape(7, 28, 5, 9, 29, 6),
            Block.createCuboidShape(7, 28, 7, 9, 29, 9),
            Block.createCuboidShape(7, 23, 7, 9, 24, 8),
            Block.createCuboidShape(7, 23, 8, 9, 24, 9),
            Block.createCuboidShape(7, 24, 8, 9, 25, 9),
            Block.createCuboidShape(7, 25, 6, 9, 26, 7)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();
    private final VoxelShape SHAPE_W_AIRLOCK = Stream.of(
            Block.createCuboidShape(7, 22, 7, 9, 23, 9),
            Block.createCuboidShape(7, 24, 6, 9, 25, 7),
            Block.createCuboidShape(7, 25, 5, 9, 29, 6),
            Block.createCuboidShape(7, 26, 7, 9, 28, 9),
            Block.createCuboidShape(7, 26, 10, 9, 28, 12),
            Block.createCuboidShape(7, 28, 11, 9, 29, 12),
            Block.createCuboidShape(7, 29, 11, 9, 30, 12),
            Block.createCuboidShape(7, 30, 9, 9, 31, 11),
            Block.createCuboidShape(7, 31, 8, 9, 32, 9),
            Block.createCuboidShape(7, 29, 6, 9, 30, 7),
            Block.createCuboidShape(7, 28, 10, 9, 29, 11),
            Block.createCuboidShape(7, 28, 7, 9, 29, 9),
            Block.createCuboidShape(7, 23, 8, 9, 24, 9),
            Block.createCuboidShape(7, 23, 7, 9, 24, 8),
            Block.createCuboidShape(7, 24, 7, 9, 25, 8),
            Block.createCuboidShape(7, 25, 9, 9, 26, 10)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);}).get();
    //endregion
    public static final DirectionProperty CARBOY_FACING = DirectionProperty.of("facing", Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
    public static final BooleanProperty CONTAINS_LIQUID = BooleanProperty.of("contains_liquid");
    public static final BooleanProperty HAS_AIRLOCK = BooleanProperty.of("has_airlock");

    public CarboyBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getStateManager().getDefaultState()
                .with(CARBOY_FACING, Direction.NORTH)
                .with(CONTAINS_LIQUID, false)
                .with(HAS_AIRLOCK, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context){
        return this.getDefaultState()
                .with(CARBOY_FACING, context.getPlayerFacing().getOpposite())
                .with(CONTAINS_LIQUID, false)
                .with(HAS_AIRLOCK, false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager){
        stateManager.add(CARBOY_FACING)
                .add(CONTAINS_LIQUID)
                .add(HAS_AIRLOCK);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context){
        switch(state.get(CARBOY_FACING)){
            case NORTH:
                if(state.get(HAS_AIRLOCK))
                    return VoxelShapes.combineAndSimplify(SHAPE_N, SHAPE_N_AIRLOCK, BooleanBiFunction.OR);
                return SHAPE_N;
            case SOUTH:
                if(state.get(HAS_AIRLOCK))
                    return VoxelShapes.combineAndSimplify(SHAPE_S, SHAPE_S_AIRLOCK, BooleanBiFunction.OR);
                return SHAPE_S;
            case EAST:
                if(state.get(HAS_AIRLOCK))
                    return VoxelShapes.combineAndSimplify(SHAPE_E, SHAPE_E_AIRLOCK, BooleanBiFunction.OR);
                return SHAPE_E;
            default:
                if(state.get(HAS_AIRLOCK))
                    return VoxelShapes.combineAndSimplify(SHAPE_W, SHAPE_W_AIRLOCK, BooleanBiFunction.OR);
                return SHAPE_W;
        }
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockView world) {
        return new CarboyBlockEntity();
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if(state.get(HAS_AIRLOCK) && !player.isCreative()){
            world.spawnEntity(new ItemEntity(world, (double)pos.getX() + 0.5, (double)pos.getY()+0.5, (double)pos.getZ()+ 0.5, new ItemStack(AIRLOCK.asItem())));
        }
    }
}
