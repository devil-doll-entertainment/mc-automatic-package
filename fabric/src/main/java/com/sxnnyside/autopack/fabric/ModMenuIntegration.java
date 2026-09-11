package com.sxnnyside.autopack.fabric;

import com.sxnnyside.autopack.gui.PackSelectorScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return PackSelectorScreen::new;
    }
}
