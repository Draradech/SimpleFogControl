package de.draradech.simplefog.mixin;

import de.draradech.simplefog.SimpleFogConfig;
import de.draradech.simplefog.SimpleFogMain;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AtmosphericFogEnvironment.class)
public class AtmosphericFogEnvironmentMixin {
    private static float currentFogStartPercent = Float.NaN;
    private static float currentFogEndPercent = Float.NaN;
    private static float approach(float current, float target, float step) {
        if (Float.isNaN(current)) return target;
        return current < target ? Math.min(target, current + step) : Math.max(target, current - step);
    }

    @Inject(at = @At("TAIL"), method = "setupFog(Lnet/minecraft/client/renderer/fog/FogData;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/client/multiplayer/ClientLevel;FLnet/minecraft/client/DeltaTracker;)V")
    public void tailSetupFog(FogData fogData, Entity entity, BlockPos blockPos, ClientLevel clientLevel, float viewDistance, DeltaTracker deltaTracker, CallbackInfo ci)
    {
        SimpleFogConfig.RainConfig rainConf = SimpleFogMain.config.rainConfig;
        if (!rainConf.rainToggle) return;

        // environment fog is spherical, while render distance fog is cylindrical. multiplier > sqrt(2) ensures no influence when not raining
        float targetFogStartPercent = SimpleFogMain.config.terrainStart * 1.5f;
        float targetFogEndPercent = SimpleFogMain.config.terrainEnd * 1.5f;

        if (entity.level().isRaining()) {
            boolean skylight = blockPos.getY() >= entity.level().getHeight(Heightmap.Types.WORLD_SURFACE, blockPos.getX(), blockPos.getZ());
            targetFogStartPercent = skylight ? rainConf.rainStart : rainConf.rainStartIndoor;
            targetFogEndPercent = rainConf.rainEnd;
        }

        if (currentFogStartPercent != targetFogStartPercent || currentFogEndPercent != targetFogEndPercent) {
            float applySpeed = rainConf.rainFogApplySpeed * deltaTracker.getRealtimeDeltaTicks();
            currentFogStartPercent = approach(currentFogStartPercent, targetFogStartPercent, applySpeed);
            currentFogEndPercent = approach(currentFogEndPercent, targetFogEndPercent, applySpeed);
        }

        fogData.environmentalStart = viewDistance * currentFogStartPercent * 0.01f;
        fogData.environmentalEnd = viewDistance * currentFogEndPercent * 0.01f;
        fogData.skyEnd = Math.min(fogData.environmentalEnd, viewDistance);
        float cloudEndClear = Minecraft.getInstance().options.cloudRange().get() * 16.0f;
        fogData.cloudEnd = rainConf.rainEnd + (cloudEndClear - rainConf.rainEnd) * ((fogData.environmentalEnd - rainConf.rainEnd) / Math.max(viewDistance * SimpleFogMain.config.terrainEnd * 0.015f - rainConf.rainEnd, 1.0f));
    }
}
