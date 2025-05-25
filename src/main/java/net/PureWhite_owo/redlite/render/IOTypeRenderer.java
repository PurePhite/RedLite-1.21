package net.PureWhite_owo.redlite.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.PureWhite_owo.redlite.util.FaceConfigurable;
import net.PureWhite_owo.redlite.util.IOType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Matrix4f;

public class IOTypeRenderer<T extends BlockEntity & FaceConfigurable> implements BlockEntityRenderer<T> {

    public IOTypeRenderer(BlockEntityRendererProvider.Context context){
    }

    @Override
    public void render(T pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        Level level = pBlockEntity.getLevel();
        if (level == null) return;

        for (Direction dir : Direction.values()){
            IOType io = pBlockEntity.getFaceConfig(dir);
            if (io != IOType.NONE){
                pPoseStack.pushPose();
                pPoseStack.translate(0.5, 0.5, 0.5);
                switch (dir){
                    case UP -> pPoseStack.mulPose(Axis.XP.rotationDegrees(90));
                    case DOWN -> pPoseStack.mulPose(Axis.XP.rotationDegrees(-90));
                    case NORTH -> {}
                    case SOUTH -> pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
                    case EAST -> pPoseStack.mulPose(Axis.YP.rotationDegrees(-90));
                    case WEST -> pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
                }
                pPoseStack.translate(0, 0, -0.501);

                VertexConsumer builder = pBufferSource.getBuffer(RenderType.lines());

                float r = io == IOType.INPUT ? 1.0f : 0.0f;
                float g = io == IOType.OUTPUT ? 1.0f : 0.0f;
                float b = io == IOType.NONE ? 1.0f : 0.0f;

                drawSquare(pPoseStack, builder, r, g, b, pPackedLight);
                pPoseStack.popPose();

            }
        }
    }

    private void drawSquare(PoseStack poseStack, VertexConsumer builder,
                            float r, float g, float b, int light) {
        float size = 0.25f;
        float half = size / 2;
        Matrix4f matrix = poseStack.last().pose();

        drawFace(matrix, builder, half, r, g, b, light, -1);
        drawFace(matrix, builder, half, r, g, b, light, 1);
    }

    private void drawFace(Matrix4f matrix, VertexConsumer builder, float half,float r, float g, float b,int light, float nz){
        builder.addVertex(matrix, -half, -half, 0).setColor(r, g, b, 1).setNormal(0, 0, nz).setUv(0, 0).setUv2(light, 0);
        builder.addVertex(matrix, half, -half, 0).setColor(r, g, b, 1).setNormal(0, 0, nz).setUv(1, 0).setUv2(light, 0);
        builder.addVertex(matrix, half, half, 0).setColor(r, g, b, 1).setNormal(0, 0, nz).setUv(1, 1).setUv2(light, 0);

    }

    @Override
    public boolean shouldRenderOffScreen(T pBlockEntity) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(pBlockEntity);
    }
}
