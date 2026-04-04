package safro.archon.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.Archon;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.api.spell.SpellAttributable;
import safro.archon.enchantment.ArcaneEnchantment;
import safro.archon.registry.SpellRegistry;
import safro.archon.util.ArchonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Reimplementation of WandItem that extends SwordItem instead
public class SpellWeaponItem extends SwordItem implements SpellAttributable {
    private final Element type;
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public SpellWeaponItem(ToolMaterial toolMaterial, Element element, int power, double critDmg, double critChance, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.type = element;

        float damage = (float)attackDamage + toolMaterial.getAttackDamage();
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", damage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION));

        addPower(builder, element, power);
        if (critDmg > 0.0D) {
            addCritDamage(builder, critDmg);
        }

        if (critChance > 0.0D) {
            addCritChance(builder, critChance);
        }

        this.attributeModifiers = builder.build();
    }

    @Override
    public Element getElement() {
        return this.type;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!world.isClient) {
            if (getSpells(player).isEmpty()) {
                player.sendMessage(Text.translatable("text.archon.invalid_spell").formatted(Formatting.RED), true);
                return TypedActionResult.pass(stack);
            }

            Spell current = getCurrentSpell(stack, player);
            if (player.isSneaking()) {
                this.cycleSpells(stack, player);
                Spell spell = getCurrentSpell(stack, player);
                if (spell != null) {
                    player.sendMessage(Text.translatable(spell.getTranslationKey()).formatted(Formatting.GREEN), true);
                }
                return TypedActionResult.success(stack);

            } else if (current != null && !current.isBlockCasted() && current.canCast(world, player, stack)) {
                current.cast(world, player, SpellPower.getSpellPower(this.getElement().getSchool(), player), stack);
                ArcaneEnchantment.applyArcane(player, stack, current.getManaCost());

                if (current.getCastSound() != null) {
                    world.playSound(null, player.getBlockPos(), current.getCastSound(), SoundCategory.PLAYERS, 0.9F, 1.0F);
                }
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.pass(stack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        PlayerEntity player = context.getPlayer();
        Spell current = getCurrentSpell(stack, player);

        if (!context.getWorld().isClient() && player != null) {
            ServerWorld world = (ServerWorld) context.getWorld();
            if (current != null && current.isBlockCasted() && ArchonUtil.canRemoveMana(player, current.getManaCost())) {
                if (current.castOnBlock(world, context.getBlockPos(), player, stack) == ActionResult.SUCCESS) {
                    ArcaneEnchantment.applyArcane(player, stack, current.getManaCost());

                    if (current.getCastSound() != null) {
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), current.getCastSound(), SoundCategory.PLAYERS, 0.9F, 1.0F);
                    }
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.PASS;
    }

    @Nullable
    public Spell getCurrentSpell(ItemStack stack, PlayerEntity player) {
        if (stack.getOrCreateSubNbt(Archon.MODID).contains("CurrentSpell")) {
            String name = stack.getOrCreateSubNbt(Archon.MODID).getString("CurrentSpell");
            Spell spell = SpellRegistry.REGISTRY.get(new Identifier(name));
            if (spell != null && Archon.CONFIG.enabledSpells.getOrDefault(name, true)) {
                return spell;
            }
        }

        List<Spell> spells = getSpells(player);
        if (!spells.isEmpty()) {
            Spell spell = spells.get(0);
            Identifier id = SpellRegistry.REGISTRY.getId(spell);
            if (id != null) {
                stack.getOrCreateSubNbt(Archon.MODID).putString("CurrentSpell", id.toString());
                return spell;
            }
        }
        return null;
    }

    public void cycleSpells(ItemStack stack, PlayerEntity player) {
        List<Spell> spells = getSpells(player);
        if (spells.size() > 1) {
            List<Spell> allSpells = ArchonUtil.getSpells(player);
            Spell currentSpell = getCurrentSpell(stack, player);
            if (currentSpell != null) {
                int currentIndex = allSpells.indexOf(currentSpell);
                if (currentIndex >= 0) {
                    Collections.rotate(allSpells, -currentIndex);
                }
            }
            
            do {
                Collections.rotate(allSpells, -1);
                Spell nextSpell = allSpells.get(0);
                Identifier id = SpellRegistry.REGISTRY.getId(nextSpell);
                if (id != null) {
                    stack.getOrCreateSubNbt(Archon.MODID).putString("CurrentSpell", id.toString());
                }
            } while (getCurrentSpell(stack, player) == null || getCurrentSpell(stack, player).getElement() != this.getElement());
        }
    }

    public ArrayList<Spell> getSpells(PlayerEntity player) {
        ArrayList<Spell> list = new ArrayList<>();
        for (Spell spell : ArchonUtil.getSpells(player)) {
            Identifier id = SpellRegistry.REGISTRY.getId(spell);
            if (id != null && Archon.CONFIG.enabledSpells.getOrDefault(id.toString(), true) && spell.getElement() == this.getElement()) {
                list.add(spell);
            }
        }
        return list;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        Spell spell = null;
        if (stack.getOrCreateSubNbt(Archon.MODID).contains("CurrentSpell")) {
            String name = stack.getOrCreateSubNbt(Archon.MODID).getString("CurrentSpell");
            Spell candidate = SpellRegistry.REGISTRY.get(new Identifier(name));
            if (candidate != null && Archon.CONFIG.enabledSpells.getOrDefault(name, true)) {
                spell = candidate;
            }
        }
        
        if (spell != null) {
            tooltip.add(Text.translatable("text.archon.current_spell", Text.translatable(spell.getTranslationKey()).getString()).formatted(Formatting.GRAY));
            tooltip.add(ArchonUtil.createManaText(spell.getManaCost(), false));
        } else {
            tooltip.add(Text.translatable("text.archon.none").formatted(Formatting.GRAY));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
