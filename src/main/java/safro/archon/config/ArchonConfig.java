package safro.archon.config;

import draylar.omegaconfig.api.Comment;
import draylar.omegaconfig.api.Config;
import draylar.omegaconfig.api.Syncing;
import java.util.HashMap;
import java.util.Map;

@Syncing
public class ArchonConfig implements Config {

    @Comment(
            """
            Sets the position of the mana display; Client-sided
            The x-offset is subtracted from the x pos of the middle of the screen. (Ex: 0 would make it right in the middle)
            The y-offset is subtracted from the y pos at the bottom of the screen. (Ex: 0 would make it at the very bottom of the screen)
            X-Offset Default: 180
            Y-Offset Default: 15
            """
    )
    public int mana_xoffset = 180;
    public int mana_yoffset = 15;

    @Comment(
            """
            Determines whether mana should only display if you have a mana item in your hand.
            Client-Sided, Accepts "true" or "false"
            Default: false (always shows mana)
            """
    )
    public boolean displayManaWithItem = false;

    @Comment(
            """
            Chance for a soul to drop when killing players and creatures using a soul scythe (bosses always drop a soul)
            The number should follow these bounds: 0 <= x <= 1.0
            Default: 0.05 (1/20)
            """
    )
    @Syncing
    public float soulDropChance = 0.05F;

    @Comment(
            """
            Weight/Chance for the Wizard Village House to spawn in villages
            Default: 10
            """
    )
    @Syncing
    public int wizard_village_weight = 10;

    @Comment(
            """
            Determines whether a sound should be played when using a channeler
            Client-Sided, Accepts "true" or "false"
            Default: true
            """
    )
    public boolean play_channel_sound = true;

    @Comment(
            """
            The chance that Harvesters will drop the bonus related to the mob
            The number should follow these bounds: 0 <= x <= 1.0
            Default: 0.05 (1/20)
            """
    )
    @Syncing
    public float harvester_chance = 0.05F;

    @Comment(
            """
            Whether screen shaking should be enabled or not. Used for players hit with the Rumble spell.
            Client-Sided, Accepts "true" or "false"
            Default: true
            """
    )
    public boolean enableScreenShake = true;

    @Comment("""
             The max amount of experience the standard experience pouch can hold.
             Default: 550
            """)
    @Syncing
    public int experiencePouchMax = 550;

    @Comment("""
             The max amount of experience the super experience pouch can hold.
             Default: 2920
            """)
    @Syncing
    public int superExperiencePouchMax = 2920;
    
    @Comment("""
              Enable or disable specific spells.
              Accepts "true" or "false"
              Example: "fireball": false to disable Fireball
              Default: true
             """)
    @Syncing
    public Map<String, Boolean> enabledSpells = new HashMap<>() {{
                put("fireball", true);
                put("incombustible", true);
                put("scorch", true);
                put("hellbeam", true);

                put("aqua_shield", true);
                put("freeze", true);
                put("mend", true);
                put("overcast", true);
                put("bubble_beam", true);

                put("propel", true);
                put("gust", true);
                put("thunder_strike", true);
                put("cloudshot", true);
                put("clearing_breeze", true);
                put("vacuum", true);

                put("rumble", true);
                put("crush", true);
                put("spike", true);
                put("terrain_toss", true);
                put("rage", true);

                put("darkball", true);
                put("swap", true);
                put("ender", true);
                put("shadow", true);
                put("astrofall", true);
                put("warp", true);
    }};

    @Comment("""
              Change specific spells' mana cost.
              Accepts integers.
              Example: "fireball": 40 to set Fireball's mana cost to 40.
             """)
    @Syncing
    public Map<String, Integer> spellManaCost = new HashMap<>() {{
        put("fireball", 20);
        put("incombustible", 50);
        put("scorch", 30);
        put("hellbeam", 10);

        put("aqua_shield", 100);
        put("freeze", 10);
        put("mend", 40);
        put("overcast", 90);
        put("bubble_beam", 10);

        put("propel", 10);
        put("gust", 20);
        put("thunder_strike", 30);
        put("cloudshot", 10);
        put("clearing_breeze", 90);
        put("vacuum", 50);

        put("rumble", 40);
        put("crush", 2);
        put("spike", 20);
        put("terrain_toss", 10);
        put("rage", 40);

        put("darkball", 10);
        put("swap", 40);
        put("ender", 20);
        put("shadow", 70);
        put("astrofall", 80);
        put("warp", 10);
    }};

    @Comment("""
              Change the duration of effects given by spells (in seconds).
              Accepts decimals.
             """)
    @Syncing
    public Map<String, Float> spellDuration = new HashMap<>() {{
        put("incombustible", 60.0f);
        put("aqua_shield", 10.0f);
        put("rage", 7.0f);
        put("shadow", 10.0f);
    }};

    @Comment("""
             The bonus multiplier for spell power to add to the effect's timer.
             Formula (result in seconds): effectDuration = effectDuration + spellPower * spellEffectDurationMultiplier
             Default: 2.0
            """)
    @Syncing
    public float spellEffectDurationMultiplier = 2.0f;

    @Comment("""
              Enable or disable specific soul summons.
              Accepts "true" or "false"
              Example: "hound_pack": false to disable Hound Pack
              Default: true
             """)
    @Syncing
    public Map<String, Boolean> enabledSummons = new HashMap<>() {{
        put("hound_pack", true);
        put("silver_swarm", true);
        put("archer", true);
        put("twin_knights", true);
        put("titan", true);
    }};

    @Comment("""
             Enables the alternate mend spell scaling formula:
             heal = maxPlayerHealth * maxMendHealingPercentage * min(maxBonusHealingMultiplier, 1 + mendSpellScaling * ln(1 + max(spellPower - minMendSpellPower, 0)))
             Default: false
            """)
    @Syncing
    public boolean enableAlternateMendScalingFormula = false;

    @Comment("""
             Maximum amount of health that can be restored by one use of the Mend spell.
             No effect if the alternate formula is disabled.
             Default: 0.1
            """)
    @Syncing
    public float maxMendHealingPercentage = 0.1f;

    @Comment("""
             Maximum bonus healing multiplier from additional spell power for maxMendHealingPercentage.
             No effect if the alternate formula is disabled.
             Default: 1.1
            """)
    @Syncing
    public float maxBonusHealingMultiplier = 1.1f;

    @Comment("""
             Spell power coefficient which the Mend spell scales with in the alternate formula.
             No effect if the alternate formula is disabled.
             Default: 0.03
            """)
    @Syncing
    public float mendSpellScaling = 0.03f;

    @Comment("""
             Minimum spell power to apply bonus healing when using the Mend spell.
             No effect if the alternate formula is disabled.
             Default: 5.0
            """)
    @Syncing
    public float minMendSpellPower = 5.0f;

    @Override
    public String getName() {
        return "archon";
    }

    @Override
    public String getExtension() {
        return "json5";
    }
}
