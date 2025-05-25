package net.PureWhite_owo.redlite.datagen;

import net.PureWhite_owo.redlite.RedLite;
import net.PureWhite_owo.redlite.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RedLite.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.SUPER_RED_STICK.get());
        basicItem(ModItems.RED_CORE.get());
    }
}
