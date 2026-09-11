package com.sxnnyside.autopack.neoforge;

import com.sxnnyside.autopack.AutomaticPackageCommon;
import com.sxnnyside.autopack.config.ModConfig;
import com.sxnnyside.autopack.gui.PackSelectorScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(AutomaticPackageCommon.MOD_ID)
public class AutomaticPackageNeoForge {

    public AutomaticPackageNeoForge(IEventBus modBus, ModContainer modContainer) {
        AutomaticPackageCommon.init();
        ModConfig.load();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modContainer.registerExtensionPoint(
                    IConfigScreenFactory.class, (container, parent) -> new PackSelectorScreen(parent));

            modBus.addListener(NeoForgeClientEvents::onRegisterKeyMappings);
            NeoForge.EVENT_BUS.addListener(NeoForgeClientEvents::onClientTick);
            NeoForge.EVENT_BUS.addListener(NeoForgeClientEvents::onRegisterClientCommands);
        }

        AutomaticPackageCommon.LOGGER.info("[{}] NeoForge initialized.", AutomaticPackageCommon.MOD_NAME);
    }
}
