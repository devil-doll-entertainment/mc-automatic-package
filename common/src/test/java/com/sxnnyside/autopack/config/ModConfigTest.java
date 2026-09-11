package com.sxnnyside.autopack.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModConfigTest {

    @BeforeEach
    void setUp() {
        ModConfig.load();
    }

    @Test
    void testDefaultValues() {
        ModConfig config = ModConfig.get();
        assertNotNull(config);
        assertEquals("", config.getTargetPackId());
        assertFalse(config.hasTargetPack());
        assertTrue(config.isPlayToggleSound());
        assertTrue(config.isNotifyActionBar());
    }

    @Test
    void testSetTargetPack() {
        ModConfig config = ModConfig.get();
        config.setTargetPack("file/test_pack.zip", "Test Pack");

        assertEquals("file/test_pack.zip", config.getTargetPackId());
        assertEquals("Test Pack", config.getTargetPackDisplayName());
        assertTrue(config.hasTargetPack());

        // Reset
        config.setTargetPack("", "");
        assertFalse(config.hasTargetPack());
    }

    @Test
    void testToggleOptions() {
        ModConfig config = ModConfig.get();
        config.setPlayToggleSound(false);
        assertFalse(config.isPlayToggleSound());
        config.setPlayToggleSound(true);
        assertTrue(config.isPlayToggleSound());

        config.setNotifyActionBar(false);
        assertFalse(config.isNotifyActionBar());
        config.setNotifyActionBar(true);
        assertTrue(config.isNotifyActionBar());
    }
}
