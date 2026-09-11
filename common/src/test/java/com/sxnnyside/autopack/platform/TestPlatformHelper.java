package com.sxnnyside.autopack.platform;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestPlatformHelper implements PlatformHelper {

    @Override
    public Path getConfigDirectory() {
        return Paths.get("build", "test-config");
    }

    @Override
    public String getPlatformName() {
        return "Test";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return false;
    }
}
