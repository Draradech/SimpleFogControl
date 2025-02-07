package de.draradech.simplefog;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "simplefog")
public class SimpleFogConfig implements ConfigData {
    @ConfigEntry.Category(value = "terrain")
    public boolean terrainToggle = true;
    @ConfigEntry.Category(value = "terrain")
    public float terrainStart = 70;
    @ConfigEntry.Category(value = "terrain")
    public float terrainEnd = 130;

    @ConfigEntry.Category(value = "terrain")
    @ConfigEntry.Gui.CollapsibleObject
    public RainConfig rainConfig = new RainConfig();
    
    @ConfigEntry.Category(value = "water")
    public boolean waterToggle = true;
    @ConfigEntry.Category(value = "water")
    public float waterStart = -20;
    @ConfigEntry.Category(value = "water")
    public float waterEnd = 90;
    @ConfigEntry.Category(value = "water")
    public float waterEndSwamp = 60;
    
    @ConfigEntry.Category(value = "nether")
    public boolean netherToggle = true;
    @ConfigEntry.Category(value = "nether")
    public float netherStart = 5;
    @ConfigEntry.Category(value = "nether")
    public float netherEnd = 80;

    public static class RainConfig
    {
        public boolean rainToggle = true;
        public float rainStart = 0;
        public float rainEnd = 110;
        public int minIndoorFog = 50;
        public int rainFogApplySpeed = 3;
    }
}
