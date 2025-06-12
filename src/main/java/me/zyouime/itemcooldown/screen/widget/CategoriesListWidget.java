package me.zyouime.itemcooldown.screen.widget;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.screen.MainScreen;
import me.zyouime.itemcooldown.setting.CategorySetting;
import me.zyouime.itemcooldown.util.render.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.Objects;

public class CategoriesListWidget extends EntryListWidget<CategoriesListWidget.Elements> {

    private boolean open;
    private final MainScreen screen;
    private final ItemCooldown itemCooldown = ItemCooldown.getInstance();
    private final CategorySetting categorySetting = itemCooldown.settings.selectedCategory;

    public CategoriesListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight, MainScreen screen) {
        super(client, width, height, top, bottom, itemHeight);
        this.addEntry(new Elements(categorySetting.getValue()));
        this.screen = screen;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        RenderHelper.enableScissor(left, top, right, bottom);
        this.renderList(matrixStack, 0, 0, mouseX, mouseY, delta);
        RenderHelper.disableScissor();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Elements elements : this.children()) {
            if (elements.mouseClicked(mouseX, mouseY, button)) {
                open = !open;
                if (!open) {
                    categorySetting.setValue(elements.getCategory());
                    this.children().get(0).setCategory(categorySetting.getValue());
                    this.setScrollAmount(0);
                    this.screen.clearAndInit();
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
    protected void renderList(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
        int i = this.getRowLeft();
        int j = this.getRowWidth();
        int k = this.itemHeight - 4;
        int l = this.getEntryCount();
        for(int m = 0; m < (open ? l : 1); ++m) {
            int n = this.getRowTop(m);
            Elements entry = this.getEntry(m);
            int o = this.getRowBottom(m);
            if (m == 0) {
                entry.setColor(Color.GREEN);
            }
            if (o < this.top || n > this.bottom) continue;
            entry.render(matrices, m, n, i, j, k, mouseX, mouseY, this.isMouseOver(mouseX, mouseY) && Objects.equals(entry, entry), delta);
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

        private ConfigData.Category category;
        private int x, y, width, height;
        private Color color;

        public Elements(ConfigData.Category category) {
            this.category = category;
        }

        @Override
        public void render(MatrixStack matrixStack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.x = x;
            this.y = y;
            this.width = entryWidth;
            this.height = entryHeight;
            RenderHelper.drawRect(matrixStack, x, y, width, height, new Color(0, 0, 0, 128));
            RenderHelper.drawCenteredXYText(matrixStack, x + entryWidth  / 2f - 1, y + (entryHeight / 2f) - 8f, 0.9f, category.name, color == null ? Color.WHITE : color);

        }

        public void setColor(Color color) {
            this.color = color;
        }

        public ConfigData.Category getCategory() {
            return category;
        }

        public void setCategory(ConfigData.Category category) {
            this.category = category;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
    }
}

