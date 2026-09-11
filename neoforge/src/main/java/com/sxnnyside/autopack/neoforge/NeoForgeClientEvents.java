package com.sxnnyside.autopack.neoforge;

import com.mojang.blaze3d.platform.InputConstants;
import com.sxnnyside.autopack.command.AutoPackCommand;
import com.sxnnyside.autopack.config.ModConfig;
import com.sxnnyside.autopack.gui.PackSelectorScreen;
import com.sxnnyside.autopack.pack.ResourcePackController;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public final class NeoForgeClientEvents {

    public static KeyMapping toggleKey;
    public static KeyMapping openGuiKey;

    private NeoForgeClientEvents() {}

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        toggleKey = new KeyMapping(
                "key.automaticpackage.toggle_pack",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.automaticpackage.main");
        event.register(toggleKey);

        openGuiKey = new KeyMapping(
                "key.automaticpackage.open_gui",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.automaticpackage.main");
        event.register(openGuiKey);
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) {
            return;
        }

        if (toggleKey != null) {
            while (toggleKey.consumeClick()) {
                ModConfig config = ModConfig.get();
                if (!config.hasTargetPack()) {
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
        }

        if (openGuiKey != null) {
            while (openGuiKey.consumeClick()) {
                client.setScreen(new PackSelectorScreen());
            }
        }
    }

    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        AutoPackCommand.register(event.getDispatcher());
    }
}
