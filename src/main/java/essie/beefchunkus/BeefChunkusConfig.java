package essie.beefchunkus;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import static essie.beefchunkus.BeefChunkus.MOD_ID;

@Config(name = MOD_ID)
public class BeefChunkusConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    double saturation_multiplier = 1.4;
    @ConfigEntry.Gui.Tooltip
    int default_food_notches = 70;
    @ConfigEntry.Gui.Tooltip
    int default_max_food_notches = 70;
}
