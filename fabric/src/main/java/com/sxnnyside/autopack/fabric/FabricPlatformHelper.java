package com.sxnnyside.autopack.fabric;

import com.sxnnyside.autopack.platform.PlatformHelper;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformHelper implements PlatformHelper {

    @Override
    public Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
