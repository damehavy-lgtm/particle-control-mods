package com.particlecontrol.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;

public class ParticleControlConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("particle-control.json");

    private static ParticleControlConfig INSTANCE = new ParticleControlConfig();

    public boolean hideOwnParticles = true;
    public boolean showEnemyParticles = true;
    public float enemyParticleMultiplier = 1.0f;
    public float neutralParticleMultiplier = 1.0f;
    public boolean hideEnvironmentParticles = false;

    public static ParticleControlConfig getInstance() {
        return INSTANCE;
    }

    public static void load() {
        File file = CONFIG_PATH.toFile();
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                ParticleControlConfig loaded = GSON.fromJson(reader, ParticleControlConfig.class);
                if (loaded != null) {
                    INSTANCE = loaded;
                    INSTANCE.clamp();
                }
            } catch (Exception e) {
                System.err.println("[ParticleControl] Failed to load config: " + e.getMessage());
                INSTANCE = new ParticleControlConfig();
            }
        } else {
            save();
        }
    }

    public static void save() {
        INSTANCE.clamp();
        try (Writer writer = new FileWriter(CONFIG_PATH.toFile())) {
            GSON.toJson(INSTANCE, writer);
        } catch (Exception e) {
            System.err.println("[ParticleControl] Failed to save config: " + e.getMessage());
        }
    }

    private void clamp() {
        enemyParticleMultiplier   = Math.max(0f, Math.min(2f, enemyParticleMultiplier));
        neutralParticleMultiplier = Math.max(0f, Math.min(2f, neutralParticleMultiplier));
    }
}
