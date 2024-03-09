package essie.beefchunkus;

import essie.beefchunkus.items.BeefChunkusItem;
import essie.beefchunkus.items.MendingAppleItem;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class BeefChunkus extends JavaPlugin {

    public static int beef_chunkus_custom_model_data;
    public static double beef_chunkus_saturation_multiplier;
    public static int beef_chunkus_default_food_notches;
    public static int beef_chunkus_default_max_food_notches;
    public static boolean beef_chunkus_allow_play_craft_sound;
    public static boolean beef_chunkus_allow_play_finish_sound;

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
        MendingAppleItem.init();
        getServer().getPluginManager().registerEvents(new BeefChunkusEvent(), this);
        getServer().getPluginManager().registerEvents(new MendingAppleEvent(), this);
        this.getCommand("beefchunkus").setExecutor(new BeefChunkusCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
