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
            Determines whether the a sound should be played when using a channeler
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
              Example: "archon:fireball": false to disable Fireball
              Default: true
             """)
    @Syncing
    public Map<String, Boolean> enabledSpells = new HashMap<>() {{
                put("archon:fireball", true);
                put("archon:incombustible", true);
                put("archon:scorch", true);
                put("archon:hellbeam", true);

                put("archon:aqua_shield", true);
                put("archon:freeze", true);
                put("archon:mend", true);
                put("archon:overcast", true);
                put("archon:bubble_beam", true);

                put("archon:propel", true);
                put("archon:gust", true);
                put("archon:thunder_strike", true);
                put("archon:cloudshot", true);
                put("archon:clearing_breeze", true);
                put("archon:vacuum", true);

                put("archon:rumble", true);
                put("archon:crush", true);
                put("archon:spike", true);
                put("archon:terrain_toss", true);
                put("archon:rage", true);

                put("archon:darkball", true);
                put("archon:swap", true);
                put("archon:ender", true);
                put("archon:shadow", true);
                put("archon:astrofall", true);
                put("archon:warp", true);
    }};

    @Comment("""
              Enable or disable specific soul summons.
              Accepts "true" or "false"
              Example: "archon:hound_pack": false to disable Hound Pack
              Default: true
             """)
    @Syncing
    public Map<String, Boolean> enabledSummons = new HashMap<>() {{
        put("archon:hound_pack", true);
        put("archon:silver_swarm", true);
        put("archon:archer", true);
        put("archon:twin_knights", true);
        put("archon:titan", true);
    }};

    @Override
    public String getName() {
        return "archon";
    }

    @Override
    public String getExtension() {
        return "json5";
    }
}
