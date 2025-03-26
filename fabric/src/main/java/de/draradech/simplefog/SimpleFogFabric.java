package de.draradech.simplefog;

import net.fabricmc.api.ModInitializer;

public class SimpleFogFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        SimpleFogMain.init();
    }
}
