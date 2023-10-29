package vazkii.zeta.client.event;

import java.util.Map;
import java.util.function.Function;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import vazkii.zeta.event.bus.IZetaLoadEvent;

public interface ZAddItemColorHandlers extends IZetaLoadEvent {
	void register(ItemColor c, ItemLike... items);
	void registerNamed(Function<Item, ItemColor> c, String... names);
	ItemColors getItemColors();

	Post makePostEvent();
	interface Post extends ZAddItemColorHandlers {
		Map<String, Function<Item, ItemColor>> getNamedItemColors();
	}
}
