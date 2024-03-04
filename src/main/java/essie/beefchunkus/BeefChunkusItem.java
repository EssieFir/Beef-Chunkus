package essie.beefchunkus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BeefChunkusItem {

    public static ItemStack beefChunkus;

    public static void init() {
        makeBeefChunkus();
    }

    private static void makeBeefChunkus() {
        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK, 1);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§rBeef Chunkus");
        meta.setCustomModelData(1);
        meta.setLore(BeefChunkusEvent.createFoodTooltip(70,70));
        item.setItemMeta(meta);

        beefChunkus = item;

        ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("beef_chunkus"), item);
        sr.shape("RBR",
                 "BSB",
                 "BSB");
        sr.setIngredient('R', Material.COOKED_RABBIT);
        sr.setIngredient('B', Material.COOKED_BEEF);
        sr.setIngredient('S', Material.RABBIT_STEW);
        Bukkit.getServer().addRecipe(sr);
    }
}
