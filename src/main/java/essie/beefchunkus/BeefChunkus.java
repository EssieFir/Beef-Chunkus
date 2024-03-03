package essie.beefchunkus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.minecraft.util.registry.Registry.ITEM;

public class BeefChunkus implements ModInitializer {
    /**
     * Runs the mod initializer.
     */
    public static final String MOD_ID = "beef_chunkus";
    public static final FoodComponent BEEF_CHUNKUS_FOOD = (new FoodComponent.Builder()).hunger(7).saturationModifier(0.5f).build();
    public static final Item BEEF_CHUNKUS = new BeefChunkusItem(new FabricItemSettings().food(BEEF_CHUNKUS_FOOD).maxDamage(10).group(ItemGroup.FOOD));
    @Override
    public void onInitialize() {
        Registry.register(ITEM, new Identifier(MOD_ID, "beef_chunkus"), BEEF_CHUNKUS);
    }
}
