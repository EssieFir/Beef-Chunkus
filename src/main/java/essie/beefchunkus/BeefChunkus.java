package essie.beefchunkus;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class BeefChunkus extends JavaPlugin {

    public static int beef_chunkus_custom_model_data;
    public static double beef_chunkus_saturation_multiplier;
    public static int beef_chunkus_default_food_notches;
    public static int beef_chunkus_default_max_food_notches;
    public static boolean beef_chunkus_allow_play_craft_sound;
    public static boolean beef_chunkus_allow_play_finish_sound;

    public static final NamespacedKey BEEF_CHUNKUS_RECIPE = new NamespacedKey("beef_chunkus", "beef_chunkus");

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        beef_chunkus_custom_model_data = getConfig().getInt("beef_chunkus_custom_model_data");
        beef_chunkus_saturation_multiplier = getConfig().getInt("beef_chunkus_saturation_multiplier");
        beef_chunkus_default_food_notches = getConfig().getInt("beef_chunkus_default_food_notches");
        beef_chunkus_default_max_food_notches = getConfig().getInt("beef_chunkus_default_max_food_notches");
        beef_chunkus_allow_play_craft_sound = getConfig().getBoolean("beef_chunkus_allow_play_craft_sound", true);
        beef_chunkus_allow_play_finish_sound = getConfig().getBoolean("beef_chunkus_allow_play_finish_sound", true);

        BeefChunkusItem.init();
        getServer().getPluginManager().registerEvents(new BeefChunkusEvent(), this);
        this.getCommand("beefchunkus").setExecutor(new BeefChunkusCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
