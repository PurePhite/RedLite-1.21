package net.PureWhite_owo.redlite.block.entity.custom;

import net.PureWhite_owo.redlite.block.custom.DelayBlock;
import net.PureWhite_owo.redlite.block.entity.ModBlockEntities;
import net.PureWhite_owo.redlite.util.FaceConfigurable;
import net.PureWhite_owo.redlite.util.IOType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

public class DelayBlockEntity extends BlockEntity implements FaceConfigurable {
    private final EnumMap<Direction, IOType> faceConfig = new EnumMap<>(Direction.class);

    private int delayTicks = 20;
    private int timer = -1;
    private int lastInput = 0;
    private boolean hasPulsed = false;

    public DelayBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DELAY_BLOCK_BE.get(), pPos, pBlockState);
        for (Direction dir : Direction.values()){
            faceConfig.put(dir, IOType.NONE);
        }
    }

    public int getDelayTicks() {
        return delayTicks;
    }

    public void increaseDelay() {
        delayTicks = Math.min(delayTicks+1, 100);
        setChanged();
        if (level != null && !level.isClientSide){
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public void decreaseDelay() {
        delayTicks = Math.max(delayTicks-1, 1);
        setChanged();
        if (level != null && !level.isClientSide){
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public IOType getFaceConfig(Direction direction) {
        return faceConfig.getOrDefault(direction, IOType.NONE);
    }

    public void cycleFaceConfig(Direction dir) {
        IOType next = faceConfig.get(dir).next();
        faceConfig.put(dir, next);
        setChanged();
        if (level != null && !level.isClientSide){
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, DelayBlockEntity be){
        int currentInput = 0;

        for (Direction dir : Direction.values()) {
            if (be.faceConfig.get(dir) == IOType.INPUT) {
                int signal = pLevel.getSignal(pPos.relative(dir), dir.getOpposite());
                currentInput = Math.max(currentInput, signal);
            }
        }

        if (currentInput > 0 && be.lastInput == 0 && be.timer < 0) {
            be.timer = be.delayTicks;
            be.hasPulsed = false;
        }

        be.lastInput = currentInput;

        if (be.timer >= 0){
            be.timer--;
            if (be.timer == 0 && !be.hasPulsed) {
                be.hasPulsed = true;
                pLevel.setBlock(pPos, pState.setValue(DelayBlock.POWERED, true), 3);
                pLevel.updateNeighborsAt(pPos, pState.getBlock());
                pLevel.scheduleTick(pPos, pState.getBlock(), 2);
                be.timer = -1;
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return this.saveWithoutMetadata(pRegistries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookup) {
        this.loadAdditional(pkt.getTag(), lookup);
    }

    @Override
    public void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        for (Direction dir : Direction.values()) {
            pTag.putString("face_" + dir.getName(), faceConfig.get(dir).name());
        }
        pTag.putInt("delay_ticks", delayTicks);
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        for (Direction dir : Direction.values()) {
            String name = pTag.getString("face_" + dir.getName());
            if (!name.isEmpty()) {
                try {
                    faceConfig.put(dir, IOType.valueOf(name));
                } catch (IllegalArgumentException ignored) {}
                try {
                    delayTicks = pTag.getInt("delay_ticks");
                } catch (IllegalArgumentException ignored) {}
            }
        }
    }
}
