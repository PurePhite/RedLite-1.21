package net.PureWhite_owo.redlite.block.custom;

import net.PureWhite_owo.redlite.block.entity.custom.LogicBlockEntity;
import net.PureWhite_owo.redlite.util.IOType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class LogicBlock extends Block implements EntityBlock {

    public LogicBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {

    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new LogicBlockEntity(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if(!pLevel.isClientSide) {
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if(be instanceof LogicBlockEntity logicBlockEntity){
                Direction face = pHitResult.getDirection();
                if (pPlayer.isShiftKeyDown()) {
                    logicBlockEntity.cycleLogicType();
                    pPlayer.displayClientMessage(Component.literal("Logic Mode: " + logicBlockEntity.getLogicType()),true);
                }else {
                    logicBlockEntity.cycleFaceConfig(face);
                    pPlayer.displayClientMessage(Component.literal(face + "set to "+ logicBlockEntity.getFaceConfig(face)),true);
                }
                logicBlockEntity.updateLogic();
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isSignalSource(BlockState pState) {
        return true;
    }

    @Override
    public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        BlockEntity be = pLevel.getBlockEntity(pPos);
        if (be instanceof LogicBlockEntity logicEntity) {
            if (logicEntity.getFaceConfig(pDirection.getOpposite()) == IOType.OUTPUT){
                return logicEntity.evaluateSignal();
            }
        }
        return 0;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : (pLevel1, pPos, pState1, pBlockEntity) -> {
            if (pBlockEntity instanceof LogicBlockEntity logicBlockEntity){
                logicBlockEntity.updateLogic();
            }
        };
    }
}
