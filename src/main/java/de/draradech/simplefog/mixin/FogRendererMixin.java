package de.draradech.simplefog.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;

import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.draradech.simplefog.SimpleFogMain;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Inject(at = @At("RETURN"), method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;Lorg/joml/Vector4f;FZF)Lnet/minecraft/client/renderer/FogParameters;", cancellable = true)
    private static void afterSetupFog(Camera camera, FogMode fogMode, Vector4f fogColor, float viewDistance, boolean thickFog, float partialTick, CallbackInfoReturnable<FogParameters> info) {
        FogParameters parameters = info.getReturnValue();
        if (parameters == FogParameters.NO_FOG) return;
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
            if(SimpleFogMain.config.waterToggle) overrideWaterFog(viewDistance, entity, info);
        } else if (thickFog) {
            if(SimpleFogMain.config.netherToggle) overrideNetherFog(viewDistance, info);
        } else if (fogMode == FogMode.FOG_SKY) {
        } else {
            if(SimpleFogMain.config.terrainToggle) overrideTerrainFog(viewDistance, info);
        }
    }

    private static void overrideWaterFog(float viewDistance, Entity entity, CallbackInfoReturnable<FogParameters> info) {
        float fogStart, fogEnd;
        FogParameters parameters = info.getReturnValue();
        fogStart = viewDistance * SimpleFogMain.config.waterStart * 0.01f;
        fogEnd = viewDistance * SimpleFogMain.config.waterEnd * 0.01f;
        if (entity instanceof LocalPlayer) {
            LocalPlayer localPlayer = (LocalPlayer)entity;
            Holder<Biome> biomeHolder = localPlayer.level().getBiome(localPlayer.blockPosition());
            if (biomeHolder.is(BiomeTags.HAS_CLOSER_WATER_FOG)) {
                fogEnd = viewDistance * SimpleFogMain.config.waterEndSwamp * 0.01f;
            }
            fogEnd *= Math.max(0.25f, localPlayer.getWaterVision());
        }
        info.setReturnValue(new FogParameters(fogStart, fogEnd, parameters.shape(), parameters.red(), parameters.green(), parameters.blue(), parameters.alpha()));
    }

    private static void overrideNetherFog(float viewDistance, CallbackInfoReturnable<FogParameters> info) {
        float fogStart, fogEnd;
        FogParameters parameters = info.getReturnValue();
        fogStart = viewDistance * SimpleFogMain.config.netherStart * 0.01f;
        fogEnd = viewDistance * SimpleFogMain.config.netherEnd * 0.01f;
        info.setReturnValue(new FogParameters(fogStart, fogEnd, parameters.shape(), parameters.red(), parameters.green(), parameters.blue(), parameters.alpha()));
    }

    private static void overrideTerrainFog(float viewDistance, CallbackInfoReturnable<FogParameters> info) {
        float fogStart, fogEnd;
        FogParameters parameters = info.getReturnValue();
        fogStart = viewDistance * SimpleFogMain.config.terrainStart * 0.01f;
        fogEnd = viewDistance * SimpleFogMain.config.terrainEnd * 0.01f;
        info.setReturnValue(new FogParameters(fogStart, fogEnd, parameters.shape(), parameters.red(), parameters.green(), parameters.blue(), parameters.alpha()));
    }
}
