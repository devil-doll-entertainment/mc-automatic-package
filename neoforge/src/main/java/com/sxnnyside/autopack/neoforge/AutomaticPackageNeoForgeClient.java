package com.sxnnyside.autopack.neoforge;

import com.sxnnyside.autopack.gui.PackSelectorScreen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

/**
 * Encapsulates all client-only lifecycle events and UI extension points for NeoForge.
 * Kept in an isolated class to guarantee that client classes (Screens, KeyMappings)
 * are never loaded by the JVM on dedicated servers.
 */
public final class AutomaticPackageNeoForgeClient {

    private AutomaticPackageNeoForgeClient() {}

    public static void init(@NotNull IEventBus modBus, @NotNull ModContainer modContainer) {
        modContainer.registerExtensionPoint(
                IConfigScreenFactory.class, (container, parent) -> new PackSelectorScreen(parent));

        modBus.addListener(NeoForgeClientEvents::onRegisterKeyMappings);
        NeoForge.EVENT_BUS.addListener(NeoForgeClientEvents::onClientTick);
        NeoForge.EVENT_BUS.addListener(NeoForgeClientEvents::onRegisterClientCommands);
    }
}
