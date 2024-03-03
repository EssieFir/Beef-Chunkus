package essie.beefchunkus;

import org.bukkit.plugin.java.JavaPlugin;

public final class BeefChunkus extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        BeefChunkusItem.init();
        getServer().getPluginManager().registerEvents(new BeefChunkusEvent(), this);
        this.getCommand("beefchunkus").setExecutor(new BeefChunkusCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
