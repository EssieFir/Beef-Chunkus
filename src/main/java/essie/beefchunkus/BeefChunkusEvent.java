package essie.beefchunkus;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Objects;

public class BeefChunkusEvent implements Listener {

    private static boolean isItemBeefChunkus(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) {
            if (item.getItemMeta().getCustomModelData() == BeefChunkusItem.beefChunkus.getItemMeta().getCustomModelData()) {
                if (item.getType().equals(BeefChunkusItem.beefChunkus.getType())) {
                    return true;
                }
            }
        }

        return false;
    }

    private static void damageItemAndFeedPlayer(Player player, ItemStack item, EquipmentSlot hand) {
        if (!(hand == EquipmentSlot.OFF_HAND && isItemBeefChunkus(player.getInventory().getItemInMainHand()))) {
            //Get the Damage metadata (tag)
            Damageable itemdmg = (Damageable) item.getItemMeta();
            assert itemdmg != null;
            int damage;

            //Damage the item (less than 4 check is, so it adds up to 10 uses)
            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                if (itemdmg.getDamage() <= 4) {
                    damage = itemdmg.getDamage() + 2;
                } else {
                    damage = itemdmg.getDamage() + 3;
                }
                itemdmg.setDamage((short) damage);

                //Damage the item
                item.setItemMeta(itemdmg);
            }

            //Feed the player
            if (player.getSaturation() >= 20) {
                player.setSaturation(20f);
            } else {
                player.setSaturation(player.getSaturation() + 10f);
            }
            if (player.getFoodLevel() >= 20) {
                player.setFoodLevel(20);
            } else {
                player.setFoodLevel(player.getFoodLevel() + 7);
            }

            //"Break" the item if it's over 25 damage
            if (itemdmg.getDamage() >= 25) {
                item.setType(Material.AIR);
            }

            //Spawn particles and sounds
            player.spawnParticle(Particle.ITEM_CRACK, player.getLocation().add(0, 1.6, 0), 10, 0, 0, 0, 0.1, BeefChunkusItem.beefChunkus);
            player.playSound(player, Sound.ENTITY_GENERIC_EAT, 1, 1);

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
                event.setCancelled(true);
                Player player = event.getPlayer();
                ItemStack item = event.getItem();
                EquipmentSlot hand = event.getHand();
                if (player.getFoodLevel() <= 19 || player.getGameMode().equals(GameMode.CREATIVE)) {
                    damageItemAndFeedPlayer(player, item, hand);
                }
            }
        }
    }
}
