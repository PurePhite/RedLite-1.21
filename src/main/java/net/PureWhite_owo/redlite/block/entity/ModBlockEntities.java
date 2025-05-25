package net.PureWhite_owo.redlite.block.entity;

import net.PureWhite_owo.redlite.RedLite;
import net.PureWhite_owo.redlite.block.ModBlocks;
import net.PureWhite_owo.redlite.block.custom.LogicBlock;
import net.PureWhite_owo.redlite.block.entity.custom.DelayBlockEntity;
import net.PureWhite_owo.redlite.block.entity.custom.LogicBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RedLite.MOD_ID);

    public static final RegistryObject<BlockEntityType<LogicBlockEntity>> LOGIC_BLOCK_BE =
            BLOCK_ENTITIES.register("logic_block",
                    () -> BlockEntityType.Builder.of(LogicBlockEntity::new, ModBlocks.LOGIC_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<DelayBlockEntity>> DELAY_BLOCK_BE =
            BLOCK_ENTITIES.register("delay_block",
                    () -> BlockEntityType.Builder.of(DelayBlockEntity::new, ModBlocks.DELAY_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<DelayBlockEntity>> PULSE_BLOCK_BE =
            BLOCK_ENTITIES.register("pulse_block",
                    () -> BlockEntityType.Builder.of(DelayBlockEntity::new, ModBlocks.PULSE_BLOCK.get()).build(null));


    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
