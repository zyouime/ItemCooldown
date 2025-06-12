package me.zyouime.itemcooldown.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import me.zyouime.itemcooldown.screen.widget.element.ItemCustomElement;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.Objects;

public class ItemsListWidget extends EntryListWidget<ItemsListWidget.Elements> {

    public ItemsListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        this.client.getTextureManager().bindTexture(DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
        RenderSystem.color4f(0.125F, 0.125F, 0.125F, 0.125F);
        float f = 32.0F;
        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex((double)this.left, (double)this.bottom, 0.0).texture((float)this.left / 32.0F, (float)(this.bottom + (int)this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).next();
        bufferBuilder.vertex((double)this.right, (double)this.bottom, 0.0).texture((float)this.right / 32.0F, (float)(this.bottom + (int)this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).next();
        bufferBuilder.vertex((double)this.right, (double)this.top, 0.0).texture((float)this.right / 32.0F, (float)(this.top + (int)this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).next();
        bufferBuilder.vertex((double)this.left, (double)this.top, 0.0).texture((float)this.left / 32.0F, (float)(this.top + (int)this.getScrollAmount()) / 32.0F).color(32, 32, 32, 255).next();
        tessellator.draw();
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        int k = this.getRowLeft();
        int l = this.top + 4 - (int)this.getScrollAmount();
        RenderHelper.enableScissor(left, top, right, bottom);
        this.renderList(matrixStack, k, l, mouseX, mouseY, delta);
        RenderHelper.disableScissor();
    }

    @Override
    protected void renderList(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
        int i = this.getRowLeft();
        int j = this.getRowWidth();
        int k = this.itemHeight - 4;
        int l = this.getEntryCount();
        for(int m = 0; m < l; ++m) {
            int n = this.getRowTop(m);
            int o = this.getRowBottom(m);
            if (o >= this.top && n <= this.bottom) {
                Elements entry = this.getEntry(m);
                entry.render(matrices, m, n, i, j, k, mouseX, mouseY, this.isMouseOver((double)mouseX, (double)mouseY) && Objects.equals(entry, entry), delta);
            }
        }
    }

    private int getRowBottom(int index) {
        return this.getRowTop(index) + this.itemHeight;
    }

    @Override
    public int addEntry(Elements entry) {
        return super.addEntry(entry);
    }

    public static class Elements extends EntryListWidget.Entry<Elements> {

        private final ItemCustomElement listElement;
        private int currentAlpha;
        private final MinecraftClient client = MinecraftClient.getInstance();
        private int top;
        private int width;
        private int height;

        public Elements(ItemCustomElement listElement) {
            this.listElement = listElement;
        }

        @Override
        public void render(MatrixStack matrixStack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            listElement.updatePos(x, y);
            this.top = y;
            this.height = entryHeight;
            this.width = MinecraftClient.getInstance().getWindow().getScaledWidth();
            int animationSpeed = (client.world == null) ? 4 : 1;
            if (this.isMouseOver(mouseX, mouseY)) {
                int maxAlpha = 80;
                if (currentAlpha < maxAlpha) {
                    currentAlpha += animationSpeed;
                    if (currentAlpha > maxAlpha) {
                        currentAlpha = maxAlpha;
                    }
                }
            } else {
                int minAlpha = 0;
                if (currentAlpha > minAlpha) {
                    currentAlpha -= animationSpeed;
                    if (currentAlpha < minAlpha) {
                        currentAlpha = minAlpha;
                    }
                }
            }
            Color color = new Color(82, 82, 82, currentAlpha);
            RenderHelper.drawRect(matrixStack, 0, y, width, entryHeight, color);
            listElement.render(matrixStack, mouseX, mouseY, tickDelta);
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return mouseX >= 0 && mouseX <= width && mouseY >= top && mouseY <= top + height;
        }
    }
}
