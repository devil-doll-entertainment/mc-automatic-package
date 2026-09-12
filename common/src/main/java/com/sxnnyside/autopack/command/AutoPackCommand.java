package com.sxnnyside.autopack.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.sxnnyside.autopack.config.ModConfig;
import com.sxnnyside.autopack.gui.PackSelectorScreen;
import com.sxnnyside.autopack.pack.PackInfo;
import com.sxnnyside.autopack.pack.ResourcePackController;
import com.sxnnyside.autopack.pack.ResourcePackFeedback;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Common client-side command handler for /autopack.
 */
public final class AutoPackCommand {

    private AutoPackCommand() {}

    public static <S extends SharedSuggestionProvider> void register(@NotNull CommandDispatcher<S> dispatcher) {
        LiteralArgumentBuilder<S> root = LiteralArgumentBuilder.<S>literal("autopack")
                .executes(context -> {
                    Minecraft client = Minecraft.getInstance();
                    client.tell(() -> client.setScreen(new PackSelectorScreen()));
                    return 1;
                })
                .then(LiteralArgumentBuilder.<S>literal("gui").executes(context -> {
                    Minecraft client = Minecraft.getInstance();
                    client.tell(() -> client.setScreen(new PackSelectorScreen()));
                    return 1;
                }))
                .then(LiteralArgumentBuilder.<S>literal("toggle").executes(context -> {
                    Minecraft client = Minecraft.getInstance();
                    client.tell(() -> {
                        ResourcePackController.ToggleResult result =
                                ResourcePackController.toggleConfiguredPack(client);
                        String packId = ModConfig.get().getTargetPackId();
                        ResourcePackFeedback.notifyToggleResult(client, result, packId);
                    });
                    return 1;
                }))
                .then(LiteralArgumentBuilder.<S>literal("status").executes(context -> {
                    Minecraft client = Minecraft.getInstance();
                    ModConfig config = ModConfig.get();
                    if (!config.hasTargetPack()) {
                        ResourcePackFeedback.sendFeedback(
                                client,
                                Component.translatable("message.automaticpackage.no_pack_configured")
                                        .withStyle(ChatFormatting.YELLOW));
                    } else {
                        String packId = config.getTargetPackId();
                        Component name = ResourcePackController.getPackDisplayName(client, packId);
                        boolean isEnabled = client.getResourcePackRepository()
                                .getSelectedIds()
                                .contains(packId);

                        Component status = isEnabled
                                ? Component.translatable("gui.automaticpackage.status_active")
                                        .withStyle(ChatFormatting.GREEN)
                                : Component.translatable("gui.automaticpackage.status_inactive")
                                        .withStyle(ChatFormatting.RED);

                        ResourcePackFeedback.sendFeedback(
                                client,
                                Component.translatable("message.automaticpackage.status_report", name, status)
                                        .withStyle(ChatFormatting.AQUA));
                    }
                    return 1;
                }))
                .then(LiteralArgumentBuilder.<S>literal("select")
                        .then(RequiredArgumentBuilder.<S, String>argument("pack", StringArgumentType.greedyString())
                                .suggests((context, builder) -> {
                                    Minecraft client = Minecraft.getInstance();
                                    List<PackInfo> packs = ResourcePackController.getAvailableUserPacks(client);
                                    return SharedSuggestionProvider.suggest(
                                            packs.stream().map(PackInfo::id), builder);
                                })
                                .executes(context -> {
                                    String packId = StringArgumentType.getString(context, "pack");
                                    Minecraft client = Minecraft.getInstance();
                                    Component displayName = ResourcePackController.getPackDisplayName(client, packId);

                                    ModConfig.get().setTargetPack(packId, displayName.getString());
                                    ResourcePackFeedback.sendFeedback(
                                            client,
                                            Component.translatable(
                                                            "message.automaticpackage.pack_selected", displayName)
                                                    .withStyle(ChatFormatting.GREEN));
                                    return 1;
                                })));

        dispatcher.register(root);
    }
}
