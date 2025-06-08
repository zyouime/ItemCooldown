package me.zyouime.itemcooldown.screen.widget;

import me.zyouime.itemcooldown.screen.widget.element.ItemCustomElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;

import java.awt.*;

public class ItemsListWidget extends EntryListWidget<ItemsListWidget.Elements> {

    public ItemsListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.setShaderColor(0.125f, 0.125f, 0.125f, 1.0f);
        context.drawTexture(Screen.OPTIONS_BACKGROUND_TEXTURE, this.left, this.top, this.right, this.bottom + (int)this.getScrollAmount(), this.right - this.left, this.bottom - this.top, 32, 32);
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        context.enableScissor(left, top, right, bottom);
        this.renderList(context, mouseX, mouseY, delta);
        context.disableScissor();
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {}

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
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            listElement.updatePos(x, y);
            this.top = y;
            this.height = entryHeight;
            this.width = context.getScaledWindowWidth();
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
            context.fill(0, y, width, y + entryHeight, color.getRGB());
            listElement.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return mouseX >= 0 && mouseX <= width && mouseY >= top && mouseY <= top + height;
        }
    }
}
