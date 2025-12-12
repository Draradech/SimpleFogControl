package de.draradech.simplefog;

import me.shedaniel.autoconfig.*;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(SimpleFogNeoforge.MODID)
public class SimpleFogNeoforge {
    public static final String MODID = "simplefog";

    public SimpleFogNeoforge()
    {
        SimpleFogMain.init();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class,() -> (container, parent) ->
                AutoConfigClient.getConfigScreen(SimpleFogConfig.class, parent).get());
    }
}
