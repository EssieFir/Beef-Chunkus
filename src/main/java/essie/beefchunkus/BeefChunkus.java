package essie.beefchunkus;

import com.mojang.logging.LogUtils;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BeefChunkus.MOD_ID)
public class BeefChunkus {

    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "beef_chunkus";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "beef_chunkus" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    // Create a Deferred Register to hold Items which will all be registered under the "beef_chunkus" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final FoodProperties BEEF_CHUNKUS_FOOD = (new FoodProperties.Builder()).build();

    // Creates a new Block with the id "beef_chunkus:example_block", combining the namespace and path
    // Creates a new BlockItem with the id "beef_chunkus:example_block", combining the namespace and path
    public static final RegistryObject<Item> BEEF_CHUNKUS = ITEMS.register("beef_chunkus", () -> new BeefChunkusItem(new Item.Properties().durability(100).food(BEEF_CHUNKUS_FOOD).tab(CreativeModeTab.TAB_FOOD)));

    public BeefChunkus() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the Deferred Register to the mod event bus so blocks get registered
        //BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
    }
}
