package com.sxnnyside.autopack.pack;

import com.sxnnyside.autopack.config.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Handles presentation, player notifications (chat/action bar), and sound feedback
 * for resource pack operations.
 */
public final class ResourcePackFeedback {

    private ResourcePackFeedback() {}

    /**
     * Plays the appropriate UI feedback sound for a toggle event if audio feedback is enabled.
     */
    public static void playToggleSound(@NotNull Minecraft client, boolean wasEnabled) {
        if (!ModConfig.get().isPlayToggleSound()) {
            return;
        }

        client.getSoundManager()
                .play(SimpleSoundInstance.forUI(
                        wasEnabled ? SoundEvents.UI_BUTTON_CLICK.value() : SoundEvents.NOTE_BLOCK_CHIME.value(),
                        wasEnabled ? 0.9F : 1.2F));
    }

    /**
     * Sends formatted feedback to the player (action bar overlay or chat).
     */
    public static void sendFeedback(@NotNull Minecraft client, @NotNull Component message) {
        if (client.player != null) {
            boolean overlay = ModConfig.get().isNotifyActionBar();
            client.player.displayClientMessage(message, overlay);
        }
    }

    /**
     * Formats feedback message for toggle results with appropriate status styling and translations.
     */
    public static @NotNull Component formatResult(
            @NotNull Minecraft client, @NotNull ResourcePackController.ToggleResult result, @Nullable String packId) {
        Component packName = ResourcePackController.getPackDisplayName(client, packId);

        return switch (result) {
            case ENABLED -> Component.translatable("message.automaticpackage.pack_enabled", packName)
                    .withStyle(ChatFormatting.GREEN);
            case DISABLED -> Component.translatable("message.automaticpackage.pack_disabled", packName)
                    .withStyle(ChatFormatting.GOLD);
            case NOT_FOUND -> Component.translatable(
                            "message.automaticpackage.pack_not_found", packId != null ? packId : "")
                    .withStyle(ChatFormatting.RED);
            case NO_PACK_SELECTED -> Component.translatable("message.automaticpackage.no_pack_configured")
                    .withStyle(ChatFormatting.YELLOW);
            case COOLDOWN_ACTIVE -> Component.translatable("message.automaticpackage.cooldown_active")
                    .withStyle(ChatFormatting.RED);
        };
    }

    /**
     * Convenience method to format and send toggle result directly to the player.
     */
    public static void notifyToggleResult(
            @NotNull Minecraft client, @NotNull ResourcePackController.ToggleResult result, @Nullable String packId) {
        sendFeedback(client, formatResult(client, result, packId));
    }
}
