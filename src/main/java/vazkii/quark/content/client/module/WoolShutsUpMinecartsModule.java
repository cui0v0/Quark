package vazkii.quark.content.client.module;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import vazkii.zeta.module.ZetaLoadModule;
import vazkii.zeta.module.ZetaModule;
import vazkii.zeta.util.Hint;
import vazkii.zeta.event.ZConfigChanged;
import vazkii.zeta.event.bus.LoadEvent;

@ZetaLoadModule(category = "client")
public class WoolShutsUpMinecartsModule extends ZetaModule {

	private static boolean staticEnabled;
	
	@Hint(key = "wool_muffling") TagKey<Item> dampeners = ItemTags.DAMPENS_VIBRATIONS;
	
	@LoadEvent
	public final void configChanged(ZConfigChanged event) {
		staticEnabled = enabled;
	}

	public static boolean canPlay(AbstractMinecart cart) {
		return !staticEnabled || !cart.level.getBlockState(cart.blockPosition().below()).is(BlockTags.DAMPENS_VIBRATIONS);
	}
	
}
