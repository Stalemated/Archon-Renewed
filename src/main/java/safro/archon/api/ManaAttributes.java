package safro.archon.api;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import safro.archon.Archon;

import java.util.UUID;

public class ManaAttributes {
    public static final ClampedEntityAttribute MAX_MANA = register("max_mana", 100, 1, 1024);
    public static final ClampedEntityAttribute MANA_REGEN_SPEED = register("mana_regen_speed", Archon.CONFIG.manaRegenTickAmount, 1, 200);

    public static final UUID CAPACITY_SCROLL_MODIFIER = UUID.fromString("26a3cd44-900c-4a88-8562-75fc31690ebe");
    public static final UUID MAX_ITEM_MODIFIER = UUID.fromString("d8b87e74-5699-483a-9644-4e16311a8d27");
    public static final UUID MANA_BOOST_MODIFIER = UUID.fromString("b3c2b78f-f1bd-47f0-8b5e-a11dff725fc6");
    public static final UUID ACCELERATE_SCROLL_MODIFIER = UUID.fromString("6a0c5b52-25e6-42bc-9d0a-9d9f9308cc4b");

    private static ClampedEntityAttribute register(String name, double base, double min, double max) {
        ClampedEntityAttribute attribute = (ClampedEntityAttribute) new ClampedEntityAttribute("attribute.name.generic." + name, base, min, max).setTracked(true);
        return Registry.register(Registries.ATTRIBUTE, new Identifier(Archon.MODID, name), attribute);
    }

    public static void init() {}
}
