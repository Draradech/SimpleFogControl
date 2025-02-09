package de.draradech.simplefog.mixin;

import de.draradech.simplefog.FogOverrider;
import de.draradech.simplefog.SimpleFogMain;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogParameters;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin
{
    @Inject(at = @At("RETURN"), method = "setupFog(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/FogRenderer$FogMode;Lorg/joml/Vector4f;FZF)Lnet/minecraft/client/renderer/FogParameters;", cancellable = true)
    private static void afterSetupFog(Camera camera, FogMode fogMode, Vector4f fogColor, float viewDistance, boolean thickFog, float partialTick, CallbackInfoReturnable<FogParameters> info)
    {
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
            if(SimpleFogMain.config.waterToggle) FogOverrider.overrideWaterFog(viewDistance, entity, info);
        } else if (thickFog) {
            if(SimpleFogMain.config.netherToggle) FogOverrider.overrideNetherFog(viewDistance, info);
        } else if (fogMode == FogMode.FOG_SKY) {
        } else {
            if(SimpleFogMain.config.terrainToggle) FogOverrider.overrideTerrainFog(viewDistance, entity, info);
        }
    }
}
