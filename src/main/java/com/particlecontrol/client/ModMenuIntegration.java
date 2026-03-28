package com.particlecontrol.client;

import com.particlecontrol.config.ParticleControlConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ParticleConfigScreen(parent);
    }

    @Environment(EnvType.CLIENT)
    public static class ParticleConfigScreen extends Screen {

        private final Screen parent;

        public ParticleConfigScreen(Screen parent) {
            super(Text.literal("Particle Control — Config"));
            this.parent = parent;
        }

        @Override
        protected void init() {
            ParticleControlConfig cfg = ParticleControlConfig.getInstance();
            int centerX = this.width / 2;
            int startY = 50;
            int spacing = 28;

            addToggleButton(centerX, startY, "Hide own particles: ", cfg.hideOwnParticles,
                    btn -> {
                        cfg.hideOwnParticles = !cfg.hideOwnParticles;
                        btn.setMessage(Text.literal("Hide own particles: " + onOff(cfg.hideOwnParticles)));
                    });

            addToggleButton(centerX, startY + spacing, "Show enemy particles: ", cfg.showEnemyParticles,
                    btn -> {
                        cfg.showEnemyParticles = !cfg.showEnemyParticles;
                        btn.setMessage(Text.literal("Show enemy particles: " + onOff(cfg.showEnemyParticles)));
                    });

            addToggleButton(centerX, startY + spacing * 2, "Hide environment particles: ", cfg.hideEnvironmentParticles,
                    btn -> {
                        cfg.hideEnvironmentParticles = !cfg.hideEnvironmentParticles;
                        btn.setMessage(Text.literal("Hide environment particles: " + onOff(cfg.hideEnvironmentParticles)));
                    });

            addDrawableChild(new FloatSlider(
                    centerX - 100, startY + spacing * 3, 200, 20,
                    "Enemy particle amount", cfg.enemyParticleMultiplier, 0f, 2f,
                    value -> cfg.enemyParticleMultiplier = value));

            addDrawableChild(new FloatSlider(
                    centerX - 100, startY + spacing * 4, 200, 20,
                    "Neutral mob particle amount", cfg.neutralParticleMultiplier, 0f, 2f,
                    value -> cfg.neutralParticleMultiplier = value));

            addDrawableChild(ButtonWidget.builder(
                    Text.literal("Done"),
                    btn -> {
                        ParticleControlConfig.save();
                        if (this.client != null) this.client.setScreen(parent);
                    })
                    .dimensions(centerX - 75, this.height - 35, 150, 20)
                    .build());
        }

        private void addToggleButton(int centerX, int y, String label, boolean initialValue,
                                     ButtonWidget.PressAction action) {
            addDrawableChild(ButtonWidget.builder(
                    Text.literal(label + onOff(initialValue)), action)
                    .dimensions(centerX - 100, y, 200, 20)
                    .build());
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            super.render(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(
                    this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        }

        @Override
        public void close() {
            ParticleControlConfig.save();
            if (this.client != null) this.client.setScreen(parent);
        }

        private static String onOff(boolean value) { return value ? "ON" : "OFF"; }
    }

    @Environment(EnvType.CLIENT)
    private static class FloatSlider extends SliderWidget {

        private final String label;
        private final float min;
        private final float max;
        private final java.util.function.Consumer<Float> onChange;

        public FloatSlider(int x, int y, int width, int height,
                           String label, float initial, float min, float max,
                           java.util.function.Consumer<Float> onChange) {
            super(x, y, width, height,
                    Text.literal(label + ": " + String.format("%.1f", initial) + "x"),
                    (double)(initial - min) / (max - min));
            this.label = label;
            this.min = min;
            this.max = max;
            this.onChange = onChange;
        }

        @Override
        protected void updateMessage() {
            float value = min + (float) this.value * (max - min);
            setMessage(Text.literal(label + ": " + String.format("%.1f", value) + "x"));
        }

        @Override
        protected void applyValue() {
            float value = min + (float) this.value * (max - min);
            onChange.accept(value);
        }
    }
}
