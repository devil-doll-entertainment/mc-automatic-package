package com.sxnnyside.autopack.keybind;

import com.sxnnyside.autopack.AutomaticPackageMod;
import com.sxnnyside.autopack.config.ModConfig;
import com.sxnnyside.autopack.pack.ResourcePackToggle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/**
 * Registers the toggle keybind and handles press events.
 *
 * <p>The keybind appears in the Minecraft controls menu under the
 * "MC-AutomaticPackage" category. Default key: <b>V</b>.</p>
 */
public final class KeybindHandler {

    private static KeyBinding toggleKey;

    private KeybindHandler() { }

    /**
     * Register the keybind and subscribe to client tick events.
     * Call once during mod initialization.
     */
    public static void register() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.automaticpackage.toggle_pack",   // translation key
                InputUtil.Type.KEYSYM,                 // keyboard key
                GLFW.GLFW_KEY_V,                       // default: V
                "category.automaticpackage.main"       // category translation key
        ));

        ClientTickEvents.END_CLIENT_TICK.register(KeybindHandler::onClientTick);
    }

    // ---- internal ----

    private static void onClientTick(MinecraftClient client) {
        while (toggleKey.wasPressed()) {
            if (client.player == null) {
                continue;
            }

            String packId = ModConfig.get().getResourcePackId();
            ResourcePackToggle.ToggleResult result = ResourcePackToggle.toggle(client, packId);

            switch (result) {
                case ENABLED -> {
                    client.player.sendMessage(
                            Text.translatable("message.automaticpackage.pack_enabled", packId),
                            true  // action-bar overlay
                    );
                    AutomaticPackageMod.LOGGER.info("Resource pack '{}' enabled.", packId);
                }
                case DISABLED -> {
                    client.player.sendMessage(
                            Text.translatable("message.automaticpackage.pack_disabled", packId),
                            true
                    );
                    AutomaticPackageMod.LOGGER.info("Resource pack '{}' disabled.", packId);
                }
                case NOT_FOUND -> {
                    client.player.sendMessage(
                            Text.translatable("message.automaticpackage.pack_not_found", packId),
                            true
                    );
                    AutomaticPackageMod.LOGGER.warn("Resource pack '{}' not found in available packs.", packId);
                }
            }
        }
    }
}
