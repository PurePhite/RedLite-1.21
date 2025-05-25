package net.PureWhite_owo.redlite.event;

import net.PureWhite_owo.redlite.RedLite;
import net.PureWhite_owo.redlite.block.entity.ModBlockEntities;
import net.PureWhite_owo.redlite.render.IOTypeRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RedLite.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.LOGIC_BLOCK_BE.get(), IOTypeRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DELAY_BLOCK_BE.get(), IOTypeRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.PULSE_BLOCK_BE.get(), IOTypeRenderer::new);
    }
}
