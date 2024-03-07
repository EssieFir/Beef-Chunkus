package essie.beefchunkus;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static essie.beefchunkus.BeefChunkus.*;
import static essie.beefchunkus.BeefChunkusItem.*;

public class BeefChunkusEvent implements Listener {

    private static void damageItemAndFeedPlayer(Player player, ItemStack item, EquipmentSlot hand) {
        if (!(hand == EquipmentSlot.OFF_HAND && isItemBeefChunkus(player.getInventory().getItemInMainHand()))) {

            NBTItem nbtItem = new NBTItem(item, true);

            //Check if the item has the custom nbt data, if not, add it.
            if (nbtItem.getInteger(FOOD_NOTCHES_KEY) == null) { nbtItem.setInteger(FOOD_NOTCHES_KEY, beef_chunkus_default_food_notches); }
            if (nbtItem.getInteger(FOOD_NOTCHES_KEY) == null) { nbtItem.setInteger(MAX_FOOD_NOTCHES_KEY, beef_chunkus_default_max_food_notches); }

            int remainingFoodNotches = nbtItem.getInteger(FOOD_NOTCHES_KEY);
            int maxFoodNotches = nbtItem.getInteger(MAX_FOOD_NOTCHES_KEY);

            int amountToFeed = 20 - player.getFoodLevel();
            float amountToSaturate;

            if (player.getGameMode().equals(GameMode.CREATIVE)) {
                player.setFoodLevel(20);
                player.setSaturation(20);
            } else {
                //Feed the player
                int playerFoodLevel = player.getFoodLevel();
                float playerSaturationLevel = player.getSaturation();
                if (amountToFeed > remainingFoodNotches) {
                    player.setFoodLevel(playerFoodLevel + remainingFoodNotches);
                    if (player.getFoodLevel()>= 20) { player.setFoodLevel(20); }

                    amountToSaturate = (float) (remainingFoodNotches * beef_chunkus_saturation_multiplier);
                    if (amountToSaturate > playerFoodLevel+remainingFoodNotches) { amountToSaturate = playerFoodLevel+remainingFoodNotches; }
                    player.setSaturation(playerSaturationLevel + amountToSaturate);

                    remainingFoodNotches = 0;

                } else {
                    player.setFoodLevel(20);
                    amountToSaturate = (float) (amountToFeed * beef_chunkus_saturation_multiplier);
                    if (amountToSaturate > playerFoodLevel+amountToFeed) { amountToSaturate = playerFoodLevel+amountToFeed; }
                    player.setSaturation(playerSaturationLevel + amountToSaturate);
                    remainingFoodNotches -= amountToFeed;
                }

                //Save the new nbt
                nbtItem.setInteger(FOOD_NOTCHES_KEY, remainingFoodNotches);

                //update the item
                item = nbtItem.getItem();

                //Get the Damage metadata
                Damageable itemdmg = (Damageable) item.getItemMeta();
                assert itemdmg != null;

                //Calculate the damage percentage.
                double foodNotchPercentage = ((double) remainingFoodNotches / maxFoodNotches);
                int chunkusDamageAmount = (int) Math.round(25-(25*foodNotchPercentage));
                if (chunkusDamageAmount <= 0) { chunkusDamageAmount = 1; }

                //Damage the item
                itemdmg.setDamage((short) chunkusDamageAmount);
                item.setItemMeta(itemdmg);
            }

            ItemMeta meta = item.getItemMeta();
            meta.setLore(createFoodTooltip(remainingFoodNotches, maxFoodNotches));
            item.setItemMeta(meta);

            //"Break" the item if it has no more food notches
            if (remainingFoodNotches <= 0) {
                item.setType(Material.AIR);
                if (beef_chunkus_allow_play_finish_sound) {
                    player.playSound(player.getLocation(), "beef_chunkus:beef_chunkus_finish", 100f, 1f);
                }
            }

            //Spawn particles and sounds
            player.spawnParticle(Particle.ITEM_CRACK, player.getLocation().add(0, 1.6, 0), 10, 0, 0, 0, 0.1, BeefChunkusItem.beefChunkus);
            player.playSound(player, Sound.ENTITY_GENERIC_EAT, 1f, 0.75f);

            //Replace the item in the players respective hand, main hand prioritized.
            if (hand == EquipmentSlot.HAND) {
                player.getInventory().setItemInMainHand(item);
            } else {
                player.getInventory().setItemInOffHand(item);
            }
        }
    }

    @EventHandler
    public static void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (isItemBeefChunkus(event.getItem())) {

                Player player = event.getPlayer();
                ItemStack item = event.getItem();
                EquipmentSlot hand = event.getHand();

                if (player.getVehicle() != null && player.getVehicle().getType() == EntityType.PIG) {
                    event.setCancelled(true);
                }

                //Fix the custom model data if it has changed.
                correctBeefChunkusModelData(item);

                if (player.getFoodLevel() <= 19 || player.getGameMode().equals(GameMode.CREATIVE)) {
                    damageItemAndFeedPlayer(player, item, hand);
                }
            }
        }
    }

    @EventHandler
    public static void onCraft(CraftItemEvent event) {
        if (beef_chunkus_allow_play_craft_sound && isItemBeefChunkus(event.getCurrentItem())) {
            Player player = (Player) event.getWhoClicked();
            player.playSound(player.getLocation(), "beef_chunkus:beef_chunkus_craft", 100f, 1f);
            event.getWhoClicked().discoverRecipe(BEEF_CHUNKUS_RECIPE);
        }
    }

    private static boolean isItemBeefChunkus(ItemStack item) {
        if (item == null) { return false; }
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getBoolean(VALID_BEEF_CHUNKUS_KEY);
    }

    private static void correctBeefChunkusModelData(ItemStack item) {
        if (!item.getItemMeta().hasCustomModelData() || item.getItemMeta().getCustomModelData() != beef_chunkus_custom_model_data) {
            ItemMeta meta = item.getItemMeta();
            meta.setCustomModelData(beef_chunkus_custom_model_data);
            item.setItemMeta(meta);
        }
    }

    public static List<String> createFoodTooltip(int amount, int maximum) {
        return List.of("§r§7\uD83C\uDF56 " + amount + "/" + maximum + " Notches \uD83C\uDF56");
    }
}
