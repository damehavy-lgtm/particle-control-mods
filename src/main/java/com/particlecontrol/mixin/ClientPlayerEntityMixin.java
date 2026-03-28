package com.particlecontrol.mixin;

import com.particlecontrol.client.ParticleContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void beforeTickMovement(CallbackInfo ci) {
        ParticleContext.setCurrentlyTickingLocalPlayer(true);
    }

    @Inject(method = "tickMovement", at = @At("RETURN"))
    private void afterTickMovement(CallbackInfo ci) {
        ParticleContext.setCurrentlyTickingLocalPlayer(false);
    }
}
