package net.PureWhite_owo.redlite.block;

import net.PureWhite_owo.redlite.RedLite;
import net.PureWhite_owo.redlite.block.custom.DelayBlock;
import net.PureWhite_owo.redlite.block.custom.LogicBlock;
import net.PureWhite_owo.redlite.block.custom.PulseBlock;
import net.PureWhite_owo.redlite.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RedLite.MOD_ID);

    //add block
    public static final RegistryObject<Block> LOGIC_BLOCK = registerBlock("logic_block",
            () -> new LogicBlock(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.METAL)));
    public static final RegistryObject<Block> DELAY_BLOCK = registerBlock("delay_block",
            () -> new DelayBlock(BlockBehaviour.Properties.of().strength(4f).mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().sound(SoundType.METAL)));
    public static final RegistryObject<Block> PULSE_BLOCK = registerBlock("pulse_block",
            () -> new PulseBlock(BlockBehaviour.Properties.of().strength(4f).mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
