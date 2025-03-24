package me.zyouime.itemcooldown.screen.widget;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.screen.widget.element.CategoryElement;
import me.zyouime.itemcooldown.util.Wrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;

import java.awt.*;

public class CategoriesListWidget extends EntryListWidget<CategoriesListWidget.Elements> implements Wrapper {

    private boolean open;

    public CategoriesListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
        this.addEntry(new Elements(new CategoryElement(ic.currentCategory)));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.enableScissor(left, top, right, bottom);
        this.renderList(context, mouseX, mouseY, delta);
        context.disableScissor();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Elements elements : this.children()) {
            if (elements.mouseClicked(mouseX, mouseY, button)) {
                open = !open;
                if (!open) {
                    ic.currentCategory = elements.category.getCategory();
                    this.children().get(0).category.setCategory(ic.currentCategory);
                    this.setScrollAmount(0);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected int getMaxPosition() {
        return open ? super.getMaxPosition() : this.itemHeight;
    }

    @Override
    protected void renderList(DrawContext context, int mouseX, int mouseY, float delta) {
        int i = this.left;
        int j = this.getRowWidth();
        int k = this.itemHeight - 4;
        int l = open ? this.getEntryCount() : 1;
        for (int m = 0; m < l; ++m) {
            int n = this.getRowTop(m);
            if (m == 0) {
                Elements entry = this.getEntry(m);
                entry.category.setColor(Color.GREEN);
            }
            int o = this.getRowBottom(m);
            if (o < this.top || n > this.bottom) continue;
            this.renderEntry(context, mouseX, mouseY, delta, m, i, n, j, k);
        }
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {}

    @Override
    public int addEntry(Elements entry) {
        return super.addEntry(entry);
    }

    public static class Elements extends EntryListWidget.Entry<Elements> {

        private final CategoryElement category;
        private int x, y, width, height;

        public Elements(CategoryElement category) {
            this.category = category;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            category.updatePos(x, y);
            this.x = x;
            this.y = y;
            this.width = entryWidth;
            this.height = entryHeight;
            context.fillGradient(x, y, x + entryWidth, y + entryHeight, -1072689136, -804253680);
            category.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
    }
}

