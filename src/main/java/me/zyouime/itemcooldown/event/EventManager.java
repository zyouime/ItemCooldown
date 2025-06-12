package me.zyouime.itemcooldown.event;

import me.zyouime.itemcooldown.ItemCooldown;
import me.zyouime.itemcooldown.config.ConfigData;
import me.zyouime.itemcooldown.item.AbstractItemCooldown;
import me.zyouime.itemcooldown.mixin.BossBarHudAccessor;
import me.zyouime.itemcooldown.screen.MainScreen;
import me.zyouime.itemcooldown.util.UseItem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EventManager {

    private static final String[] PVP_TYPES = new String[]{"режим боя", "пвп", "pvp"};

    public static void registerEvents() {
        registerHudRenderEvent();
        registerTickEvent();
        registerJoinEvent();
        registerUseItemEvent();
    }

    private static void registerHudRenderEvent() {
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            Map<ConfigData.Category, List<AbstractItemCooldown>> items = ItemCooldown.getInstance().settings.items.getValue();
            ConfigData.Category selectedCategory = ItemCooldown.getInstance().settings.selectedCategory.getValue();
            if (!ItemCooldown.getInstance().settings.enabled.getValue()) return;
            if (items.get(selectedCategory) == null) return;
            for (AbstractItemCooldown item : items.get(selectedCategory)) {
                if (item.getCooldown() > 0) {
                    item.render(matrixStack);
                }
            }
        });
    }

    private static void registerTickEvent() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ItemCooldown.getInstance().OPEN_SETTINGS.wasPressed()) {
                client.openScreen(new MainScreen(client.currentScreen));
            }
            Map<ConfigData.Category, List<AbstractItemCooldown>> items = ItemCooldown.getInstance().settings.items.getValue();
            ConfigData.Category selectedCategory = ItemCooldown.getInstance().settings.selectedCategory.getValue();
            for (AbstractItemCooldown item : items.get(selectedCategory)) {
                if (item.getCooldown() >= 0) {
                    item.tick();
                }
                if (item.isResetWhenNoFightMode() && !isPvP()) {
                    item.setCooldown(0);
                }
            }
        });
    }

    private static void registerJoinEvent() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            Map<ConfigData.Category, List<AbstractItemCooldown>> items = ItemCooldown.getInstance().settings.items.getValue();
            ConfigData.Category selectedCategory = ItemCooldown.getInstance().settings.selectedCategory.getValue();
            List<AbstractItemCooldown> cooldownItems = items.get(selectedCategory);
            for (AbstractItemCooldown item : cooldownItems) {
                if (item.isResetWhenLeftTheServer()) {
                    item.setCooldown(0);
                }
            }
        });
    }

    private static void registerUseItemEvent() {
        UseItemCallback.EVENT.register((player, world, hand) -> useItem(player, hand));
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> useItem(player, hand).getResult());
    }

    public static boolean isPvP() {
        BossBarHudAccessor bossBar = (BossBarHudAccessor) MinecraftClient.getInstance().inGameHud.getBossBarHud();
        return bossBar.getBossBars().values().stream().anyMatch(boss -> Arrays.stream(PVP_TYPES).anyMatch(pvpType -> boss.getName().getString().toLowerCase().contains(pvpType)));
    }

    private static TypedActionResult<ItemStack> useItem(PlayerEntity player, Hand hand) {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        if (clientPlayer != null) {
            ItemStack itemStack = clientPlayer.getStackInHand(hand).copy();
            if (!itemStack.isFood() && itemStack.getItem() != Items.POTION && !itemStack.isEmpty()) {
                if (clientPlayer instanceof UseItem) {
                    ((UseItem) clientPlayer).setItem(itemStack);
                }
            }
        }
        return TypedActionResult.pass(player.getStackInHand(hand));
    }
}
