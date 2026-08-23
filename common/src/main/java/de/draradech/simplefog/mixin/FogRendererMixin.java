package de.draradech.simplefog.mixin;

import de.draradech.simplefog.SimpleFogMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    private boolean active()
    {
        ResourceKey<Level> level = Minecraft.getInstance().player.level().dimension();
        if (level == Level.NETHER)
        {
            return SimpleFogMain.config.netherToggle;
        }
        else if (level == Level.END)
        {
            return SimpleFogMain.config.endToggle;
        }
        // overworld and custom dimensions
        return SimpleFogMain.config.overworldToggle;
    }

    @Redirect(method = "setupFog", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/fog/FogData;renderDistanceStart:F", opcode = Opcodes.PUTFIELD))
    private void modifyRenderDistanceStart(FogData data, float renderDistanceStart) {
        if (active()) data.renderDistanceStart = 1e5f;
        else data.renderDistanceStart = renderDistanceStart;
    }
}
