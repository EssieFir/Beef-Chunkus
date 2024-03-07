package essie.beefchunkus;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import static essie.beefchunkus.BeefChunkus.*;

public class BeefChunkusItem {

    public static final String FOOD_NOTCHES_KEY = "beef_chunkus:food_notches";
    public static final String MAX_FOOD_NOTCHES_KEY = "beef_chunkus:max_food_notches";
    public static final String VALID_BEEF_CHUNKUS_KEY = "beef_chunkus:valid_beef_chunkus";

    public static ItemStack beefChunkus;

    public static void init() {
        makeBeefChunkus();
    }

    private static void makeBeefChunkus() {
        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK, 1);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§rBeef Chunkus");
        meta.setCustomModelData(BEEF_CHUNKUS_CUSTOM_MODEL_DATA);
        meta.setLore(BeefChunkusEvent.createFoodTooltip(BEEF_CHUNKUS_DEFAULT_FOOD_NOTCHES, BEEF_CHUNKUS_DEFAULT_MAX_FOOD_NOTCHES));
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item, true);
        nbtItem.setInteger(FOOD_NOTCHES_KEY, BEEF_CHUNKUS_DEFAULT_FOOD_NOTCHES);
        nbtItem.setInteger(MAX_FOOD_NOTCHES_KEY, BEEF_CHUNKUS_DEFAULT_MAX_FOOD_NOTCHES);
        //I'm not sure how to prevent mending from being applied, so here's a fix I guess.
        nbtItem.setInteger("RepairCost", 2560);
        nbtItem.setBoolean(VALID_BEEF_CHUNKUS_KEY, true);

        beefChunkus = nbtItem.getItem();


        ShapedRecipe sr = new ShapedRecipe(BEEF_CHUNKUS_RECIPE, item);
        sr.shape("RBR",
                 "BSB",
                 "BSB");
        sr.setIngredient('R', Material.COOKED_RABBIT);
        sr.setIngredient('B', Material.COOKED_BEEF);
        sr.setIngredient('S', Material.RABBIT_STEW);
        Bukkit.getServer().addRecipe(sr);
    }
}
