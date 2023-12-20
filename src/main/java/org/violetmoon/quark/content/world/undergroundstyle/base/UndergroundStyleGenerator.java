package org.violetmoon.quark.content.world.undergroundstyle.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;

import net.minecraft.world.level.levelgen.Heightmap;
import org.violetmoon.quark.base.world.generator.multichunk.ClusterBasedGenerator;

import java.util.Random;

public class UndergroundStyleGenerator<T extends UndergroundStyle> extends ClusterBasedGenerator {

	public final UndergroundStyleConfig<T> info;

	public UndergroundStyleGenerator(UndergroundStyleConfig<T> info, String name) {
		super(info.dimensions, info, name.hashCode());
		this.info = info;
	}

	@Override
	public int getFeatureRadius() {
		return info.horizontalSize + info.horizontalVariation;
	}

	@Override
	public BlockPos[] getSourcesInChunk(WorldGenRegion world, Random random, ChunkGenerator generator, BlockPos chunkCorner) {
		if(info.rarity > 0 && random.nextInt(info.rarity) == 0) {
			int x = chunkCorner.getX() + random.nextInt(16);
			int y = random.nextInt(info.maxYLevel - info.minYLevel);
			int z = chunkCorner.getZ() + random.nextInt(16);
			BlockPos pos = new BlockPos(x, y, z);

			//check the biome at world height, and don't start blobs unless theyre actually underground
			if(info.biomes.canSpawn(getBiome(world, pos, true)) && world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) >= y)
				return new BlockPos[]{ pos };
		}

		return new BlockPos[0];
	}

	@Override
	public IGenerationContext createContext(BlockPos src, ChunkGenerator generator, Random random, BlockPos chunkCorner, WorldGenRegion world) {
		return new Context(world, src, generator, random, info);
	}

	@Override
	public String toString() {
		return "UndergroundBiomeGenerator[" + info.biomeObj + "]";
	}

	public static class Context implements IGenerationContext {

		public final WorldGenRegion world;
		public final BlockPos source;
		public final ChunkGenerator generator;
		public final Random random;
		public final UndergroundStyleConfig<?> info;

		public Context(WorldGenRegion world, BlockPos source, ChunkGenerator generator, Random random, UndergroundStyleConfig<?> info) {
			this.world = world;
			this.source = source;
			this.generator = generator;
			this.random = random;
			this.info = info;
		}

		@Override
		public void consume(BlockPos pos, double noise) {
			info.biomeObj.fill(this, pos);
		}

	}
}
