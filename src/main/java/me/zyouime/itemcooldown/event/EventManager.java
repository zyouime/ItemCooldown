package me.zyouime.itemcooldown.event;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.config.ModConfig;
import me.zyouime.itemcooldown.item.ItemCooldownStatus;
import me.zyouime.itemcooldown.util.CooldownRenderable;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.lwjgl.glfw.GLFW;

import java.util.Iterator;
import java.util.List;

public class EventManager {

    private static final List<ItemCooldownStatus> cooldownItems = ItemCooldown.cooldownItems();

    private static void hudRenderEvent() {
        HudRenderCallback.EVENT.register(((drawContext, tickDelta) -> {
           // float offsetY = 0;
           // ConfigData configData = ModConfig.configData;
           // float renderX = (float) configData.getField("x");
           // float renderY = (float) configData.getField("y");
            for (ItemCooldownStatus item : cooldownItems) {
               // item.updatePos(renderX, renderY + offsetY);
                item.render(drawContext);
               // offsetY += 28;
            }
        }));
    }

    private static void tickEvent() {
//        ClientTickEvents.END_CLIENT_TICK.register(client -> cooldownItems.removeIf(ict -> {
//            ict.tick();
//            return ict.getCooldown() <= 0;
//        }));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            cooldownItems.forEach(ct -> {
                if (ct.getCooldown() > 0) {
                    ct.tick();
                }
            });
        });
    }

    private static void joinEvent() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> cooldownItems.clear());
    }

    public static void register() {
        hudRenderEvent();
        tickEvent();
        joinEvent();
    }
}
