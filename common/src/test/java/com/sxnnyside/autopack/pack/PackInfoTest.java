package com.sxnnyside.autopack.pack;

import static org.junit.jupiter.api.Assertions.*;

import net.minecraft.network.chat.Component;
import org.junit.jupiter.api.Test;

class PackInfoTest {

    @Test
    void testPackInfoProperties() {
        Component title = Component.literal("My Resource Pack");
        Component desc = Component.literal("A test description");

        PackInfo info = new PackInfo("file/my_pack.zip", title, desc, true, false, false);

        assertEquals("file/my_pack.zip", info.id());
        assertEquals(title, info.title());
        assertEquals(desc, info.description());
        assertTrue(info.isEnabled());
        assertFalse(info.isTarget());
        assertFalse(info.isRequired());
        assertEquals("My Resource Pack", info.getPlainTitle());
    }

    @Test
    void testPlainTitleFallback() {
        PackInfo fallbackInfo = new PackInfo("file/fallback.zip", null, null, false, false, false);
        assertEquals("file/fallback.zip", fallbackInfo.getPlainTitle());
    }

    @Test
    void testToggleResultValues() {
        assertNotNull(ResourcePackController.ToggleResult.valueOf("ENABLED"));
        assertNotNull(ResourcePackController.ToggleResult.valueOf("DISABLED"));
        assertNotNull(ResourcePackController.ToggleResult.valueOf("NOT_FOUND"));
        assertNotNull(ResourcePackController.ToggleResult.valueOf("NO_PACK_SELECTED"));
        assertNotNull(ResourcePackController.ToggleResult.valueOf("COOLDOWN_ACTIVE"));
    }
}
