package essie.beefchunkus;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BeefChunkus extends JavaPlugin {

    public static int BEEF_CHUNKUS_CUSTOM_MODEL_DATA;
    public static double BEEF_CHUNKUS_SATURATION_MULTIPLIER;
    public static int BEEF_CHUNKUS_DEFAULT_FOOD_NOTCHES;
    public static int BEEF_CHUNKUS_DEFAULT_MAX_FOOD_NOTCHES;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        BEEF_CHUNKUS_CUSTOM_MODEL_DATA = getConfig().getInt("beef_chunkus_custom_model_data");
        BEEF_CHUNKUS_SATURATION_MULTIPLIER = getConfig().getInt("beef_chunkus_saturation_multiplier");
        BEEF_CHUNKUS_DEFAULT_FOOD_NOTCHES = getConfig().getInt("beef_chunkus_default_food_notches");
        BEEF_CHUNKUS_DEFAULT_MAX_FOOD_NOTCHES = getConfig().getInt("beef_chunkus_default_max_food_notches");

        BeefChunkusItem.init();
        getServer().getPluginManager().registerEvents(new BeefChunkusEvent(), this);
        this.getCommand("beefchunkus").setExecutor(new BeefChunkusCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
