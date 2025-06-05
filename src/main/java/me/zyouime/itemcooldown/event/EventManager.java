package me.zyouime.itemcooldown.event;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.mixin.BossBarHudAccessor;
import me.zyouime.itemcooldown.screen.MainScreen;
import me.zyouime.itemcooldown.util.CooldownManager;
import me.zyouime.itemcooldown.util.NbtHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EventManager {

    private static final ItemCooldown itemCooldown = ItemCooldown.getInstance();
    private static final String[] PVP_TYPES = new String[]{"режим боя", "пвп", "pvp"};
    private static final Map<ConfigData.Category, List<AbstractItemCooldown>> items = itemCooldown.settings.items.getValue();
    private static final ConfigData.Category selectedCategory = itemCooldown.settings.selectedCategory.getValue();

    private static void hudRenderEvent() {
        HudRenderCallback.EVENT.register(((drawContext, tickDelta) -> {
            if (items.get(selectedCategory) == null) return;
            if (!itemCooldown.settings.enabled.getValue()) return;
            for (AbstractItemCooldown item : items.get(selectedCategory)) {
                if (item.getCooldown() > 0) {
                    item.render(drawContext);
                }
            }
        }));
    }

    private static void tickEvent() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (itemCooldown.OPEN_SETTINGS.wasPressed()) {
                client.setScreen(new MainScreen(client.currentScreen));
            }
            if (items.get(selectedCategory) == null) return;
            for (AbstractItemCooldown item : items.get(selectedCategory)) {
                if (item.getCooldown() >= 0) item.tick();
                if (item.isResetWhenNoFightMode() && !isPvP()) {
                    item.setCooldown(0);
                }
            }
        });
    }

    private static void joinEvent() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (items.get(selectedCategory) == null) return;
            for (AbstractItemCooldown item : items.get(selectedCategory)) {
                item.setCooldown(0);
            }
        });
    }

    private static void useItemEvent() {
        UseItemCallback.EVENT.register(((player, world, hand) -> useItem(player, hand)));
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> useItem(player, hand).getResult()));
    }

    public static void registerEvents() {
        hudRenderEvent();
        tickEvent();
        joinEvent();
        useItemEvent();
    }

    public static boolean isPvP() {
        BossBarHudAccessor bossBar = (BossBarHudAccessor) MinecraftClient.getInstance().inGameHud.getBossBarHud();
        return bossBar.getBossBars().values().stream().anyMatch(boss ->
                Arrays.stream(PVP_TYPES).anyMatch(pvpType ->
                        boss.getName().getString().toLowerCase().contains(pvpType)));
    }

    private static TypedActionResult<ItemStack> useItem(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isFood() && !itemStack.isOf(Items.POTION) && !itemStack.isEmpty()) {
            CooldownManager.setCooldownIfNeeded(itemStack);
        }
        return TypedActionResult.pass(itemStack);
    }
}
