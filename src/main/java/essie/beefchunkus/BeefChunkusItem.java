package essie.beefchunkus;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BeefChunkusItem extends Item {

    private static final String FOOD_NOTCHES_TAG = "beef_chunkus:food_notches";
    private static final String MAX_FOOD_NOTCHES_TAG = "beef_chunkus:max_food_notches";

    public BeefChunkusItem(Properties properties) {
        super(properties);
    }
    @Override
    public boolean isEnchantable(ItemStack pStack) { return false; }
    @Override
    public boolean isRepairable(ItemStack pStack) { return false; }

    @Override
    public boolean canBeDepleted() {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack item) {
        return item.getItem().isDamaged(item);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pContext) {

        CompoundTag compoundTag = pStack.getTag();

        final int DEFAULT_FOOD_NOTCHES = BeefChunkusConfig.default_food_notches.get();
        final int DEFAULT_MAX_FOOD_NOTCHES = BeefChunkusConfig.default_max_food_notches.get();

        if (compoundTag == null) { compoundTag = new CompoundTag(); }

        //Check if the item has the custom nbt data, if not, add it.
        if (compoundTag.get(FOOD_NOTCHES_TAG) == null) {
            setTag(pStack, FOOD_NOTCHES_TAG, DEFAULT_FOOD_NOTCHES);
        }
        if (compoundTag.get(MAX_FOOD_NOTCHES_TAG) == null) {
            setTag(pStack, MAX_FOOD_NOTCHES_TAG, DEFAULT_MAX_FOOD_NOTCHES);
        }

        int remainingFoodNotches = compoundTag.getInt(FOOD_NOTCHES_TAG);
        int maxFoodNotches = compoundTag.getInt(MAX_FOOD_NOTCHES_TAG);

        pTooltip.add(Component.translatable("tooltip.beef_chunkus.remaining_notches",
                (Component.literal(String.valueOf(remainingFoodNotches))), Component.literal(String.valueOf(maxFoodNotches))).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {

        final double SATURATION_MULTIPLIER = BeefChunkusConfig.saturation_multiplier.get();
        final int DEFAULT_FOOD_NOTCHES = BeefChunkusConfig.default_food_notches.get();
        final int DEFAULT_MAX_FOOD_NOTCHES = BeefChunkusConfig.default_max_food_notches.get();

        //itemstack is consumed and set to air when passed through finishUsingItem, so copy it before it gets deleted.
        ItemStack initialItem = pStack.copy();
        ItemStack itemstack = super.finishUsingItem(pStack, pLevel, pEntityLiving);
        ItemStack item = new ItemStack(BeefChunkus.BEEF_CHUNKUS.get());

        item.setTag(initialItem.getTag());

        if (pEntityLiving instanceof Player player) {

            boolean isCreative = player.getAbilities().instabuild;

            CompoundTag initialTag = initialItem.getTag();

            if (initialTag == null) { initialTag = new CompoundTag(); }

            //Check if the item has the custom nbt data, if not, add it.
            if (initialTag.get(FOOD_NOTCHES_TAG) == null) {
                setTag(pStack, FOOD_NOTCHES_TAG, DEFAULT_FOOD_NOTCHES);
            }
            if (initialTag.get(MAX_FOOD_NOTCHES_TAG) == null) {
                setTag(pStack, MAX_FOOD_NOTCHES_TAG, DEFAULT_MAX_FOOD_NOTCHES);
            }

            int remainingFoodNotches = initialTag.getInt(FOOD_NOTCHES_TAG);
            int maxFoodNotches = initialTag.getInt(MAX_FOOD_NOTCHES_TAG);

            int amountToFeed = 20 - player.getFoodData().getFoodLevel();
            float amountToSaturate;

            if (isCreative) {
                player.getFoodData().setFoodLevel(20);
                player.getFoodData().setSaturation(20);
            } else {
                //Feed the player
                int playerFoodLevel = player.getFoodData().getFoodLevel();
                float playerSaturationLevel = player.getFoodData().getSaturationLevel();
                //If the player's required food level (amount to feed) is larger than what the food item has,
                //use the remaining notches of food to fill the player
                if (amountToFeed > remainingFoodNotches) {
                    player.getFoodData().setFoodLevel(playerFoodLevel + remainingFoodNotches);
                    if (playerFoodLevel >= 20) {
                        player.getFoodData().setFoodLevel(20);
                    }

                    //Fill the player's food saturation 1.4x the amount that was fed to the player.
                    amountToSaturate = (float) (remainingFoodNotches * SATURATION_MULTIPLIER);
                    if (amountToSaturate > playerFoodLevel + remainingFoodNotches) {
                        amountToSaturate = playerFoodLevel + remainingFoodNotches;
                    }
                    player.getFoodData().setSaturation(playerSaturationLevel + amountToSaturate);

                    remainingFoodNotches = 0;

                } else {
                    //If the amountToFeed is larger than the remaining Notches, just fill the player to full.
                    player.getFoodData().setFoodLevel(20);

                    //Fill the player's food saturation 1.4x the amount that was fed to the player.
                    amountToSaturate = (float) (amountToFeed * SATURATION_MULTIPLIER);
                    if (amountToSaturate > playerFoodLevel + amountToFeed) {
                        amountToSaturate = playerFoodLevel + amountToFeed;
                    }
                    player.getFoodData().setSaturation(playerSaturationLevel + amountToSaturate);
                    remainingFoodNotches -= amountToFeed;
                }

                //Save the new nbt
                setTag(item, FOOD_NOTCHES_TAG, remainingFoodNotches);

                //Calculate the damage percentage.
                int chunkusDamageAmount = (int) (100 - (((double) remainingFoodNotches / (double) maxFoodNotches) * 100));
                if (remainingFoodNotches != maxFoodNotches && chunkusDamageAmount == 0) { chunkusDamageAmount = 1; }

                //Damage the item
                item.setDamageValue(chunkusDamageAmount);
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
        CompoundTag tag = item.getTag();
        if (tag == null) { tag = new CompoundTag(); }
        tag.putInt(key,value);
        item.setTag(tag);
    }
}
