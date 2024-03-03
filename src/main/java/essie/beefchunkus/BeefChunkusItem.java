package essie.beefchunkus;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BeefChunkusItem extends Item {
    public BeefChunkusItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        //Get the durability of the item before it is consumed.
        ItemStack initialItem = pStack.copy();

        ItemStack itemstack = super.finishUsingItem(pStack, pLevel, pEntityLiving);
        ItemStack item = new ItemStack(BeefChunkus.BEEF_CHUNKUS.get());
        boolean isCreative = ((Player) pEntityLiving).getAbilities().instabuild;

        item.setTag(initialItem.getTag());
        item.setDamageValue(initialItem.getDamageValue() + 1);

        if (item.getDamageValue() >= 10 || isCreative) {
            return itemstack;
        } else {
            return item;
        }
    }
}
