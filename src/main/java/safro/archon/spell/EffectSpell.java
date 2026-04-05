package safro.archon.spell;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import safro.archon.Archon;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.registry.SpellRegistry;

public class EffectSpell extends Spell {
    private final StatusEffectInstance instance;

    public EffectSpell(StatusEffectInstance instance, Element type, int manaCost) {
        super(type, manaCost);
        this.instance = instance;
    }

    @Override
    public void cast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack) {
        int amplifier = instance.getAmplifier();
        int duration = instance.getDuration() + MathHelper.floor(power.nonCriticalValue() * 20 * Archon.CONFIG.spellEffectDurationMultiplier);
        StatusEffectInstance effect = new StatusEffectInstance(instance.getEffectType(), duration, amplifier, instance.isAmbient(), instance.shouldShowParticles(), instance.shouldShowIcon());
        player.addStatusEffect(effect);

        // Hard-coded for Aqua Shield
        if (this == SpellRegistry.AQUA_SHIELD) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, duration, amplifier, false, false, true));
        }
    }

    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL;
    }
}
