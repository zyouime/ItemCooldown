package me.zyouime.itemcooldown.setting;

import com.google.common.reflect.TypeToken;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;

public class ItemsSetting extends Setting<Map<ConfigData.Category, List<AbstractItemCooldown>>> {
    public ItemsSetting(String configKey) {
        super(configKey, new TypeToken<Map<ConfigData.Category, List<AbstractItemCooldown>>>() {}.getType());
    }

    public void updatePos(ConfigData.Category category, AbstractItemCooldown abstractItemCooldown, float x, float y) {
        List<AbstractItemCooldown> configItems = this.getValue().get(category);
        for (AbstractItemCooldown item : configItems) {
            if (ItemStack.areItemsEqual(item.getItem(), abstractItemCooldown.getItem())) {
                item.updatePos(x, y);
                break;
            }
        }
        this.save();
    }
}
