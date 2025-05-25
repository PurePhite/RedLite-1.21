package net.PureWhite_owo.redlite.item;

import net.PureWhite_owo.redlite.RedLite;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RedLite.MOD_ID);

    //add item
        public static final RegistryObject<Item> SUPER_RED_STICK = ITEMS.register("super_red_stick",
                () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> RED_CORE = ITEMS.register("red_core",
                () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
