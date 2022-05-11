package de.draradech.simplefog;

import net.fabricmc.api.ModInitializer;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public class SimpleFogMain implements ModInitializer {
    public static SimpleFogConfig config;
    
    @Override
    public void onInitialize() {
        AutoConfig.register(SimpleFogConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(SimpleFogConfig.class).getConfig();
    }
}
