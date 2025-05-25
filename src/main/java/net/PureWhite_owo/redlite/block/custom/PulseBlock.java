package net.PureWhite_owo.redlite.block.custom;

import net.PureWhite_owo.redlite.block.entity.custom.DelayBlockEntity;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class PulseBlock extends Block implements EntityBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public PulseBlock(Properties properties) {
        super(BlockBehaviour.Properties.of().strength(4f).mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().sound(SoundType.METAL));
        registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected boolean isSignalSource(BlockState pState) {
        return true;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PulseBlockEntity(pPos, pState);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide){
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof PulseBlockEntity pulseBlockEntity){
                if (pPlayer.isShiftKeyDown()){
                    pulseBlockEntity.increasePulse();
                    pPlayer.displayClientMessage(Component.literal("Pulse: " + pulseBlockEntity.getPulseTicks() + " ticks"),true);
                }else {
                    Direction face = pHitResult.getDirection();
                    pulseBlockEntity.cycleFaceConfig(face);
                    pPlayer.displayClientMessage(Component.literal(face + "set to "+ pulseBlockEntity.getFaceConfig(face)),true);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        if (!pLevel.isClientSide && pPlayer.isShiftKeyDown()){
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (be instanceof PulseBlockEntity pulseBlockEntity){
                pulseBlockEntity.decreasePulse();
                pPlayer.displayClientMessage(Component.literal("Pulse: " + pulseBlockEntity.getPulseTicks() + " ticks"),true);
            }
        }
    }

    @Override
    public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        BlockEntity be = pLevel.getBlockEntity(pPos);
        if (be instanceof PulseBlockEntity pulseBlockEntity) {
            if (pState.getValue(POWERED) && pulseBlockEntity.getFaceConfig(pDirection.getOpposite()) == IOType.OUTPUT){
                return 15;
            }
        }
        return 0;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(POWERED)){
            pLevel.setBlock(pPos, pState.setValue(POWERED, false), 3);
            pLevel.updateNeighborsAt(pPos, pState.getBlock());
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : (lvl, pos, st, be) -> {
            if (be instanceof PulseBlockEntity pulseBlockEntity) {
                PulseBlockEntity.tick(lvl, pos, st, pulseBlockEntity);
            }
        };
    }
}
