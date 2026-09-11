package com.sxnnyside.autopack.platform;

import java.util.ServiceLoader;

/**
 * Service locator to load platform-specific implementations via Java's ServiceLoader.
 */
public final class Services {

    public static final PlatformHelper PLATFORM = load(PlatformHelper.class);

    private Services() {}

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        return loadedService;
    }
}
