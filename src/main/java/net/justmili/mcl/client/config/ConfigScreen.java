package net.justmili.mcl.client.config;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Component.translatable("corelibs.screen.config.title"));
        this.parent = parent;
    }

    @Override
    public void onClose() {
        if (minecraft != null) minecraft.setScreen(parent);
    }
}