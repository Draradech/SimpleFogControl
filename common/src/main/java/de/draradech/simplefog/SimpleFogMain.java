package de.draradech.simplefog;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public class SimpleFogMain {
    public static SimpleFogConfig config;
    
    public static void init() {
        AutoConfig.register(SimpleFogConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(SimpleFogConfig.class).getConfig();
    }
}
