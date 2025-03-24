package me.zyouime.itemcooldown.screen;

import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ItemCooldownScreen extends Screen {

    private final Screen parent;
    private final AbstractItemCooldown item;

    public ItemCooldownScreen(Screen parent, AbstractItemCooldown item) {
        super(Text.empty());
        this.parent = parent;
        this.item = item;
    }
}
