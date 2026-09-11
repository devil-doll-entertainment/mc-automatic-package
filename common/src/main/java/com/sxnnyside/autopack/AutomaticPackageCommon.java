package com.sxnnyside.autopack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common entry points and constants.
 */
public final class AutomaticPackageCommon {

    public static final String MOD_ID = "automaticpackage";
    public static final String MOD_NAME = "MC-AutomaticPackage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private AutomaticPackageCommon() {}

    public static void init() {
        LOGGER.info("[{}] Initializing common logic...", MOD_NAME);
    }
}
