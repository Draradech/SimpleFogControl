package de.draradech.simplefog.mixin;

import de.draradech.simplefog.SimpleFogMain;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WaterFogEnvironment.class)
public class WaterFogEnvironmentMixin {
    @Inject(
            at = @At("TAIL"),
            method = "setupFog(Lnet/minecraft/client/renderer/fog/FogData;Lnet/minecraft/client/Camera;Lnet/minecraft/client/multiplayer/ClientLevel;FLnet/minecraft/client/DeltaTracker;)V"
    )
    private void tailSetupFog(FogData fogData, Camera camera, ClientLevel clientLevel, float viewDistance, DeltaTracker deltaTracker, CallbackInfo ci)
    {
        if (!SimpleFogMain.config.waterToggle) return;
        float vanillaEnd = camera.attributeProbe().getValue(EnvironmentAttributes.WATER_FOG_END_DISTANCE, deltaTracker.getGameTimeDeltaPartialTick(false));
        boolean hasCloserFog = vanillaEnd < 96.0f;
        fogData.environmentalStart = viewDistance * SimpleFogMain.config.waterStart * 0.01f;
        float targetEndPercent = hasCloserFog ? SimpleFogMain.config.waterEndSwamp : SimpleFogMain.config.waterEnd;
        fogData.environmentalEnd = viewDistance * targetEndPercent * 0.01f;
        Entity entity = camera.entity();
        if (entity instanceof LocalPlayer localPlayer) {
            fogData.environmentalEnd *= Math.max(0.25F, localPlayer.getWaterVision());
        }
        fogData.skyEnd = fogData.environmentalEnd;
        fogData.cloudEnd = fogData.environmentalEnd;
    }
}
