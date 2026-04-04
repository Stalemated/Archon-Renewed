package safro.archon.registry;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.item.SoulTomeItem;

import java.util.Arrays;
import java.util.List;

public class LootTableRegistry {
    public static final Identifier[] VILLAGE_CHESTS = new Identifier[]{LootTables.VILLAGE_WEAPONSMITH_CHEST, LootTables.VILLAGE_TOOLSMITH_CHEST, LootTables.VILLAGE_ARMORER_CHEST, LootTables.VILLAGE_CARTOGRAPHER_CHEST, LootTables.VILLAGE_MASON_CHEST, LootTables.VILLAGE_SHEPARD_CHEST, LootTables.VILLAGE_BUTCHER_CHEST, LootTables.VILLAGE_FLETCHER_CHEST, LootTables.VILLAGE_FISHER_CHEST, LootTables.VILLAGE_TANNERY_CHEST, LootTables.VILLAGE_TEMPLE_CHEST, LootTables.VILLAGE_DESERT_HOUSE_CHEST, LootTables.VILLAGE_PLAINS_CHEST, LootTables.VILLAGE_TAIGA_HOUSE_CHEST, LootTables.VILLAGE_SNOWY_HOUSE_CHEST, LootTables.VILLAGE_SAVANNA_HOUSE_CHEST};

    public static void init() {
        // Spell Tomes
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (LootTables.NETHER_BRIDGE_CHEST.equals(id) || LootTables.BASTION_TREASURE_CHEST.equals(id)) {
                LootPool pool = createForSpell(0.2F, SpellRegistry.SPELLS.get(Element.FIRE));
                if (pool != null) tableBuilder.pool(pool);
            } else if (LootTables.FISHING_TREASURE_GAMEPLAY.equals(id) || LootTables.UNDERWATER_RUIN_BIG_CHEST.equals(id)) {
                LootPool pool = createForSpell(0.5F, SpellRegistry.SPELLS.get(Element.WATER));
                if (pool != null) tableBuilder.pool(pool);
            } else if (Arrays.stream(VILLAGE_CHESTS).toList().contains(id)) {
                LootPool pool = createForSpell(0.06F, SpellRegistry.SPELLS.get(Element.SKY));
                if (pool != null) tableBuilder.pool(pool);
            } else if (LootTables.ABANDONED_MINESHAFT_CHEST.equals(id) || LootTables.DESERT_PYRAMID_CHEST.equals(id)) {
                LootPool pool = createForSpell(0.22F, SpellRegistry.SPELLS.get(Element.EARTH));
                if (pool != null) tableBuilder.pool(pool);
            } else if (LootTables.END_CITY_TREASURE_CHEST.equals(id) || LootTables.STRONGHOLD_LIBRARY_CHEST.equals(id)) {
                LootPool pool = createForSpell(0.17F, SpellRegistry.SPELLS.get(Element.END));
                if (pool != null) tableBuilder.pool(pool);
            }
        });

        // Soul Tomes
        LootTableEvents.MODIFY.register(((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (LootTables.ANCIENT_CITY_CHEST.equals(id)) {
                LootPool pool = createForSummon(0.4F);
                tableBuilder.pool(pool);
            } else if (LootTables.BASTION_TREASURE_CHEST.equals(id) || LootTables.BASTION_BRIDGE_CHEST.equals(id)) {
                LootPool pool = createForSummon(0.35F);
                tableBuilder.pool(pool);
            }
        }));
    }

    private static LootPool createForSpell(float chance, List<Spell> spells) {
        LootPool.Builder builder = LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).conditionally(RandomChanceLootCondition.builder(chance));
        int validSpells = 0;
        for (Spell s : spells) {
            if (SpellRegistry.getTome(s) != null) validSpells++;
        }
        if (validSpells == 0) return null;
        
        int weight = 100 / validSpells;
        for (Spell s : spells) {
            Item tome = SpellRegistry.getTome(s);
            if (tome != null) {
                builder.with(ItemEntry.builder(tome).weight(weight).build());
            }
        }
        return builder.build();
    }

    private static LootPool createForSummon(float chance) {
        LootPool.Builder builder = LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).conditionally(RandomChanceLootCondition.builder(chance));
        int weight = 100 / SummonRegistry.TOMES.size();
        for (SoulTomeItem item : SummonRegistry.TOMES) {
            builder.with(ItemEntry.builder(item).weight(weight).build());
        }
        return builder.build();
    }
}