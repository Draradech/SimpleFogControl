package de.draradech.simplefog.mixin;

import de.draradech.simplefog.SimpleFogMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Redirect(method = "setupFog", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/fog/FogData;renderDistanceStart:F", opcode = Opcodes.PUTFIELD))
    private void modifyRenderDistanceStart(FogData data, float renderDistanceStart) {
        if (!SimpleFogMain.config.terrainToggle) data.renderDistanceStart = renderDistanceStart;
        else data.renderDistanceStart = Minecraft.getInstance().options.getEffectiveRenderDistance() * SimpleFogMain.config.terrainStart * 0.16f;
    }

    @Redirect(method = "setupFog", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/fog/FogData;renderDistanceEnd:F", opcode = Opcodes.PUTFIELD))
    private void modifyRenderDistanceEnd(FogData data, float renderDistanceEnd) {
        if (!SimpleFogMain.config.terrainToggle) data.renderDistanceEnd = renderDistanceEnd;
        else data.renderDistanceEnd = Minecraft.getInstance().options.getEffectiveRenderDistance() * SimpleFogMain.config.terrainEnd * 0.16f;
    }
}
