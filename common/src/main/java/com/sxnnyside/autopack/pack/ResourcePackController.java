package com.sxnnyside.autopack.pack;

import com.sxnnyside.autopack.AutomaticPackageCommon;
import com.sxnnyside.autopack.config.ModConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.sounds.SoundEvents;

/**
 * Controller for scanning, querying, and toggling client resource packs.
 */
public final class ResourcePackController {

    public enum ToggleResult {
        ENABLED,
        DISABLED,
        NOT_FOUND,
        NO_PACK_SELECTED,
        COOLDOWN_ACTIVE
    }

    private static final long RELOAD_COOLDOWN_MS = 2000;
    private static long lastToggleTimestamp = 0;

    private ResourcePackController() {}

    /**
     * Scans and returns all available user-configurable resource packs.
     */
    public static List<PackInfo> getAvailableUserPacks(Minecraft client) {
        PackRepository repository = client.getResourcePackRepository();
        repository.reload();

        Collection<Pack> available = repository.getAvailablePacks();
        Collection<Pack> selected = repository.getSelectedPacks();
        String currentTargetId = ModConfig.get().getTargetPackId();

        List<PackInfo> list = new ArrayList<>();
        for (Pack pack : available) {
            boolean isRequired = pack.isRequired();
            boolean isEnabled = selected.contains(pack);
            boolean isTarget = pack.getId().equals(currentTargetId);

            list.add(new PackInfo(
                    pack.getId(), pack.getTitle(), pack.getDescription(), isEnabled, isTarget, isRequired));
        }

        return list;
    }

    /**
     * Toggles the currently configured quick-toggle pack.
     */
    public static ToggleResult toggleConfiguredPack(Minecraft client) {
        ModConfig config = ModConfig.get();
        if (!config.hasTargetPack()) {
            return ToggleResult.NO_PACK_SELECTED;
        }

        return togglePack(client, config.getTargetPackId());
    }

    /**
     * Toggles any pack by its ID with anti-spam reload debouncing.
     */
    public static ToggleResult togglePack(Minecraft client, String packId) {
        if (packId == null || packId.trim().isEmpty()) {
            return ToggleResult.NO_PACK_SELECTED;
        }

        long now = System.currentTimeMillis();
        if (now - lastToggleTimestamp < RELOAD_COOLDOWN_MS) {
            AutomaticPackageCommon.LOGGER.debug("Resource reload blocked: cooldown active.");
            return ToggleResult.COOLDOWN_ACTIVE;
        }

        PackRepository repository = client.getResourcePackRepository();
        repository.reload();

        Optional<Pack> targetPackOpt = repository.getAvailablePacks().stream()
                .filter(p -> p.getId().equals(packId))
                .findFirst();

        if (targetPackOpt.isEmpty()) {
            AutomaticPackageCommon.LOGGER.warn("Resource pack '{}' not found.", packId);
            return ToggleResult.NOT_FOUND;
        }

        // Current selected list
        List<String> currentSelected = new ArrayList<>(repository.getSelectedIds());
        boolean wasEnabled = currentSelected.contains(packId);

        if (wasEnabled) {
            currentSelected.remove(packId);
        } else {
            // Stack Priority: Add to end so toggled utility pack takes highest precedence over other textures
            currentSelected.remove(packId);
            currentSelected.add(packId);
        }

        // Apply new set of selected packs
        repository.setSelected(currentSelected);

        // Sync with options.txt so changes survive client restart
        syncOptions(client, repository);

        // Record cooldown timestamp before triggering expensive reload
        lastToggleTimestamp = System.currentTimeMillis();

        // Play feedback sound if enabled
        if (ModConfig.get().isPlayToggleSound()) {
            client.getSoundManager()
                    .play(SimpleSoundInstance.forUI(
                            wasEnabled ? SoundEvents.UI_BUTTON_CLICK.value() : SoundEvents.NOTE_BLOCK_CHIME.value(),
                            wasEnabled ? 0.9F : 1.2F));
        }

        // Trigger full resource reload
        client.reloadResourcePacks();

        return wasEnabled ? ToggleResult.DISABLED : ToggleResult.ENABLED;
    }

    /**
     * Resolves human-friendly display name of a pack.
     */
    public static Component getPackDisplayName(Minecraft client, String packId) {
        PackRepository repository = client.getResourcePackRepository();
        Pack pack = repository.getPack(packId);
        if (pack != null) {
            return pack.getTitle();
        }

        String cached = ModConfig.get().getTargetPackDisplayName();
        if (cached != null && !cached.isEmpty()) {
            return Component.literal(cached);
        }

        return Component.literal(packId);
    }

    /**
     * Sends formatted feedback to the player (action bar or chat).
     */
    public static void sendFeedback(Minecraft client, Component message) {
        if (client.player != null) {
            boolean overlay = ModConfig.get().isNotifyActionBar();
            client.player.displayClientMessage(message, overlay);
        }
    }

    /**
     * Formats feedback message for toggle results.
     */
    public static Component formatResult(Minecraft client, ToggleResult result, String packId) {
        Component packName = getPackDisplayName(client, packId);

        return switch (result) {
            case ENABLED -> Component.translatable("message.automaticpackage.pack_enabled", packName)
                    .withStyle(ChatFormatting.GREEN);
            case DISABLED -> Component.translatable("message.automaticpackage.pack_disabled", packName)
                    .withStyle(ChatFormatting.GOLD);
            case NOT_FOUND -> Component.translatable("message.automaticpackage.pack_not_found", packId)
                    .withStyle(ChatFormatting.RED);
            case NO_PACK_SELECTED -> Component.translatable("message.automaticpackage.no_pack_configured")
                    .withStyle(ChatFormatting.YELLOW);
            case COOLDOWN_ACTIVE -> Component.translatable("message.automaticpackage.cooldown_active")
                    .withStyle(ChatFormatting.RED);
        };
    }

    private static void syncOptions(Minecraft client, PackRepository repository) {
        List<String> userSelected = new ArrayList<>();
        for (Pack pack : repository.getSelectedPacks()) {
            // Keep packs that are user-configurable (skip required/core packs)
            if (!pack.isRequired()) {
                userSelected.add(pack.getId());
            }
        }

        client.options.resourcePacks.clear();
        client.options.resourcePacks.addAll(userSelected);
        client.options.save();
    }
}
