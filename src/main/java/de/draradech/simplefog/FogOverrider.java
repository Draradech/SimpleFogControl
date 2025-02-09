package de.draradech.simplefog;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class FogOverrider
{
    public static float fogStart, fogEnd;
    public static int prevAge = 0;

    public static void overrideWaterFog(float viewDistance, Entity entity, CallbackInfoReturnable<FogParameters> info)
    {
        float fogStart, fogEnd;
        FogParameters parameters = info.getReturnValue();
        fogStart = viewDistance * SimpleFogMain.config.waterStart * 0.01f;
        fogEnd = viewDistance * SimpleFogMain.config.waterEnd * 0.01f;
        if (entity instanceof LocalPlayer localPlayer)
        {
            Holder<Biome> biomeHolder = localPlayer.level().getBiome(localPlayer.blockPosition());
            if (biomeHolder.is(BiomeTags.HAS_CLOSER_WATER_FOG))
                fogEnd = viewDistance * SimpleFogMain.config.waterEndSwamp * 0.01f;
            fogEnd *= Math.max(0.25f, localPlayer.getWaterVision());
        }
        info.setReturnValue(new FogParameters(fogStart, fogEnd, parameters.shape(), parameters.red(), parameters.green(), parameters.blue(), parameters.alpha()));
    }

    public static void overrideNetherFog(float viewDistance, CallbackInfoReturnable<FogParameters> info)
    {
        float fogStart, fogEnd;
        FogParameters parameters = info.getReturnValue();
        fogStart = viewDistance * SimpleFogMain.config.netherStart * 0.01f;
        fogEnd = viewDistance * SimpleFogMain.config.netherEnd * 0.01f;
        info.setReturnValue(new FogParameters(fogStart, fogEnd, parameters.shape(), parameters.red(), parameters.green(), parameters.blue(), parameters.alpha()));
    }

    public static void overrideTerrainFog(float viewDistance, Entity entity, CallbackInfoReturnable<FogParameters> info)
    {
        try (Level level = entity.level())
        {
            SimpleFogConfig conf = SimpleFogMain.config;
            SimpleFogConfig.RainConfig rainConf = conf.rainConfig;
            float targetFogStart = conf.terrainStart;
            float targetFogEnd = conf.terrainEnd;

            boolean raining = rainConf.rainToggle && level.isRaining();
            FogParameters parameters = info.getReturnValue();

            if (raining)
            {
                boolean skylight = entity.getEyeY() >= level.getHeight(Heightmap.Types.WORLD_SURFACE, entity.getBlockX(), entity.getBlockZ());
                targetFogStart = Math.min(100, Math.max(skylight ? -999 : -20, rainConf.rainStart) +
                        (skylight ? 0 : rainConf.minIndoorFog / viewDistance * 100));
                targetFogEnd = rainConf.rainEnd;
            }

            if (entity.tickCount != prevAge)
            {
                int a = rainConf.rainFogApplySpeed;
                if (fogStart < targetFogStart)
                    fogStart = Math.min(targetFogStart, fogStart + a);
                else
                    fogStart = Math.max(targetFogStart, fogStart - a);

                if (fogEnd < targetFogEnd)
                    fogEnd = Math.min(targetFogEnd, fogEnd + a);
                else
                    fogEnd = Math.max(targetFogEnd, fogEnd - a);
                prevAge = entity.tickCount;
            }

            info.setReturnValue(new FogParameters(
                    viewDistance * fogStart * 0.01f,
                    viewDistance * fogEnd * 0.01f,
                    parameters.shape(), parameters.red(), parameters.green(), parameters.blue(), parameters.alpha())
            );
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
