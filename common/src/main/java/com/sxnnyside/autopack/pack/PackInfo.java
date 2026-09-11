package com.sxnnyside.autopack.pack;

import net.minecraft.network.chat.Component;

/**
 * Encapsulates presentation data for a resource pack.
 */
public record PackInfo(
        String id, Component title, Component description, boolean isEnabled, boolean isTarget, boolean isRequired) {
    public String getPlainTitle() {
        return title != null ? title.getString() : id;
    }
}
