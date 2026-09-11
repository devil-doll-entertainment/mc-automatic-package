package com.sxnnyside.autopack.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import com.sxnnyside.autopack.AutomaticPackageCommon;
import com.sxnnyside.autopack.command.AutoPackCommand;
import com.sxnnyside.autopack.config.ModConfig;
import com.sxnnyside.autopack.gui.PackSelectorScreen;
import com.sxnnyside.autopack.pack.ResourcePackController;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class AutomaticPackageFabric implements ClientModInitializer {

    private static KeyMapping toggleKey;
    private static KeyMapping openGuiKey;

    @Override
    public void onInitializeClient() {
        AutomaticPackageCommon.init();
        ModConfig.load();

        // Register Keybindings
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.automaticpackage.toggle_pack",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.automaticpackage.main"));

        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.automaticpackage.open_gui",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.automaticpackage.main"));

        // Client Tick for key handling
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);

        // Register /autopack client command
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            AutoPackCommand.register(dispatcher);
        });

        AutomaticPackageCommon.LOGGER.info("[{}] Fabric client ready.", AutomaticPackageCommon.MOD_NAME);
    }

    private void onClientTick(Minecraft client) {
        if (client.player == null) {
            return;
        }

        while (toggleKey.consumeClick()) {
            ModConfig config = ModConfig.get();
            if (!config.hasTargetPack()) {
                // Friendly UX: open GUI if no pack configured yet
                client.setScreen(new PackSelectorScreen());
                ResourcePackController.sendFeedback(
                        client, Component.translatable("message.automaticpackage.please_choose_pack"));
            } else {
                ResourcePackController.ToggleResult result = ResourcePackController.toggleConfiguredPack(client);
                String packId = config.getTargetPackId();
                ResourcePackController.sendFeedback(
                        client, ResourcePackController.formatResult(client, result, packId));
            }
        }

        while (openGuiKey.consumeClick()) {
            client.setScreen(new PackSelectorScreen());
        }
    }
}
