package essie.beefchunkus;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.loader.api.FabricLoader;
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
    public static final FoodComponent BEEF_CHUNKUS_FOOD = (new FoodComponent.Builder()).build();
    public static final Item BEEF_CHUNKUS = new BeefChunkusItem(new FabricItemSettings().food(BEEF_CHUNKUS_FOOD).maxDamage(100).group(ItemGroup.FOOD));

    public static final boolean CLOTH_CONFIG_INSTALLED = FabricLoader.getInstance().isModLoaded("cloth-config");

    public static double saturation_multiplier = 1.4;
    public static int default_food_notches = 70;
    public static int default_max_food_notches = 70;

    @Override
    public void onInitialize() {
        if (CLOTH_CONFIG_INSTALLED) {
            AutoConfig.register(BeefChunkusConfig.class, GsonConfigSerializer::new);
            BeefChunkusConfig config = AutoConfig.getConfigHolder(BeefChunkusConfig.class).getConfig();
            saturation_multiplier = config.saturation_multiplier;
            default_food_notches = config.default_food_notches;
            default_max_food_notches = config.default_max_food_notches;
        }

        Registry.register(ITEM, new Identifier(MOD_ID, "beef_chunkus"), BEEF_CHUNKUS);
    }
}
