package com.particlecontrol.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ParticleContext {

    private static final ThreadLocal<Boolean> TICKING_LOCAL_PLAYER = 
        ThreadLocal.withInitial(() -> false);

    public static void setCurrentlyTickingLocalPlayer(boolean value) {
        TICKING_LOCAL_PLAYER.set(value);
    }

    public static boolean isLocalPlayer() {
        return TICKING_LOCAL_PLAYER.get();
    }
}
