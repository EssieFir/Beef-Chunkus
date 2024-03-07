package essie.beefchunkus;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

import static essie.beefchunkus.BeefChunkus.*;

public class BeefChunkusItem extends Item {

    private static final String FOOD_NOTCHES_TAG = "beef_chunkus:food_notches";
    private static final String MAX_FOOD_NOTCHES_TAG = "beef_chunkus:max_food_notches";

    public BeefChunkusItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }
    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return stack.getDamage() > 0;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {

        NbtCompound compoundTag = stack.getNbt();

        if (compoundTag == null) { compoundTag = new NbtCompound(); }

        //Check if the item has the custom nbt data, if not, add it.
        if (compoundTag.get(FOOD_NOTCHES_TAG) == null) {
            setTag(stack, FOOD_NOTCHES_TAG, default_food_notches);
        }
        if (compoundTag.get(MAX_FOOD_NOTCHES_TAG) == null) {
            setTag(stack, MAX_FOOD_NOTCHES_TAG, default_max_food_notches);
        }

        int remainingFoodNotches = compoundTag.getInt(FOOD_NOTCHES_TAG);
        int maxFoodNotches = compoundTag.getInt(MAX_FOOD_NOTCHES_TAG);

        tooltip.add(Text.translatable("tooltip.beef_chunkus.remaining_notches",
                (Text.literal(String.valueOf(remainingFoodNotches))), Text.literal(String.valueOf(maxFoodNotches))).formatted(Formatting.GRAY));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        //itemstack is consumed and set to air when passed through finishUsingItem, so copy it before it gets deleted.
        ItemStack initialItem = stack.copy();
        ItemStack itemstack = this.isFood() ? user.eatFood(world, stack) : stack;
        ItemStack item = new ItemStack(BeefChunkus.BEEF_CHUNKUS);

        item.setNbt(initialItem.getNbt());

        if (user instanceof PlayerEntity player) {

            boolean isCreative = player.getAbilities().creativeMode;

            NbtCompound initialTag = initialItem.getNbt();

            if (initialTag == null) {initialTag = new NbtCompound();}

            //Check if the item has the custom nbt data, if not, add it.
            if (initialTag.get(FOOD_NOTCHES_TAG) == null) {
                setTag(item, FOOD_NOTCHES_TAG, default_food_notches);
            }
            if (initialTag.get(MAX_FOOD_NOTCHES_TAG) == null) {
                setTag(item, MAX_FOOD_NOTCHES_TAG, default_max_food_notches);
            }

            int remainingFoodNotches = initialTag.getInt(FOOD_NOTCHES_TAG);
            int maxFoodNotches = initialTag.getInt(MAX_FOOD_NOTCHES_TAG);

            int amountToFeed = 20 - player.getHungerManager().getFoodLevel();
            float amountToSaturate;

            if (isCreative) {
                player.getHungerManager().setFoodLevel(20);
                player.getHungerManager().setSaturationLevel(20);
            } else {
                //Feed the player
                int playerFoodLevel = player.getHungerManager().getFoodLevel();
                float playerSaturationLevel = player.getHungerManager().getSaturationLevel();
                //If the player's required food level (amount to feed) is larger than what the food item has,
                //use the remaining notches of food to fill the player
                if (amountToFeed > remainingFoodNotches) {
                    player.getHungerManager().setFoodLevel(playerFoodLevel + remainingFoodNotches);
                    if (playerFoodLevel >= 20) {
                        player.getHungerManager().setFoodLevel(20);
                    }

                    //Fill the player's food saturation 1.4x the amount that was fed to the player.
                    amountToSaturate = (float) (remainingFoodNotches * saturation_multiplier);
                    if (amountToSaturate > playerFoodLevel + remainingFoodNotches) {
                        amountToSaturate = playerFoodLevel + remainingFoodNotches;
                    }
                    player.getHungerManager().setSaturationLevel(playerSaturationLevel + amountToSaturate);

                    remainingFoodNotches = 0;

                } else {
                    //If the amountToFeed is larger than the remaining Notches, just fill the player to full.
                    player.getHungerManager().setFoodLevel(20);

                    //Fill the player's food saturation 1.4x the amount that was fed to the player.
                    amountToSaturate = (float) (amountToFeed * saturation_multiplier);
                    if (amountToSaturate > playerFoodLevel + amountToFeed) {
                        amountToSaturate = playerFoodLevel + amountToFeed;
                    }
                    player.getHungerManager().setSaturationLevel(playerSaturationLevel + amountToSaturate);
                    remainingFoodNotches -= amountToFeed;
                }

                //Save the new nbt
                setTag(item, FOOD_NOTCHES_TAG, remainingFoodNotches);

                //Calculate the damage percentage.
                int chunkusDamageAmount = (int) (100 - (((double) remainingFoodNotches / (double) maxFoodNotches) * 100));
                if (remainingFoodNotches != maxFoodNotches && chunkusDamageAmount == 0) { chunkusDamageAmount = 1; }

                //Damage the item
                item.setDamage(chunkusDamageAmount);
            }

            //"Break" the item if it has no more food notches
            if (remainingFoodNotches <= 0) {
                return itemstack;
            } else {
                return item;
            }
        } else {
            //Entity is not a player, delete the item if eaten.
            return itemstack;
        }
    }

    private static void setTag(ItemStack item, String key, int value) {
        NbtCompound tag = item.getNbt();
        if (tag == null) {tag = new NbtCompound();}
        tag.putInt(key,value);
        item.setNbt(tag);
    }
}
