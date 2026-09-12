package com.sxnnyside.autopack.neoforge;

import com.sxnnyside.autopack.platform.PlatformHelper;
import java.nio.file.Path;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

public class NeoForgePlatformHelper implements PlatformHelper {

    @Override
    public @NotNull Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public @NotNull String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(@NotNull String modId) {
        return ModList.get().isLoaded(modId);
    }
}
