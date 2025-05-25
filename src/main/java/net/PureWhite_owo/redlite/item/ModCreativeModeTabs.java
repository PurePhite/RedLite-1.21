package net.PureWhite_owo.redlite.item;

import net.PureWhite_owo.redlite.RedLite;
import net.PureWhite_owo.redlite.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.swing.plaf.PanelUI;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RedLite.MOD_ID);

    public static final RegistryObject<CreativeModeTab> REDLITE_TAB = CREATIVE_MODE_TABS.register("redlite_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SUPER_RED_STICK.get()))
                    .title(Component.translatable("creativetab.redlite.redlite"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.SUPER_RED_STICK.get());
                        pOutput.accept(ModItems.RED_CORE.get());
                        pOutput.accept(ModBlocks.LOGIC_BLOCK.get());
                        pOutput.accept(ModBlocks.DELAY_BLOCK.get());
                        pOutput.accept(ModBlocks.PULSE_BLOCK.get());
                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
