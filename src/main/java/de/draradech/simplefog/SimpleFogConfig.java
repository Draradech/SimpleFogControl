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
    public boolean rainToggle = true;
    @ConfigEntry.Category(value = "terrain")
    public float rainStart = 0;
    @ConfigEntry.Category(value = "terrain")
    public float rainEnd = 110;
    @ConfigEntry.Category(value = "terrain")
    public int rainFogApplySpeed = 3;
    
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
}
