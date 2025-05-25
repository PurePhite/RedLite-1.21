package net.PureWhite_owo.redlite.block.entity.custom;

import net.PureWhite_owo.redlite.block.entity.ModBlockEntities;
import net.PureWhite_owo.redlite.util.FaceConfigurable;
import net.PureWhite_owo.redlite.util.IOType;
import net.PureWhite_owo.redlite.util.LogicType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

public class LogicBlockEntity extends BlockEntity implements FaceConfigurable {
    private final EnumMap<Direction, IOType> faceConfig = new EnumMap<>(Direction.class);
    private LogicType logicType = LogicType.OR;

    public LogicBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.LOGIC_BLOCK_BE.get(), pPos, pBlockState);
        for (Direction dir : Direction.values()) {
            faceConfig.put(dir, IOType.NONE);
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
        updateLogic();
        updateSignal();
        if (level != null && !level.isClientSide){
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public LogicType getLogicType() {
        return logicType;
    }

    public void cycleLogicType() {
        logicType = logicType.next();
        setChanged();
        updateLogic();
        updateSignal();
    }

    public int evaluateSignal() {
        int input = 0;
        int totalInputs = 0;

        for (Direction dir : Direction.values()) {
            if (faceConfig.get(dir) == IOType.INPUT) {
                BlockPos neighbor = getBlockPos().relative(dir);
                int signal = getLevel().getBestNeighborSignal(neighbor);
                if (signal > 0) input++;
                totalInputs++;
            }
        }

        int result = switch (logicType) {
            case AND -> (input == totalInputs && totalInputs > 0) ? 15:0;
            case OR -> (input > 0) ? 15:0;
            case NOT -> (input == 0 && totalInputs > 0) ? 15:0;
        };
        return result;
    }

    public void updateLogic () {
        if (level == null || level.isClientSide) return;

        for (Direction dir : Direction.values()){
            if (faceConfig.get(dir) == IOType.OUTPUT){
                BlockPos outPos = getBlockPos().relative(dir);
                level.updateNeighborsAt(outPos, getBlockState().getBlock());
            }
        }
    }

    public void updateSignal () {
        int signal = evaluateSignal();
        getLevel().updateNeighborsAt(getBlockPos(),getBlockState().getBlock());
        setChanged();
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
        pTag.putString("logic_type", logicType.name());
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        for (Direction dir : Direction.values()) {
            String name = pTag.getString("face_" + dir.getName());
            if (!name.isEmpty()) {
                try {
                    faceConfig.put(dir, IOType.valueOf(name));
                } catch (IllegalArgumentException ignored) {
                }
            }
            try {
                logicType = LogicType.valueOf(pTag.getString("logic_type"));
            } catch (IllegalArgumentException ignored) {}
        }
    }
}

