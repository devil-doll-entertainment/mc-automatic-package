package com.sxnnyside.autopack.neoforge;

import com.sxnnyside.autopack.AutomaticPackageCommon;
import com.sxnnyside.autopack.config.ModConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

@Mod(AutomaticPackageCommon.MOD_ID)
public class AutomaticPackageNeoForge {

    public AutomaticPackageNeoForge(@NotNull IEventBus modBus, @NotNull ModContainer modContainer) {
        AutomaticPackageCommon.init();
        ModConfig.load();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            AutomaticPackageNeoForgeClient.init(modBus, modContainer);
        }

        AutomaticPackageCommon.LOGGER.info("[{}] NeoForge initialized.", AutomaticPackageCommon.MOD_NAME);
    }
}
