package com.sxnnyside.autopack.gui;

import com.sxnnyside.autopack.pack.PackInfo;
import com.sxnnyside.autopack.pack.ResourcePackController;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

/**
 * Clean, interactive GUI screen allowing players to browse, filter,
 * toggle, and set their quick-toggle target resource pack.
 */
public class PackSelectorScreen extends Screen {

    private final Screen parent;
    private EditBox searchBox;
    private PackListWidget packListWidget;
    private List<PackInfo> cachedPacks;

    public PackSelectorScreen(Screen parent) {
        super(Component.translatable("gui.automaticpackage.title"));
        this.parent = parent;
    }

    public PackSelectorScreen() {
        this(null);
    }

    @Override
    protected void init() {
        super.init();

        int contentWidth = Math.min(this.width - 20, 380);

        // Search Bar at Top
        this.searchBox = new EditBox(
                this.font,
                (this.width - contentWidth) / 2,
                22,
                contentWidth,
                18,
                Component.translatable("gui.automaticpackage.search"));
        this.searchBox.setHint(Component.translatable("gui.automaticpackage.search_hint"));
        this.searchBox.setResponder(this::onSearchQueryChanged);
        this.addRenderableWidget(this.searchBox);

        // Scrollable Pack List
        int listTop = 44;
        int listBottom = this.height - 36;
        this.packListWidget = new PackListWidget(this, this.minecraft, this.width, listBottom - listTop, listTop, 36);
        this.addRenderableWidget(this.packListWidget);

        // Bottom action buttons: Open Folder & Done
        int btnWidth = 140;
        int gap = 8;
        int totalBtnWidth = (btnWidth * 2) + gap;
        int startX = (this.width - totalBtnWidth) / 2;
        int btnY = this.height - 28;

        this.addRenderableWidget(Button.builder(Component.translatable("gui.automaticpackage.open_folder"), btn -> {
                    if (this.minecraft != null) {
                        Util.getPlatform().openPath(this.minecraft.getResourcePackDirectory());
                    }
                })
                .bounds(startX, btnY, btnWidth, 20)
                .build());

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, btn -> this.onClose())
                .bounds(startX + btnWidth + gap, btnY, btnWidth, 20)
                .build());

        refreshList();
    }

    public void refreshList() {
        if (this.minecraft != null) {
            this.cachedPacks = ResourcePackController.getAvailableUserPacks(this.minecraft);
            String filter = this.searchBox != null ? this.searchBox.getValue() : "";
            this.packListWidget.setPacks(this.cachedPacks, filter);
        }
    }

    private void onSearchQueryChanged(String query) {
        if (this.cachedPacks != null && this.packListWidget != null) {
            this.packListWidget.setPacks(this.cachedPacks, query);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // Header Title
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 8, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }
}
