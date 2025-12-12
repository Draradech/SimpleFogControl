package de.draradech.simplefog.mixin;

import de.draradech.simplefog.SimpleFogMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    private boolean active()
    {
        if (!SimpleFogMain.config.overworldToggle && Minecraft.getInstance().player.level().dimension() == Level.OVERWORLD) return false;
        if (!SimpleFogMain.config.netherToggle && Minecraft.getInstance().player.level().dimension() == Level.NETHER) return false;
        if (!SimpleFogMain.config.endToggle && Minecraft.getInstance().player.level().dimension() == Level.END) return false;
        return true;
    }

    @Redirect(method = "setupFog", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/fog/FogData;renderDistanceStart:F", opcode = Opcodes.PUTFIELD))
    private void modifyRenderDistanceStart(FogData data, float renderDistanceStart) {
        if (active()) data.renderDistanceStart = 1e5f;
        else data.renderDistanceStart = renderDistanceStart;
    }

    @Redirect(method = "setupFog", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/fog/FogData;renderDistanceEnd:F", opcode = Opcodes.PUTFIELD))
    private void modifyRenderDistanceEnd(FogData data, float renderDistanceEnd) {
        if (active()) data.renderDistanceEnd = 1e5f;
        else data.renderDistanceEnd = renderDistanceEnd;
    }
}
