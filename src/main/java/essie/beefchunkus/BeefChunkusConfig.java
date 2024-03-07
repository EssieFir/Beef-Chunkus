package essie.beefchunkus;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BeefChunkusConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.ConfigValue<Double> saturation_multiplier;
    public static ForgeConfigSpec.ConfigValue<Integer> default_food_notches;
    public static ForgeConfigSpec.ConfigValue<Integer> default_max_food_notches;

    static {
        BUILDER.push("Beef Chunkus Config");

        saturation_multiplier = BUILDER.comment("The Saturation multiplier used when the player eats Beef Chunkus, default = 1.4")
                .define("Saturation Multiplier", 1.4);
        default_food_notches = BUILDER.comment("The default amount of notches Beef Chunkus has when obtained, default = 70")
                .define("Default Food Notches", 70);
        default_max_food_notches = BUILDER.comment("The default maximum amount of notches Beef Chunkus has when obtained, default = 70")
                .define("Default Max Food Notches", 70);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
