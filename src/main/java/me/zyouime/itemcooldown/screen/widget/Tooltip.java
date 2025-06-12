package me.zyouime.itemcooldown.screen.widget;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public interface Tooltip {

    void render(ClickableWidget widget, MatrixStack matrixStack, int mouseX, int mouseY);

    default List<Text> transformTooltip(String tooltip) {
        List<Text> tooltipList = new ArrayList<>();
        for (String lines : tooltip.split("\n")) {
            tooltipList.add(Text.of(lines));
        }
        return tooltipList;
    }
}
