package com.particlecontrol.client;

import com.particlecontrol.config.ParticleControlConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ParticleControlClient implements ClientModInitializer {

    public static final String MOD_ID = "particlecontrol";

    @Override
    public void onInitializeClient() {
        ParticleControlConfig.load();
        System.out.println("[ParticleControl] Mod loaded! Hide own particles: "
                + ParticleControlConfig.getInstance().hideOwnParticles);
    }
}
