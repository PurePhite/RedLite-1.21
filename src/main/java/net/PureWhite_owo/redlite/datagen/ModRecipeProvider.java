package net.PureWhite_owo.redlite.datagen;

import net.PureWhite_owo.redlite.block.ModBlocks;
import net.PureWhite_owo.redlite.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SUPER_RED_STICK.get())
                .pattern("A")
                .pattern("A")
                .define('A', Items.REDSTONE)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE)).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RED_CORE.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', Items.COBBLESTONE)
                .define('B', ModItems.SUPER_RED_STICK.get())
                .define('C', Items.IRON_INGOT)
                .unlockedBy(getHasName(ModItems.SUPER_RED_STICK.get()), has(ModItems.SUPER_RED_STICK.get())).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.LOGIC_BLOCK.get())
                .pattern("ABA")
                .pattern("CDC")
                .pattern("ABA")
                .define('A', Items.COBBLESTONE)
                .define('B', Items.COMPARATOR)
                .define('C', ModItems.SUPER_RED_STICK.get())
                .define('D', ModItems.RED_CORE.get())
                .unlockedBy(getHasName(ModItems.RED_CORE.get()), has(ModItems.RED_CORE.get())).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.DELAY_BLOCK.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', Items.COBBLESTONE)
                .define('B', Items.REPEATER)
                .define('C', ModItems.RED_CORE.get())
                .unlockedBy(getHasName(ModItems.RED_CORE.get()), has(ModItems.RED_CORE.get())).save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.PULSE_BLOCK.get())
                .pattern("ABA")
                .pattern("CDC")
                .pattern("ABA")
                .define('A', Items.COBBLESTONE)
                .define('B', Items.REPEATER)
                .define('C', ModItems.SUPER_RED_STICK.get())
                .define('D', ModItems.RED_CORE.get())
                .unlockedBy(getHasName(ModItems.RED_CORE.get()),has(ModItems.RED_CORE.get())).save(pRecipeOutput);
    }
}
