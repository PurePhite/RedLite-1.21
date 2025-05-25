package net.PureWhite_owo.redlite.datagen;

import net.PureWhite_owo.redlite.RedLite;
import net.PureWhite_owo.redlite.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, RedLite.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.LOGIC_BLOCK.get())
                .add(ModBlocks.DELAY_BLOCK.get())
                .add(ModBlocks.PULSE_BLOCK.get());
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.LOGIC_BLOCK.get())
                .add(ModBlocks.DELAY_BLOCK.get())
                .add(ModBlocks.PULSE_BLOCK.get());
    }
}
