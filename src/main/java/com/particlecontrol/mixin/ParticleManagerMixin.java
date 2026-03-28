package com.particlecontrol.mixin;

import com.particlecontrol.client.ParticleContext;
import com.particlecontrol.config.ParticleControlConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Environment(EnvType.CLIENT)
@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

    private static final Random RANDOM = new Random();

    @Inject(
        method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onAddParticle(ParticleEffect parameters,
                               double x, double y, double z,
                               double velocityX, double velocityY, double velocityZ,
                               CallbackInfoReturnable<Particle> cir) {

        ParticleControlConfig cfg = ParticleControlConfig.getInstance();
        MinecraftClient client = MinecraftClient.getInstance();
        ParticleContext.ParticleSource source = ParticleContext.classify(client);

        switch (source) {
            case SELF -> {
                if (cfg.hideOwnParticles) {
                    cir.setReturnValue(null);
                    return;
                }
            }
            case ENEMY -> {
                if (!cfg.showEnemyParticles) {
                    cir.setReturnValue(null);
                    return;
                }
                if (!shouldSpawn(cfg.enemyParticleMultiplier)) {
                    cir.setReturnValue(null);
                    return;
                }
            }
            case NEUTRAL -> {
                if (!shouldSpawn(cfg.neutralParticleMultiplier)) {
                    cir.setReturnValue(null);
                    return;
                }
            }
            case ENVIRONMENT -> {
                if (cfg.hideEnvironmentParticles) {
                    cir.setReturnValue(null);
                    return;
                }
            }
        }
    }

    private static boolean shouldSpawn(float multiplier) {
        if (multiplier <= 0f) return false;
        if (multiplier >= 1f) return true;
        return RANDOM.nextFloat() < multiplier;
    }
}
