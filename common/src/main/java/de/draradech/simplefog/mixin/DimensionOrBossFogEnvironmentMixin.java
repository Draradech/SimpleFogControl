package de.draradech.simplefog.mixin;

import de.draradech.simplefog.SimpleFogMain;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.DimensionOrBossFogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DimensionOrBossFogEnvironment.class)
public class DimensionOrBossFogEnvironmentMixin {
    @Inject(at = @At("TAIL"), method = "setupFog(Lnet/minecraft/client/renderer/fog/FogData;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/client/multiplayer/ClientLevel;FLnet/minecraft/client/DeltaTracker;)V")
    public void tailSetupFog(FogData fogData, Entity entity, BlockPos blockPos, ClientLevel clientLevel, float viewDistance, DeltaTracker deltaTracker, CallbackInfo ci)
    {
        if (!SimpleFogMain.config.netherToggle) return;
        fogData.environmentalStart = viewDistance * SimpleFogMain.config.netherStart * 0.01f;
        fogData.environmentalEnd = viewDistance * SimpleFogMain.config.netherEnd * 0.01f;
        fogData.cloudEnd = fogData.environmentalEnd;
        fogData.skyEnd = fogData.environmentalEnd;
    }
}
