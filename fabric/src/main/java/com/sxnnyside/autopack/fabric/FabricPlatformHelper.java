package com.sxnnyside.autopack.fabric;

import com.sxnnyside.autopack.platform.PlatformHelper;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

public class FabricPlatformHelper implements PlatformHelper {

    @Override
    public @NotNull Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public @NotNull String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(@NotNull String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
