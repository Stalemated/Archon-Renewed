package safro.archon.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import safro.archon.Archon;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.item.SpellTomeItem;
import safro.archon.spell.*;
import safro.saflib.SafLib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellRegistry {
    public static final Registry<Spell> REGISTRY = FabricRegistryBuilder.createSimple(Spell.class, new Identifier(Archon.MODID, "spell")).buildAndRegister();
    public static final RegistryKey<Registry<Spell>> REGISTRY_KEY = RegistryKey.ofRegistry(new Identifier(Archon.MODID, "spell"));
    public static final Map<Element, List<Spell>> SPELLS = new HashMap<>();

    // Fire
    public static final Spell FIREBALL = register("fireball", new FireballSpell(Element.FIRE, Archon.CONFIG.spellManaCost.getOrDefault("fireball", 20)));
    public static final Spell INCOMBUSTIBLE = register("incombustible", new EffectSpell(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, Math.round(Archon.CONFIG.spellEffectDuration.getOrDefault("incombustible", 60.0f) * 20), 0), Element.FIRE, Archon.CONFIG.spellManaCost.getOrDefault("incombustible", 20)));
    public static final Spell SCORCH = register("scorch", new ScorchSpell(Element.FIRE, Archon.CONFIG.spellManaCost.getOrDefault("scorch", 30)));
    public static final Spell HELLBEAM = register("hellbeam", new HellbeamSpell(Element.FIRE, Archon.CONFIG.spellManaCost.getOrDefault("hellbeam", 10)));

    // Water
    public static final Spell AQUA_SHIELD = register("aqua_shield", new EffectSpell(new StatusEffectInstance(EffectRegistry.AQUA_SHIELD, Math.round(Archon.CONFIG.spellEffectDuration.getOrDefault("aqua_shield", 10.0f) * 20), Archon.CONFIG.spellEffectAmplifier.getOrDefault("aqua_shield", 2), false, false, true), Element.WATER, Archon.CONFIG.spellManaCost.getOrDefault("aqua_shield", 100)));
    public static final Spell FREEZE = register("freeze", new FreezeSpell(Element.WATER, Archon.CONFIG.spellManaCost.getOrDefault("freeze", 10)));
    public static final Spell MEND = register("mend", new MendSpell(Element.WATER, Archon.CONFIG.spellManaCost.getOrDefault("mend", 40)));
    public static final Spell OVERCAST = register("overcast", new WeatherSpell(Element.WATER, Archon.CONFIG.spellManaCost.getOrDefault("overcast", 90), true));
    public static final Spell BUBBLE_BEAM = register("bubble_beam", new BubbleBeamSpell(Element.WATER, Archon.CONFIG.spellManaCost.getOrDefault("bubble_beam", 10)));

    // Sky
    public static final Spell PROPEL = register("propel", new PropelSpell(Element.SKY, Archon.CONFIG.spellManaCost.getOrDefault("propel", 10)));
    public static final Spell GUST = register("gust", new GustSpell(Element.SKY, Archon.CONFIG.spellManaCost.getOrDefault("gust", 20)));
    public static final Spell THUNDER_STRIKE = register("thunder_strike", new ThunderStrikeSpell(Element.SKY, Archon.CONFIG.spellManaCost.getOrDefault("thunder_strike", 30)));
    public static final Spell CLOUDSHOT = register("cloudshot", new CloudshotSpell(Element.SKY, Archon.CONFIG.spellManaCost.getOrDefault("cloudshot", 10)));
    public static final Spell CLEARING_BREEZE = register("clearing_breeze", new WeatherSpell(Element.SKY, Archon.CONFIG.spellManaCost.getOrDefault("clearing_breeze", 90), false));
    public static final Spell VACUUM = register("vacuum", new VacuumSpell(Element.SKY, Archon.CONFIG.spellManaCost.getOrDefault("vacuum", 50)));

    // Earth
    public static final Spell RUMBLE = register("rumble", new RumbleSpell(Element.EARTH, Archon.CONFIG.spellManaCost.getOrDefault("rumble", 40)));
    public static final Spell CRUSH = register("crush", new CrushSpell(Element.EARTH, Archon.CONFIG.spellManaCost.getOrDefault("crush", 2)));
    public static final Spell SPIKE = register("spike", new SpikeSpell(Element.EARTH, Archon.CONFIG.spellManaCost.getOrDefault("spike", 20)));
    public static final Spell TERRAIN_TOSS = register("terrain_toss", new TerrainTossSpell(Element.EARTH, Archon.CONFIG.spellManaCost.getOrDefault("terrain_toss", 10)));
    public static final Spell RAGE = register("rage", new EffectSpell(new StatusEffectInstance(EffectRegistry.RAGE, Math.round(Archon.CONFIG.spellEffectDuration.getOrDefault("rage", 7.0f) * 20), Archon.CONFIG.spellEffectAmplifier.getOrDefault("rage", 0), false, false, true), Element.EARTH, Archon.CONFIG.spellManaCost.getOrDefault("rage", 40)));

    // End
    public static final Spell DARKBALL = register("darkball", new DarkballSpell(Element.END, Archon.CONFIG.spellManaCost.getOrDefault("darkball", 10)));
    public static final Spell SWAP = register("swap", new SwapSpell(Element.END, Archon.CONFIG.spellManaCost.getOrDefault("swap", 40)));
    public static final Spell ENDER = register("ender", new EnderSpell(Element.END, Archon.CONFIG.spellManaCost.getOrDefault("ender", 20)));
    public static final Spell SHADOW = register("shadow", new EffectSpell(new StatusEffectInstance(EffectRegistry.SHADOW, Math.round(Archon.CONFIG.spellEffectDuration.getOrDefault("shadow", 10.0f) * 20), 0, false, false, true), Element.END, Archon.CONFIG.spellManaCost.getOrDefault("shadow", 70)));
    public static final Spell ASTROFALL = register("astrofall", new AstrofallSpell(Element.END, Archon.CONFIG.spellManaCost.getOrDefault("astrofall", 80)));
    public static final Spell WARP = register("warp", new WarpSpell(Element.END, Archon.CONFIG.spellManaCost.getOrDefault("warp", 10)));

    public static void init() {
    }

    private static Spell register(String name, Spell spell) {
        if (Archon.CONFIG.enabledSpells.getOrDefault(name, true)) {
            createTome(name + "_tome", spell);
        }
        addToLists(spell);
        return Registry.register(REGISTRY, new Identifier(Archon.MODID, name), spell);
    }

    public static SpellTomeItem createTome(String name, Spell spell) {
        SpellTomeItem item = Registry.register(Registries.ITEM, new Identifier(Archon.MODID, name), new SpellTomeItem(spell, new FabricItemSettings().maxCount(1)));
        SafLib.ITEMS.add(new ItemStack(item));
        return item;
    }

    public static Item getTome(Spell spell) {
        Identifier id = REGISTRY.getId(spell);
        if (id == null) return null;
        String s = id.toString();
        if (!Archon.CONFIG.enabledSpells.getOrDefault(s, true)) return null;
        Identifier tome = new Identifier(id.getNamespace(), id.getPath() + "_tome");
        if (Registries.ITEM.containsId(tome)) {
             return Registries.ITEM.get(tome);
        }
        return null;
    }

    private static void addToLists(Spell spell) {
        if (SPELLS.isEmpty()) {
            for (Element e : Element.values()) {
                SPELLS.put(e, new ArrayList<>());
            }
        }
        SPELLS.get(spell.getElement()).add(spell);
    }
}
