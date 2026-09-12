package com.sxnnyside.autopack.platform;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.jetbrains.annotations.NotNull;

public class TestPlatformHelper implements PlatformHelper {

    @Override
    public @NotNull Path getConfigDirectory() {
        return Paths.get("build", "test-config");
    }

    @Override
    public @NotNull String getPlatformName() {
        return "Test";
    }

    @Override
    public boolean isModLoaded(@NotNull String modId) {
        return false;
    }
}
