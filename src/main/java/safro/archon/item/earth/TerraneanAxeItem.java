package safro.archon.item.earth;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.Archon;
import safro.archon.registry.EffectRegistry;
import safro.archon.registry.MiscRegistry;
import safro.archon.util.ArchonUtil;

import java.util.List;

public class TerraneanAxeItem extends SwordItem {

    public TerraneanAxeItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    public int getManaCost() {
        return Archon.CONFIG.terraneanAxeManaCost;
    }

    public boolean activate(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, Math.round(Archon.CONFIG.terraneanAxeEffectDuration.getOrDefault("resistance", 20.0f) * 20), Archon.CONFIG.terraneanAxeResistanceAmplifier, true, false));
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.STURDY, Math.round(Archon.CONFIG.terraneanAxeEffectDuration.getOrDefault("sturdy", 20.0f) * 20), 0, true, false));
        player.swingHand(hand);
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (ArchonUtil.canRemoveMana(player, getManaCost()) && activate(world, player, stack, hand)) {
            if (EnchantmentHelper.getLevel(MiscRegistry.ARCANE, stack) >= 1) {
                int removed = (int) (getManaCost() * 0.2);
                ArchonUtil.get(player).removeMana(getManaCost() - removed);
            } else {
                ArchonUtil.get(player).removeMana(getManaCost());
            }
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(stack);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("text.archon.terranean1").formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("text.archon.shift"));
        }
        tooltip.add(ArchonUtil.createManaText(getManaCost(), true));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
