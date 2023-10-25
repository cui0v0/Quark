package vazkii.quark.content.world.config;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import vazkii.quark.base.module.config.Config;
import vazkii.quark.base.module.config.type.CompoundBiomeConfig;
import vazkii.quark.base.module.config.type.DimensionConfig;
import vazkii.quark.base.module.config.type.IConfigType;

public class BlossomTreeConfig implements IConfigType {

	@Config
	public DimensionConfig dimensions = DimensionConfig.overworld(false);
	
	@Config
	public CompoundBiomeConfig biomeConfig;
	
	@Config
	public int rarity;
	
	public BlossomTreeConfig(int rarity, TagKey<Biome> tag) {
		this.rarity = rarity;
		biomeConfig = CompoundBiomeConfig.fromBiomeTags(false, tag);
	}
	
}
