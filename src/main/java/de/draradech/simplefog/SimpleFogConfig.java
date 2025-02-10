package de.draradech.simplefog;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "simplefog")
public class SimpleFogConfig implements ConfigData {
    public static class RainConfig
    {
        public boolean rainToggle = true;
        public float rainStart = 0.0f;
        public float rainStartIndoor = 25.0f;
        public float rainEnd = 110.0f;
        public int rainFogApplySpeed = 1;
    }

    @ConfigEntry.Category(value = "terrain")
    public boolean terrainToggle = true;
    @ConfigEntry.Category(value = "terrain")
    public float terrainStart = 70.0f;
    @ConfigEntry.Category(value = "terrain")
    public float terrainEnd = 130.0f;
    @ConfigEntry.Category(value = "terrain")
    @ConfigEntry.Gui.CollapsibleObject
    public RainConfig rainConfig = new RainConfig();
    
    @ConfigEntry.Category(value = "water")
    public boolean waterToggle = true;
    @ConfigEntry.Category(value = "water")
    public float waterStart = -20.0f;
    @ConfigEntry.Category(value = "water")
    public float waterEnd = 90.0f;
    @ConfigEntry.Category(value = "water")
    public float waterEndSwamp = 60.0f;
    
    @ConfigEntry.Category(value = "nether")
    public boolean netherToggle = true;
    @ConfigEntry.Category(value = "nether")
    public float netherStart = 5.0f;
    @ConfigEntry.Category(value = "nether")
    public float netherEnd = 80.0f;
}
