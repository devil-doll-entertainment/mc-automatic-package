package com.sxnnyside.autopack.pack;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Encapsulates presentation data for a resource pack.
 */
public record PackInfo(
        @NotNull String id,
        @NotNull Component title,
        @Nullable Component description,
        boolean isEnabled,
        boolean isTarget,
        boolean isRequired) {
    public @NotNull String getPlainTitle() {
        return title != null ? title.getString() : id;
    }
}
