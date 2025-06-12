package me.zyouime.itemcooldown.util.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderHelper {

    private static final Tessellator tessellator = Tessellator.getInstance();
    public static final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    public static void drawRect(MatrixStack matrixStack, float x, float y, float width, float height, Color color) {
        setupRender();
        Matrix4f matrix4f = matrixStack.peek().getModel();
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;
        BufferBuilder vertexConsumer = tessellator.getBuffer();
        vertexConsumer.begin(7, VertexFormats.POSITION_COLOR);
        vertexConsumer.vertex(matrix4f, x, y, 0).color(r, g, b, a).next();
        vertexConsumer.vertex(matrix4f, x, y + height, 0).color(r, g, b, a).next();
        vertexConsumer.vertex(matrix4f,x + width, y + height, 0).color(r, g, b, a).next();
        vertexConsumer.vertex(matrix4f,x + width, y, 0).color(r, g, b, a).next();
        endRender();
    }

    public static void drawCenteredXYText(MatrixStack matrixStack, float x, float y, float scale, String text, Color color) {
        drawText(matrixStack, x - (textRenderer.getWidth(text) / 2f * scale), y + (textRenderer.fontHeight / 2f * scale), scale, text, color);
    }


    public static void drawCenteredYText(MatrixStack matrixStack, float x, float y, float scale, String text, Color color) {
        drawText(matrixStack, x, y + (textRenderer.fontHeight / 2f * scale), scale, text, color);
    }

    public static void drawText(MatrixStack matrixStack, float x, float y, float scale, String text, Color color) {
        matrixStack.push();
        matrixStack.scale(scale, scale, scale);
        textRenderer.draw(matrixStack, text, x / scale, y / scale, color.getRGB());
        matrixStack.pop();
    }

    public static void drawTexture(MatrixStack matrixStack, float x, float y, float width, float height, float u, float v, float regionWidth, float regionHeight, float textureWidth, float textureHeight, Identifier texture) {
        MinecraftClient.getInstance().getTextureManager().bindTexture(texture);
        Matrix4f matrix4f = matrixStack.peek().getModel();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, x, y, 0).texture(u / textureHeight, v / textureHeight).next();
        bufferBuilder.vertex(matrix4f, x, y + height, 0).texture(u / textureWidth, (v + regionHeight) / textureHeight).next();
        bufferBuilder.vertex(matrix4f, x + width, y + height, 0).texture((u + regionWidth) / textureWidth, (v + regionHeight) / textureHeight).next();
        bufferBuilder.vertex(matrix4f, x + width, y, 0).texture((u + regionWidth) / textureWidth, v / textureHeight).next();
        tessellator.draw();
        RenderSystem.disableBlend();
    }


    public static void drawItem(MatrixStack matrices, ItemStack stack, float x, float y) {
        if (stack.isEmpty()) return;
        matrices.push();
        MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).setFilter(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        matrices.translate(x, y, 150);
        matrices.translate(8.0F, 8.0F, 0.0F);
        matrices.scale(1.0F, -1.0F, 1.0F);
        matrices.scale(16.0F, 16.0F, 16.0F);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        BakedModel model = MinecraftClient.getInstance().getItemRenderer().getHeldItemModel(stack, null, null);
        boolean bl = !model.isSideLit();
        if (bl) {
            DiffuseLighting.disableGuiDepthLighting();
        }
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GUI, false, matrices, immediate, 15728880, OverlayTexture.DEFAULT_UV, model);
        immediate.draw();
        RenderSystem.enableDepthTest();
        if (bl) {
            DiffuseLighting.enableGuiDepthLighting();
        }

        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        matrices.pop();
    }

    public static void enableScissor(int x1, int y1, int x2, int y2) {
        Window window = MinecraftClient.getInstance().getWindow();
        int fbHeight = window.getFramebufferHeight();
        double scale = window.getScaleFactor();
        int ox = (int) Math.floor(x1 * scale);
        int oy = (int) Math.floor(fbHeight - (y2 * scale));
        int w  = (int) Math.ceil((x2 - x1) * scale);
        int h  = (int) Math.ceil((y2 - y1) * scale);
        RenderSystem.enableScissor(ox, oy, w, h);
    }

    public static void disableScissor() {
        RenderSystem.disableScissor();
    }

    public static void drawRoundedRect(MatrixStack matrixStack,float x, float y, float width, float height, float radius, Color color) {
        drawRect(matrixStack, x + radius, y + radius, width - 2 * radius, height - 2 * radius, color);
        drawRect(matrixStack, x + radius, y, width - 2 * radius, radius, color);
        drawRect(matrixStack, x + radius, y + height - radius, width - 2 * radius, radius, color);
        drawRect(matrixStack, x, y + radius, radius, height - 2 * radius, color);
        drawRect(matrixStack, x + width - radius, y + radius, radius, height - 2 * radius, color);
        drawQuadrantCircle(matrixStack, x + radius, y + radius, radius, color, 3);
        drawQuadrantCircle(matrixStack, x + width - radius, y + radius, radius, color, 2);
        drawQuadrantCircle(matrixStack, x + radius, y + height - radius, radius, color, 4);
        drawQuadrantCircle(matrixStack, x + width - radius, y + height - radius, radius, color, 1);
    }

    private static void setupRender() {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableAlphaTest();
        RenderSystem.shadeModel(GL11.GL_SMOOTH);
    }

    private static void endRender() {
        tessellator.draw();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.enableAlphaTest();
    }

    public static void drawQuadrantCircle(MatrixStack matrixStack, float x, float y, float radius, Color color, int mode) {
        setupRender();
        BufferBuilder vertexConsumer = tessellator.getBuffer();
        Matrix4f matrix4f = matrixStack.peek().getModel();
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;
        vertexConsumer.begin(6, VertexFormats.POSITION_COLOR);
        vertexConsumer.vertex(matrix4f, x, y, 0).color(r, g, b, a).next();
        float baseAngle;
        switch (mode) {
            case 2: {
                baseAngle = (float) Math.PI / 2;
                break;
            }
            case 3: {
                baseAngle = (float) Math.PI;
                break;
            }
            case 4: {
                baseAngle = (float) (3 * Math.PI / 2);
                break;
            }
            default: {
                baseAngle = 0;
                break;
            }
        }
        double angleStep = 0.10471975511965977D;
        for (int i = 0; i <= 15; i++) {
            float angle = baseAngle + (float) (angleStep * i);
            float x1 = (float) (x + radius * Math.sin(angle));
            float y1 = (float) (y + radius * Math.cos(angle));
            vertexConsumer.vertex(matrix4f, x1, y1, 0).color(r, g, b, a).next();
        }
        endRender();
    }
}
