package essie.beefchunkus.items;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class MendingAppleItem {

    public static final String FOOD_NOTCHES_KEY = "beef_chunkus:food_notches";
    public static final String MAX_FOOD_NOTCHES_KEY = "beef_chunkus:max_food_notches";
    public static final String VALID_MENDING_APPLE_KEY = "beef_chunkus:valid_mending_apple";
    public static final NamespacedKey MENDING_APPLE_RECIPE = new NamespacedKey("beef_chunkus", "mending_apple");
    public static ItemStack mendingApple;

    public static void init() {
        makeMendingApple();
    }

    private static void makeMendingApple() {
        ItemStack item = new ItemStack(Material.BEETROOT_SOUP, 1);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§rMending Apple");
        meta.setCustomModelData(1);
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item, true);
        nbtItem.setBoolean(VALID_MENDING_APPLE_KEY, true);

        mendingApple = nbtItem.getItem();


        ShapedRecipe sr = new ShapedRecipe(MENDING_APPLE_RECIPE, item);
        sr.shape("EEE",
                 "EGE",
                 "EEE");
        sr.setIngredient('E', Material.EXPERIENCE_BOTTLE);
        sr.setIngredient('G', Material.ENCHANTED_GOLDEN_APPLE);
        Bukkit.getServer().addRecipe(sr);
    }
}
