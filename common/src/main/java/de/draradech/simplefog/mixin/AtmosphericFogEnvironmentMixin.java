package de.draradech.simplefog.mixin;

import de.draradech.simplefog.SimpleFogConfig;
import de.draradech.simplefog.SimpleFogMain;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AtmosphericFogEnvironment.class)
public class AtmosphericFogEnvironmentMixin {
    private static float currentRainFactor = 0.0f;
    private static float currentIndoorFactor = 0.0f;
    private static float approach(float current, float target, float step) {
        if (Float.isNaN(current)) return target;
        return current < target ? Math.min(target, current + step) : Math.max(target, current - step);
    }

    private static float voxyEnd(float start, float density, float viewDistance)
    {
        float voxyFarDistance = (float)Math.sqrt(3) * Math.max(viewDistance, 20 * 16.0f);
        float intervalToFar = voxyFarDistance - start;
        float intervalToEnd = intervalToFar / (density * 0.01f);
        return start + intervalToEnd;
    }

    @Inject(at = @At("TAIL"), method = "setupFog(Lnet/minecraft/client/renderer/fog/FogData;Lnet/minecraft/client/Camera;Lnet/minecraft/client/multiplayer/ClientLevel;FLnet/minecraft/client/DeltaTracker;)V")
    public void tailSetupFog(FogData fogData, Camera camera, ClientLevel clientLevel, float viewDistance, DeltaTracker deltaTracker, CallbackInfo ci)
    {
        SimpleFogConfig config = SimpleFogMain.config;
        if (clientLevel.dimension() == Level.NETHER)
        {
            if (config.netherToggle)
            {
                fogData.environmentalStart = viewDistance * config.netherStart * 0.01f;
                float netherEnd = viewDistance * config.netherEnd * 0.01f;
                if (config.netherEndVoxy > 0.5f) netherEnd = voxyEnd(fogData.environmentalStart, config.netherEndVoxy, viewDistance);
                fogData.environmentalEnd = netherEnd;
                fogData.cloudEnd = fogData.environmentalEnd;
                fogData.skyEnd = fogData.environmentalEnd;
            }
        }
        else if (clientLevel.dimension() == Level.END)
        {
            if (config.endToggle)
            {
                fogData.environmentalStart = viewDistance * config.endStart * 0.01f;
                float endEnd = viewDistance * config.endEnd * 0.01f;
                if (config.endEndVoxy > 0.5f) endEnd = voxyEnd(fogData.environmentalStart, config.endEndVoxy, viewDistance);
                fogData.environmentalEnd = endEnd;
                fogData.cloudEnd = fogData.environmentalEnd;
                fogData.skyEnd = fogData.environmentalEnd;
            }
        }
        else
        {
            if (config.overworldToggle)
            {
                // overworld and custom dimensions
                SimpleFogConfig.RainConfig rainConf = config.rainConfig;

                if (rainConf.rainToggle) {
                    BlockPos blockPos = camera.blockPosition();
                    boolean skylight = blockPos.getY() >= clientLevel.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos.getX(), blockPos.getZ());
                    float step = rainConf.rainFogApplySpeed * deltaTracker.getRealtimeDeltaTicks() * 0.01f;
                    currentRainFactor = approach(currentRainFactor, clientLevel.isRaining() ? 1.0f : 0.0f, step);
                    currentIndoorFactor = approach(currentIndoorFactor, skylight ? 0.0f : 1.0f, step);

                    float rainStartPerc = Mth.lerp(currentIndoorFactor, rainConf.rainStart, rainConf.rainStartIndoor);
                    float envStartPerc = Mth.lerp(currentRainFactor, config.overworldStart, rainStartPerc);
                    float cloudEndClear = Minecraft.getInstance().options.cloudRange().get() * 16.0f;
                    float cloudEndRain = cloudEndClear * rainConf.rainEnd / config.overworldEnd;
                    fogData.environmentalStart = viewDistance * envStartPerc * 0.01f;
                    fogData.cloudEnd = Mth.lerp(currentRainFactor, cloudEndClear, cloudEndRain);

                    float overworldEnd = viewDistance * config.overworldEnd * 0.01f;
                    if (config.overworldEndVoxy > 0.5f) overworldEnd = voxyEnd(fogData.environmentalStart, config.overworldEndVoxy, viewDistance);
                    float rainEnd = viewDistance * rainConf.rainEnd * 0.01f;
                    if (rainConf.rainEndVoxy > 0.5f) rainEnd = voxyEnd(fogData.environmentalStart, rainConf.rainEndVoxy, viewDistance);
                    fogData.environmentalEnd = Mth.lerp(currentRainFactor, overworldEnd, rainEnd);
                } else {
                    fogData.environmentalStart = viewDistance * config.overworldStart * 0.01f;
                    fogData.cloudEnd = Minecraft.getInstance().options.cloudRange().get() * 16.0f;
                    float overworldEnd = viewDistance * config.overworldEnd * 0.01f;
                    if (config.overworldEndVoxy > 0.5f) overworldEnd = voxyEnd(fogData.environmentalStart, config.overworldEndVoxy, viewDistance);
                    fogData.environmentalEnd = overworldEnd;
                }
                fogData.skyEnd = Math.min(fogData.environmentalEnd, viewDistance);
            }
        }
    }
}
