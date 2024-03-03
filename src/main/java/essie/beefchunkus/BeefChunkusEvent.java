package essie.beefchunkus;

import org.bukkit.Material;
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

    @EventHandler
    public static void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null && event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasCustomModelData()) {
                if (event.getItem().getItemMeta().getCustomModelData() == BeefChunkusItem.beefChunkus.getItemMeta().getCustomModelData()) {
                    if (event.getItem().getType().equals(BeefChunkusItem.beefChunkus.getType())) {
                        Player player = event.getPlayer();
                        ItemStack item = event.getItem();
                        Damageable itemdmg = (Damageable) item.getItemMeta();
                        assert itemdmg != null;
                        int damage;
                        if (itemdmg.getDamage() <= 4) {
                            damage = itemdmg.getDamage() + 2;
                        } else {
                            damage = itemdmg.getDamage() + 3;
                        }
                        itemdmg.setDamage((short) damage);
                        item.setItemMeta(itemdmg);

                        player.setSaturation(player.getSaturation() + 0.5f);
                        player.setFoodLevel(player.getFoodLevel() + 7);

                        if (itemdmg.getDamage() >= 25) {
                            item.setType(Material.AIR);
                        }

                        player.playSound(player, Sound.ENTITY_GENERIC_EAT, 1, 1);
                        player.getInventory().setItemInMainHand(item);
                    }
                }
            }
        }
    }
}
