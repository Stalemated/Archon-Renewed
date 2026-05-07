package safro.archon.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import safro.archon.Archon;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CloudFeature extends Feature<CloudFeatureConfig> {

    public CloudFeature(Codec<CloudFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<CloudFeatureConfig> context) {
        if (!Archon.CONFIG.enableCloudGeneration) return false;

        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();
        List<Identifier> structures = context.getConfig().structures();

        if (structures.isEmpty()) {
            return false;
        }

        Identifier randomCloudId = structures.get(random.nextInt(structures.size()));
        StructureTemplateManager templateManager = Objects.requireNonNull(world.getServer()).getStructureTemplateManager();

        Optional<StructureTemplate> template = templateManager.getTemplate(randomCloudId);
        if (template.isEmpty()) {
            Archon.LOGGER.warn("Cloud structure template {} not found!", randomCloudId);
            return false;
        }

        BlockRotation rotation = BlockRotation.random(random);

        ChunkPos chunkPos = new ChunkPos(pos);
        BlockBox safeBounds = new BlockBox(
                chunkPos.getStartX() - 16, world.getBottomY(), chunkPos.getStartZ() - 16,
                chunkPos.getEndX() + 16, world.getTopY(), chunkPos.getEndZ() + 16
        );

        StructurePlacementData placementData = new StructurePlacementData()
                .setRotation(rotation)
                .setMirror(BlockMirror.NONE)
                .setBoundingBox(safeBounds)
                .setRandom(random);

        Vec3i size = template.get().getRotatedSize(rotation);
        BlockPos placePos = pos.add(-size.getX() / 2, 0, -size.getZ() / 2);

        template.get().place(world, placePos, placePos, placementData, random, Block.NOTIFY_LISTENERS);

        return true;
    }
}
