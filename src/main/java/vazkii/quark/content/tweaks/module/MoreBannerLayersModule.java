package vazkii.quark.content.tweaks.module;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import vazkii.zeta.module.ZetaLoadModule;
import vazkii.zeta.module.ZetaModule;
import vazkii.quark.base.module.config.Config;
import vazkii.zeta.util.Hint;
import vazkii.zeta.event.ZConfigChanged;
import vazkii.zeta.event.bus.LoadEvent;

@ZetaLoadModule(category = "tweaks")
public class MoreBannerLayersModule extends ZetaModule {

	@Config
	@Config.Min(1)
	@Config.Max(16)
	public static int layerLimit = 16;

	@Hint(key = "banner_layer_buff", content = "layerLimit")
	public static final TagKey<Item> banners = ItemTags.BANNERS;

	private static boolean staticEnabled;

	@LoadEvent
	public final void configChanged(ZConfigChanged event) {
		staticEnabled = enabled;
	}

	public static int getLimit(int curr) {
		return staticEnabled ? layerLimit : curr;
	}

}
