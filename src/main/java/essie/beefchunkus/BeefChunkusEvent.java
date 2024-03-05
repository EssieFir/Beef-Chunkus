package essie.beefchunkus;

import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtWrapper;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BeefChunkusEvent implements Listener {

    private static final String FOOD_NOTCHES = "beef_chunkus:food_notches";
    private static final String MAX_FOOD_NOTCHES = "beef_chunkus:max_food_notches";

    private static void damageItemAndFeedPlayer(Player player, ItemStack item, EquipmentSlot hand) {
        if (!(hand == EquipmentSlot.OFF_HAND && isItemBeefChunkus(player.getInventory().getItemInMainHand()))) {

            //Check if the item has the custom nbt data, if not, add it.
            if (getItemTag(item, FOOD_NOTCHES) == null) {
                setItemTag(item, FOOD_NOTCHES, 70);
                setItemTag(item, MAX_FOOD_NOTCHES, 70);
            }

            int remainingFoodNotches = (int) getItemTag(item, FOOD_NOTCHES);
            int maxFoodNotches = (int) getItemTag(item, MAX_FOOD_NOTCHES);

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

                    amountToSaturate = (float) (remainingFoodNotches * 1.4);
                    if (amountToSaturate > playerFoodLevel+remainingFoodNotches) { amountToSaturate = playerFoodLevel+remainingFoodNotches; }
                    player.setSaturation(playerSaturationLevel + amountToSaturate);

                    remainingFoodNotches = 0;

                } else {
                    player.setFoodLevel(20);
                    amountToSaturate = (float) (amountToFeed * 1.4);
                    if (amountToSaturate > playerFoodLevel+amountToFeed) { amountToSaturate = playerFoodLevel+amountToFeed; }
                    player.setSaturation(playerSaturationLevel + amountToSaturate);
                    remainingFoodNotches -= amountToFeed;
                }

                //"Saturate" the player
//                if (amountToSaturate > remainingSaturationNotches) {
//                    player.setSaturation(player.getSaturation() + remainingSaturationNotches);
//                    if (player.getSaturation() >= 20) { player.setSaturation(20); }
//
//                    remainingSaturationNotches = 0;
//
//                } else {
//                    player.setSaturation(player.getSaturation() + amountToSaturate);
//
//                    remainingSaturationNotches -= amountToSaturate;
//                }

                //Save the new nbt
                setItemTag(item, FOOD_NOTCHES, remainingFoodNotches);

                //Get the Damage metadata
                Damageable itemdmg = (Damageable) item.getItemMeta();
                assert itemdmg != null;

                double foodNotchPercentage = ((double) remainingFoodNotches / maxFoodNotches);
                int chunkusDamageAmount = (int) Math.round(25-(25*foodNotchPercentage));
                if (chunkusDamageAmount <= 0) { chunkusDamageAmount = 1; }

                //Damage the item (less than 4 check is, so it adds up to 10 uses)
                itemdmg.setDamage((short) chunkusDamageAmount);

                //Damage the item
                item.setItemMeta(itemdmg);
            }

            ItemMeta meta = item.getItemMeta();
            meta.setLore(createFoodTooltip(remainingFoodNotches, maxFoodNotches));
            item.setItemMeta(meta);

            //"Break" the item if it has no more food notches
            if (remainingFoodNotches <= 0) {
                item.setType(Material.AIR);
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

    public static List<String> createFoodTooltip(int amount, int maximum) {
        return List.of("§r§7\uD83C\uDF56 " + amount + "/" + maximum + " Notches \uD83C\uDF56");
    }

    public static void setItemTag(ItemStack item, String key, Integer value) {
        NbtWrapper<?> wrapper = NbtFactory.fromItemTag(item);
        NbtCompound compound = NbtFactory.asCompound(wrapper);

        compound.put(key, value);

        NbtFactory.setItemTag(item, compound);

    }

    public static void setItemTag(ItemStack item, String key, Float value) {
        NbtWrapper<?> wrapper = NbtFactory.fromItemTag(item);
        NbtCompound compound = NbtFactory.asCompound(wrapper);

        compound.put(key, value);

        NbtFactory.setItemTag(item, compound);

    }

    public static Object getItemTag(ItemStack item, String key) {
        NbtWrapper<?> wrapper = NbtFactory.fromItemTag(item);
        NbtCompound compound = NbtFactory.asCompound(wrapper);
        NbtBase<?> tag = compound.getValue(key);

        return (tag==null)? null : tag.getValue();
    }
}
