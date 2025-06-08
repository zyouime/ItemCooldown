package me.zyouime.itemcooldown.util.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.joml.Matrix4f;

import java.awt.*;

public class RenderHelper {

    private static final Tessellator tessellator = Tessellator.getInstance();
    public static final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    public static void drawRect(MatrixStack matrixStack, float x, float y, float width, float height, Color color) {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;
        BufferBuilder vertexConsumer = tessellator.getBuffer();
        vertexConsumer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        vertexConsumer.vertex(matrix4f, x, y, 0).color(r, g, b, a).next();
        vertexConsumer.vertex(matrix4f, x, y + height, 0).color(r, g, b, a).next();
        vertexConsumer.vertex(matrix4f,x + width, y + height, 0).color(r, g, b, a).next();
        vertexConsumer.vertex(matrix4f,x + width, y, 0).color(r, g, b, a).next();
        tessellator.draw();
        RenderSystem.disableBlend();
    }

    public static void drawCenteredXYText(DrawContext context, float x, float y, float scale, String text, Color color) {
        drawText(context, x - (textRenderer.getWidth(text) / 2f * scale), y + (textRenderer.fontHeight / 2f * scale), scale, text, color);
    }


    public static void drawCenteredYText(DrawContext context, float x, float y, float scale, String text, Color color) {
        drawText(context, x, y + (textRenderer.fontHeight / 2f * scale), scale, text, color);
    }

    public static void drawText(DrawContext context, float x, float y, float scale, String text, Color color) {
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        matrixStack.scale(scale, scale, scale);
        textRenderer.draw(text, x / scale, y / scale, color.getRGB(), false, matrixStack.peek().getPositionMatrix(), context.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL,0, 0xF000F0, textRenderer.isRightToLeft());
        context.draw();
        matrixStack.pop();
    }

    public static void drawItem(DrawContext context, ItemStack stack, float x, float y) {
        if (stack.isEmpty()) {
            return;
        }
        MatrixStack matrices = context.getMatrices();
        MinecraftClient client = MinecraftClient.getInstance();
        BakedModel bakedModel = client.getItemRenderer().getModel(stack, client.world, client.player, 0);
        matrices.push();
        matrices.translate(x + 8, y + 8, 150);
        try {
            matrices.multiplyPositionMatrix(new Matrix4f().scaling(1.0f, -1.0f, 1.0f));
            matrices.scale(16.0f, 16.0f, 16.0f);
            boolean bl = !bakedModel.isSideLit();
            if (bl) {
                DiffuseLighting.disableGuiDepthLighting();
            }
            client.getItemRenderer().renderItem(stack, ModelTransformationMode.GUI, false, matrices, context.getVertexConsumers(), 0xF000F0, OverlayTexture.DEFAULT_UV, bakedModel);
            context.draw();
            if (bl) {
                DiffuseLighting.enableGuiDepthLighting();
            }
        } catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Rendering item");
            CrashReportSection crashReportSection = crashReport.addElement("Item being rendered");
            crashReportSection.add("Item Type", () -> String.valueOf(stack.getItem()));
            crashReportSection.add("Item Damage", () -> String.valueOf(stack.getDamage()));
            crashReportSection.add("Item NBT", () -> String.valueOf(stack.getNbt()));
            crashReportSection.add("Item Foil", () -> String.valueOf(stack.hasGlint()));
            throw new CrashException(crashReport);
        }
        matrices.pop();
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

    public static void drawQuadrantCircle(MatrixStack matrixStack, float x, float y, float radius, Color color, int mode) {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexConsumer = tessellator.getBuffer();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;
        vertexConsumer.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        vertexConsumer.vertex(matrix4f, x, y, 0).color(r, g, b, a).next();
        float baseAngle = switch (mode) {
            case 2 -> (float) Math.PI / 2;
            case 3 -> (float) Math.PI;
            case 4 -> (float) (3 * Math.PI / 2);
            default -> 0;
        };
        double angleStep = 0.10471975511965977D;
        for (int i = 0; i <= 15; i++) {
            float angle = baseAngle + (float) (angleStep * i);
            float x1 = (float) (x + radius * Math.sin(angle));
            float y1 = (float) (y + radius * Math.cos(angle));
            vertexConsumer.vertex(matrix4f, x1, y1, 0).color(r, g, b, a).next();
        }
        tessellator.draw();
        RenderSystem.disableBlend();
    }
}
