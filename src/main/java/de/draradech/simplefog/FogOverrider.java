package de.draradech.simplefog;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class FogOverrider {
    public static void overrideWaterFog(float viewDistance, Entity entity, CallbackInfoReturnable<FogParameters> info) {
        float fogStart, fogEnd;
        FogParameters parameters = info.getReturnValue();
        fogStart = viewDistance * SimpleFogMain.config.waterStart * 0.01f;
        fogEnd = viewDistance * SimpleFogMain.config.waterEnd * 0.01f;
        if (entity instanceof LocalPlayer localPlayer) {
            Holder<Biome> biomeHolder = localPlayer.level().getBiome(localPlayer.blockPosition());
            if (biomeHolder.is(BiomeTags.HAS_CLOSER_WATER_FOG)) {
                fogEnd = viewDistance * SimpleFogMain.config.waterEndSwamp * 0.01f;
            }
            fogEnd *= Math.max(0.25f, localPlayer.getWaterVision());
        }
        info.setReturnValue(new FogParameters(fogStart, fogEnd, parameters.shape(), parameters.red(), parameters.green(), parameters.blue(), parameters.alpha()));
    }
    
    public static void overrideNetherFog(float viewDistance, CallbackInfoReturnable<FogParameters> info) {
        float fogStart, fogEnd;
        FogParameters parameters = info.getReturnValue();
        fogStart = viewDistance * SimpleFogMain.config.netherStart * 0.01f;
        fogEnd = viewDistance * SimpleFogMain.config.netherEnd * 0.01f;
        info.setReturnValue(new FogParameters(fogStart, fogEnd, parameters.shape(), parameters.red(), parameters.green(), parameters.blue(), parameters.alpha()));
    }
    
    private static float currentFogStartPercent = Float.NaN;
    private static float currentFogEndPercent = Float.NaN;
    private static double timeLast = 0;
    public static void overrideTerrainFog(float viewDistance, Entity entity, float partialTick, CallbackInfoReturnable<FogParameters> info) {
        float fogStart, fogEnd;
        FogParameters parameters = info.getReturnValue();
        
        float targetFogStartPercent = SimpleFogMain.config.terrainStart;
        float targetFogEndPercent = SimpleFogMain.config.terrainEnd;
        
        SimpleFogConfig.RainConfig rainConf = SimpleFogMain.config.rainConfig;
        if (rainConf.rainToggle && entity.level().isRaining()) {
            boolean skylight = entity.getEyeY() >= entity.level().getHeight(Heightmap.Types.WORLD_SURFACE, entity.getBlockX(), entity.getBlockZ());
            targetFogStartPercent = skylight ? rainConf.rainStart : rainConf.rainStartIndoor;
            targetFogEndPercent = rainConf.rainEnd;
        }
        
        if (Float.isNaN(currentFogStartPercent)) currentFogStartPercent = targetFogStartPercent;
        if (Float.isNaN(currentFogEndPercent)) currentFogEndPercent = targetFogEndPercent;
        
        if (currentFogStartPercent != targetFogStartPercent || currentFogEndPercent != targetFogEndPercent) {
            float delta = (float)((double)entity.tickCount + partialTick - timeLast);
            delta = Mth.clamp(delta, 0.0f, 1.0f);
            float applySpeed = rainConf.rainFogApplySpeed * delta;
            if (currentFogStartPercent < targetFogStartPercent) {
                currentFogStartPercent = Math.min(targetFogStartPercent, currentFogStartPercent + applySpeed);
            } else {
                currentFogStartPercent = Math.max(targetFogStartPercent, currentFogStartPercent - applySpeed);
            }

            if (currentFogEndPercent < targetFogEndPercent) {
                currentFogEndPercent = Math.min(targetFogEndPercent, currentFogEndPercent + applySpeed);
            } else {
                currentFogEndPercent = Math.max(targetFogEndPercent, currentFogEndPercent - applySpeed);
            }
        }
        timeLast = (double)entity.tickCount + partialTick;
        
        fogStart = viewDistance * currentFogStartPercent * 0.01f;
        fogEnd = viewDistance * currentFogEndPercent * 0.01f;
        info.setReturnValue(new FogParameters(fogStart, fogEnd, parameters.shape(), parameters.red(), parameters.green(), parameters.blue(), parameters.alpha()));
    }
}
