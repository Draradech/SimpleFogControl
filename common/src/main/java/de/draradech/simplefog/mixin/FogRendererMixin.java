package de.draradech.simplefog.mixin;

import de.draradech.simplefog.SimpleFogMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @ModifyVariable(at = @At("HEAD"), method = "updateBuffer(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V", ordinal = 2)
    private float modifyRenderDistanceStart(float renderDistanceStart) {
        if (!SimpleFogMain.config.terrainToggle) return renderDistanceStart;
        return Minecraft.getInstance().options.getEffectiveRenderDistance() * SimpleFogMain.config.terrainStart * 0.16f;
    }

    @ModifyVariable(at = @At("HEAD"), method = "updateBuffer(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V", ordinal = 3)
    private float modifyRenderDistanceEnd(float renderDistanceEnd) {
        if (!SimpleFogMain.config.terrainToggle) return renderDistanceEnd;
        return Minecraft.getInstance().options.getEffectiveRenderDistance() * SimpleFogMain.config.terrainEnd * 0.16f;
    }
}
