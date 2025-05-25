package net.PureWhite_owo.redlite.block.custom;

import net.PureWhite_owo.redlite.block.entity.custom.DelayBlockEntity;
import net.PureWhite_owo.redlite.block.entity.custom.LogicBlockEntity;
import net.PureWhite_owo.redlite.block.entity.custom.PulseBlockEntity;
import net.PureWhite_owo.redlite.util.IOType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.redstone.Redstone;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class DelayBlock extends Block implements EntityBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public DelayBlock(Properties properties) {
        super(BlockBehaviour.Properties.of().strength(4f).mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().sound(SoundType.METAL));
        registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean isSignalSource(BlockState pState) {
        return true;
    }

    @Override
    public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        BlockEntity be = pLevel.getBlockEntity(pPos);
        if (be instanceof DelayBlockEntity delayBlockEntity) {
            if (pState.getValue(POWERED) && delayBlockEntity.getFaceConfig(pDirection.getOpposite()) == IOType.OUTPUT){
                return 15;
            }
        }
        return 0;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide){
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof DelayBlockEntity delayBlockEntity){
                if (pPlayer.isShiftKeyDown()){
                    delayBlockEntity.increaseDelay();
                    pPlayer.displayClientMessage(Component.literal("Delay: " + delayBlockEntity.getDelayTicks() + " ticks"),true);
                }else {
                    Direction face = pHitResult.getDirection();
                    delayBlockEntity.cycleFaceConfig(face);
                    pPlayer.displayClientMessage(Component.literal(face + "set to "+ delayBlockEntity.getFaceConfig(face)),true);
                }
            }
            //update
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        if (!pLevel.isClientSide && pPlayer.isShiftKeyDown()){
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof DelayBlockEntity delayBlockEntity){
                delayBlockEntity.decreaseDelay();
                pPlayer.displayClientMessage(Component.literal("Delay: " + delayBlockEntity.getDelayTicks() + " ticks"),true);

            }
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(POWERED)){
            pLevel.setBlock(pPos, pState.setValue(POWERED, false), 3);
            pLevel.updateNeighborsAt(pPos, pState.getBlock());
        }
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DelayBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : (lvl, pos, st, be) -> {
            if (be instanceof DelayBlockEntity delayBlockEntity) {
                DelayBlockEntity.tick(lvl, pos, st, delayBlockEntity);
            }
        };
    }
}
