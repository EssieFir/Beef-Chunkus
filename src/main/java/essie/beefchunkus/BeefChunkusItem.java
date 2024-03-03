package essie.beefchunkus;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BeefChunkusItem extends Item {
    public BeefChunkusItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        //Get the durability of the item before it is consumed.
        ItemStack initialItem = stack.copy();

        ItemStack itemstack = this.isFood() ? user.eatFood(world, stack) : stack;
        ItemStack item = new ItemStack(BeefChunkus.BEEF_CHUNKUS);
        boolean isCreative = user instanceof PlayerEntity && ((PlayerEntity) user).getAbilities().creativeMode;

        item.setNbt(initialItem.getNbt());
        item.setDamage(initialItem.getDamage() + 1);

        if (item.getDamage() >= 10 || isCreative) {
            return itemstack;
        } else {
            return item;
        }
    }
}
