package com.particlecontrol.mixin;

import com.particlecontrol.client.ParticleContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void beforeRenderEntity(Entity entity,
                                    double cameraX, double cameraY, double cameraZ,
                                    float tickProgress,
                                    net.minecraft.client.util.math.MatrixStack matrices,
                                    net.minecraft.client.render.VertexConsumerProvider vertexConsumers,
                                    CallbackInfo ci) {
        ParticleContext.setEntity(entity);
    }

    @Inject(method = "renderEntity", at = @At("RETURN"))
    private void afterRenderEntity(Entity entity,
                                   double cameraX, double cameraY, double cameraZ,
                                   float tickProgress,
                                   net.minecraft.client.util.math.MatrixStack matrices,
                                   net.minecraft.client.render.VertexConsumerProvider vertexConsumers,
                                   CallbackInfo ci) {
        ParticleContext.clear();
    }

    @Inject(method = "tickEntity(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"))
    private void beforeTickEntity(Entity entity, CallbackInfo ci) {
        ParticleContext.setEntity(entity);
    }

    @Inject(method = "tickEntity(Lnet/minecraft/entity/Entity;)V", at = @At("RETURN"))
    private void afterTickEntity(Entity entity, CallbackInfo ci) {
        ParticleContext.clear();
    }
}
