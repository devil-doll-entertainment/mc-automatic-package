package com.sxnnyside.autopack.neoforge;

import com.sxnnyside.autopack.platform.PlatformHelper;
import java.nio.file.Path;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;

public class NeoForgePlatformHelper implements PlatformHelper {

    @Override
    public Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
