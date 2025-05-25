package net.PureWhite_owo.redlite.util;

import net.PureWhite_owo.redlite.RedLite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks{

        private static TagKey<Block> createTag(String name){
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(RedLite.MOD_ID, name));
        }
    }
    public static class Items{
//        public static final TagKey<Item> REDLITE_ITEMS =createTag("redlite_items");

        private static TagKey<Item> createTag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(RedLite.MOD_ID, name));
        }
    }
}
