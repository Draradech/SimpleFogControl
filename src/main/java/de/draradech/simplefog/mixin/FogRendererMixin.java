package de.draradech.simplefog.mixin;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.draradech.simplefog.SimpleFogMain;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Inject(at = @At("TAIL"), method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;FZF)V")
    private static void afterSetupFog(Camera camera, FogMode fogMode, float viewDistance, boolean thickFog, float partialTick, CallbackInfo info) {
        FogType fogType = camera.getFluidInCamera();
        Entity entity = camera.getEntity();
        boolean mobEffect = (  (entity instanceof LivingEntity)
                            && (  ((LivingEntity)entity).hasEffect(MobEffects.BLINDNESS)
                               || ((LivingEntity)entity).hasEffect(MobEffects.DARKNESS)
                               )
                            ); 
        if (fogType == FogType.LAVA) {
        } else if (fogType == FogType.POWDER_SNOW) {
        } else if (mobEffect) {
        } else if (fogType == FogType.WATER) {
            if(SimpleFogMain.config.waterToggle) overrideWaterFog(viewDistance, entity);
        } else if (thickFog) {
            if(SimpleFogMain.config.netherToggle) overrideNetherFog(viewDistance);
        } else if (fogMode == FogMode.FOG_SKY) {
        } else {
            if(SimpleFogMain.config.terrainToggle) overrideTerrainFog(viewDistance, fogMode);
        }
    }

    private static void overrideWaterFog(float viewDistance, Entity entity) {
        float fogStart, fogEnd;
        fogStart = viewDistance * SimpleFogMain.config.waterStart * 0.01f;
        fogEnd = viewDistance * SimpleFogMain.config.waterEnd * 0.01f;
        if (entity instanceof LocalPlayer) {
            LocalPlayer localPlayer = (LocalPlayer)entity;
            Holder<Biome> biomeHolder = localPlayer.level.getBiome(localPlayer.blockPosition());
            if (biomeHolder.is(BiomeTags.HAS_CLOSER_WATER_FOG)) {
                fogEnd = viewDistance * SimpleFogMain.config.waterEndSwamp * 0.01f;
            }
            fogEnd *= Math.max(0.25f, localPlayer.getWaterVision());
        }
        RenderSystem.setShaderFogStart(fogStart);
        RenderSystem.setShaderFogEnd(fogEnd);
    }

    private static void overrideNetherFog(float viewDistance) {
        float fogStart, fogEnd;
        fogStart = viewDistance * SimpleFogMain.config.netherStart * 0.01f;
        fogEnd = viewDistance * SimpleFogMain.config.netherEnd * 0.01f;
        RenderSystem.setShaderFogStart(fogStart);
        RenderSystem.setShaderFogEnd(fogEnd);
    }

    private static void overrideTerrainFog(float viewDistance, FogMode fogMode) {
        float fogStart, fogEnd;
        fogStart = viewDistance * SimpleFogMain.config.terrainStart * 0.01f;
        fogEnd = viewDistance * SimpleFogMain.config.terrainEnd * 0.01f;
        RenderSystem.setShaderFogStart(fogStart);
        RenderSystem.setShaderFogEnd(fogEnd);
    }
}
