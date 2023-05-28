package vazkii.quark.content.tweaks.module;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.Quark;
import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.block.QuarkInheritedPaneBlock;
import vazkii.quark.base.item.QuarkItem;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.content.tweaks.block.DirtyGlassBlock;

/**
 * @author WireSegal
 * Created at 12:26 PM on 8/24/19.
 */
@LoadModule(category = ModuleCategory.TWEAKS, hasSubscriptions = true)
public class GlassShardModule extends QuarkModule {

	public static QuarkBlock dirtyGlass;

	public static TagKey<Item> shardTag;

	public static Item clearShard;
	public static Item dirtyShard;

	public static final Map<DyeColor, Item> shardColors = new HashMap<>();

	@Override
	public void register() {
		dirtyGlass = new DirtyGlassBlock("dirty_glass", this, CreativeModeTabs.BUILDING_BLOCKS,
				Block.Properties.of(Material.GLASS, MaterialColor.COLOR_BROWN).strength(0.3F).sound(SoundType.GLASS));
		new QuarkInheritedPaneBlock(dirtyGlass);

		clearShard = new QuarkItem("clear_shard", this, new Item.Properties());
		dirtyShard = new QuarkItem("dirty_shard", this, new Item.Properties());
		
		RegistryHelper.setCreativeTab(clearShard, CreativeModeTabs.INGREDIENTS);
		RegistryHelper.setCreativeTab(dirtyShard, CreativeModeTabs.INGREDIENTS);

		for(DyeColor color : DyeColor.values()) {
			Item item = new QuarkItem(color.getSerializedName() + "_shard", this, new Item.Properties());
			RegistryHelper.setCreativeTab(item, CreativeModeTabs.INGREDIENTS);
			
			shardColors.put(color, item);
		}
	}

	@Override
	public void setup() {
		shardTag = ItemTags.create(new ResourceLocation(Quark.MOD_ID, "shards"));
	}
}
