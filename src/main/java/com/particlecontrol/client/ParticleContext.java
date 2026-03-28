package com.particlecontrol.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;

@Environment(EnvType.CLIENT)
public class ParticleContext {

    public enum ParticleSource {
        SELF, ENEMY, NEUTRAL, ENVIRONMENT
    }

    private static final ThreadLocal<Entity> CURRENT_ENTITY = new ThreadLocal<>();

    public static void setEntity(Entity entity) { CURRENT_ENTITY.set(entity); }
    public static void clear() { CURRENT_ENTITY.remove(); }
    public static Entity getCurrentEntity() { return CURRENT_ENTITY.get(); }

    public static ParticleSource classify(net.minecraft.client.MinecraftClient client) {
        Entity entity = CURRENT_ENTITY.get();
        if (entity == null) return ParticleSource.ENVIRONMENT;
        if (client.player != null && entity == client.player) return ParticleSource.SELF;
        if (entity instanceof HostileEntity) return ParticleSource.ENEMY;
        if (entity instanceof PlayerEntity) return ParticleSource.ENEMY;
        return ParticleSource.NEUTRAL;
    }
}
