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
        public float rainFogApplySpeed = 1.0f;
    }

    @ConfigEntry.Category(value = "overworld")
    public boolean overworldToggle = true;
    @ConfigEntry.Category(value = "overworld")
    public float overworldStart = 70.0f;
    @ConfigEntry.Category(value = "overworld")
    public float overworldEnd = 130.0f;
    @ConfigEntry.Category(value = "overworld")
    @ConfigEntry.Gui.CollapsibleObject
    public RainConfig rainConfig = new RainConfig();

    @ConfigEntry.Category(value = "nether")
    public boolean netherToggle = true;
    @ConfigEntry.Category(value = "nether")
    public float netherStart = 5.0f;
    @ConfigEntry.Category(value = "nether")
    public float netherEnd = 80.0f;

    @ConfigEntry.Category(value = "end")
    public boolean endToggle = true;
    @ConfigEntry.Category(value = "end")
    public float endStart = 70.0f;
    @ConfigEntry.Category(value = "end")
    public float endEnd = 130.0f;

    @ConfigEntry.Category(value = "water")
    public boolean waterToggle = true;
    @ConfigEntry.Category(value = "water")
    public float waterStart = -20.0f;
    @ConfigEntry.Category(value = "water")
    public float waterEnd = 90.0f;
    @ConfigEntry.Category(value = "water")
    public float waterEndSwamp = 60.0f;
}
