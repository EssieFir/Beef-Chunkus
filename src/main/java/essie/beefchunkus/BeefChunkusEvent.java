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

    private static void damageItemAndFeedPlayer(Player player) {
        ItemStack item;
        boolean isItemInMainHand = true;

        //Check each hand if the item is Beef Chunkus
        if (isItemBeefChunkus(player.getInventory().getItemInMainHand())) {
            item = player.getInventory().getItemInMainHand();
        } else if (isItemBeefChunkus(player.getInventory().getItemInOffHand())) {
            item = player.getInventory().getItemInOffHand();
            isItemInMainHand = false;
        } else {
            item = new ItemStack(Material.AIR);
        }

        //Get the Damage metadata (tag)
        Damageable itemdmg = (Damageable) item.getItemMeta();
        assert itemdmg != null;
        int damage;

        //Damage the item (less than 4 check is, so it adds up to 10 uses)
        if (itemdmg.getDamage() <= 4) {
            damage = itemdmg.getDamage() + 2;
        } else {
            damage = itemdmg.getDamage() + 3;
        }
        itemdmg.setDamage((short) damage);

        //Damage the item
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            item.setItemMeta(itemdmg);
        }

        //Feed the player
        player.setSaturation(player.getSaturation() + 10f);
        player.setFoodLevel(player.getFoodLevel() + 7);

        //"Break" the item if it's over 25 damage
        if (itemdmg.getDamage() >= 25) {
            item.setType(Material.AIR);
        }

        //Spawn particles and sounds
        player.spawnParticle(Particle.ITEM_CRACK, player.getLocation().add(0, 1.6, 0), 10, 0, 0, 0, 0.1, BeefChunkusItem.beefChunkus);
        player.playSound(player, Sound.ENTITY_GENERIC_EAT, 1, 1);

        //Replace the item in the players respective hand, main hand prioritized.
        if (isItemInMainHand) {
            player.getInventory().setItemInMainHand(item);
        }
        else {
            player.getInventory().setItemInOffHand(item);
        }
    }

    @EventHandler
    public static void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (isItemBeefChunkus(event.getItem())) {
                Player player = event.getPlayer();
                if (player.getFoodLevel() <= 19 || player.getGameMode().equals(GameMode.CREATIVE)) {
                    damageItemAndFeedPlayer(player);
                }
            }
        }
    }
}
