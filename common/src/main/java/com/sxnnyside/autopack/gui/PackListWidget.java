package com.sxnnyside.autopack.gui;

import com.sxnnyside.autopack.config.ModConfig;
import com.sxnnyside.autopack.pack.PackInfo;
import com.sxnnyside.autopack.pack.ResourcePackController;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;

public class PackListWidget extends ObjectSelectionList<PackListWidget.PackEntry> {

    private final PackSelectorScreen parent;

    public PackListWidget(PackSelectorScreen parent, Minecraft client, int width, int height, int y, int itemHeight) {
        super(client, width, height, y, itemHeight);
        this.parent = parent;
    }

    public void setPacks(List<PackInfo> packs, String filter) {
        this.clearEntries();
        String lowerFilter = filter != null ? filter.trim().toLowerCase() : "";

        for (PackInfo pack : packs) {
            // Exclude non-removable core/vanilla packs from quick-toggle target selection
            if (pack.isRequired()) {
                continue;
            }

            if (!lowerFilter.isEmpty()) {
                boolean matchesTitle = pack.getPlainTitle().toLowerCase().contains(lowerFilter);
                boolean matchesId = pack.id().toLowerCase().contains(lowerFilter);
                if (!matchesTitle && !matchesId) {
                    continue;
                }
            }

            this.addEntry(new PackEntry(pack));
        }
    }

    @Override
    public int getRowWidth() {
        return Math.min(this.width - 24, 380);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getRowLeft() + this.getRowWidth() + 6;
    }

    public class PackEntry extends ObjectSelectionList.Entry<PackEntry> {
        private final PackInfo packInfo;
        private final Button toggleButton;
        private final Button setTargetButton;

        public PackEntry(PackInfo packInfo) {
            this.packInfo = packInfo;

            this.toggleButton = Button.builder(
                            Component.translatable(
                                    packInfo.isEnabled()
                                            ? "gui.automaticpackage.disable"
                                            : "gui.automaticpackage.enable"),
                            btn -> {
                                ResourcePackController.togglePack(minecraft, packInfo.id());
                                parent.refreshList();
                            })
                    .bounds(0, 0, 56, 18)
                    .build();

            boolean isCurrentTarget = packInfo.isTarget();
            this.setTargetButton = Button.builder(
                            Component.translatable(
                                    isCurrentTarget
                                            ? "gui.automaticpackage.is_target"
                                            : "gui.automaticpackage.set_target"),
                            btn -> {
                                ModConfig.get().setTargetPack(packInfo.id(), packInfo.getPlainTitle());
                                parent.refreshList();
                            })
                    .bounds(0, 0, 72, 18)
                    .build();

            if (isCurrentTarget) {
                this.setTargetButton.active = false;
            }
        }

        @Override
        public void render(
                GuiGraphics guiGraphics,
                int index,
                int top,
                int left,
                int width,
                int height,
                int mouseX,
                int mouseY,
                boolean hovering,
                float partialTick) {
            Font font = minecraft.font;

            // Highlight background on hover
            if (hovering) {
                guiGraphics.fill(left, top, left + width, top + height, 0x22FFFFFF);
            }

            // Pack Title
            Component titleComp = packInfo.title();
            guiGraphics.drawString(font, titleComp, left + 4, top + 4, 0xFFFFFF, false);

            // Pack Subtitle (ID or status)
            Component statusComp;
            if (packInfo.isEnabled()) {
                statusComp = Component.translatable("gui.automaticpackage.status_active")
                        .withStyle(ChatFormatting.GREEN);
            } else {
                statusComp = Component.translatable("gui.automaticpackage.status_inactive")
                        .withStyle(ChatFormatting.DARK_GRAY);
            }

            if (packInfo.isTarget()) {
                Component targetBadge = Component.translatable("gui.automaticpackage.quick_toggle_badge")
                        .withStyle(ChatFormatting.GOLD);
                guiGraphics.drawString(font, targetBadge, left + 4, top + 17, 0xFFAA00, false);
                guiGraphics.drawString(font, statusComp, left + 110, top + 17, 0xAAAAAA, false);
            } else {
                guiGraphics.drawString(font, statusComp, left + 4, top + 17, 0xAAAAAA, false);
            }

            // Position and render buttons
            int rightX = left + width - 4;
            this.toggleButton.setX(rightX - 56);
            this.toggleButton.setY(top + (height - 18) / 2);
            this.toggleButton.render(guiGraphics, mouseX, mouseY, partialTick);

            this.setTargetButton.setX(rightX - 56 - 4 - 72);
            this.setTargetButton.setY(top + (height - 18) / 2);
            this.setTargetButton.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.toggleButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
            if (this.setTargetButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public Component getNarration() {
            return packInfo.title();
        }
    }
}
