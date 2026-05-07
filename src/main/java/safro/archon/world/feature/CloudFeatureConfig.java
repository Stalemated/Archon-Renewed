package safro.archon.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.List;

public record CloudFeatureConfig(List<Identifier> structures) implements FeatureConfig {
    public static final Codec<CloudFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.listOf().fieldOf("structures").forGetter(CloudFeatureConfig::structures)
    ).apply(instance, CloudFeatureConfig::new));
}