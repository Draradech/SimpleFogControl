package de.draradech.simplefog;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.ClothConfigDemo;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(SimpleFogNeoforge.MODID)
public class SimpleFogNeoforge {
    public static final String MODID = "simplefog";

    public static SimpleFogConfig config;
    
    public SimpleFogNeoforge()
    {
        SimpleFogMain.init();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (container, parent) -> {
            return AutoConfig.getConfigScreen(SimpleFogConfig.class, parent).get();
        });
    }
}
