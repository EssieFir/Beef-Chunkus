package essie.beefchunkus;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static essie.beefchunkus.BeefChunkus.*;
import static essie.beefchunkus.items.BeefChunkusItem.*;
import static essie.beefchunkus.items.MendingAppleItem.VALID_MENDING_APPLE_KEY;
import static essie.beefchunkus.items.MendingAppleItem.mendingApple;

public class MendingAppleEvent implements Listener {
    @EventHandler
    public static void onRightClick(PlayerItemConsumeEvent event) {
            if (isItemMendingApple(event.getItem())) {

                Player player = event.getPlayer();
                ItemStack item = event.getItem();
                EquipmentSlot hand = event.getHand();

                //Fix the custom model data if it has changed.
//                correctBeefChunkusModelData(item);

                if (player.getFoodLevel() <= 19 || player.getGameMode().equals(GameMode.CREATIVE)) {
                    int playerHungerLevel = player.getFoodLevel();
                    float playerExperienceLevel = XpHelper.getExp(player);
                    player.sendMessage("Mending apple prototype: player total xp is " + playerExperienceLevel);

                    int amountToFeed = 20 - playerHungerLevel;

                    if (playerExperienceLevel > amountToFeed) {
                        player.setFoodLevel(20);
                        XpHelper.changeExp(player, amountToFeed*-1);
                        player.sendMessage("Mending apple prototype: Consumed XP fed player.");
                    } else {
                        player.setFoodLevel(playerHungerLevel + Math.round(playerExperienceLevel));
                        XpHelper.changeExp(player, 0);
                        player.sendMessage("Mending apple prototype: Not enough XP to feed player, fed remainder");
                    }

//                  player.getInventory().removeItem(new ItemStack(Material.BOWL,1));
                    event.setCancelled(true);
                }
            }
    }

//    @EventHandler
//    public static void onCraft(CraftItemEvent event) {
//        if (beef_chunkus_allow_play_craft_sound && isItemMendingApple(event.getCurrentItem())) {
//            Player player = (Player) event.getWhoClicked();
//            player.playSound(player.getLocation(), "beef_chunkus:beef_chunkus_craft", 100f, 1f);
//            event.getWhoClicked().discoverRecipe(BEEF_CHUNKUS_RECIPE);
//        }
//    }

    private static boolean isItemMendingApple(ItemStack item) {
        if (item == null) { return false; }
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getBoolean(VALID_MENDING_APPLE_KEY);
    }

    private static void correctBeefChunkusModelData(ItemStack item) {
        if (!item.getItemMeta().hasCustomModelData() || item.getItemMeta().getCustomModelData() != beef_chunkus_custom_model_data) {
            ItemMeta meta = item.getItemMeta();
            meta.setCustomModelData(1);
            item.setItemMeta(meta);
        }
    }

    public static List<String> createFoodTooltip(int amount, int maximum) {
        return List.of("§r§7\uD83C\uDF56 " + amount + "/" + maximum + " Notches \uD83C\uDF56");
    }
}
