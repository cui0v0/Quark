package vazkii.zetaimplforge.event.client;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import vazkii.quark.mixin.client.accessor.AccessorItemColors;
import vazkii.zeta.client.event.ZAddItemColorHandlers;

public record ForgeZAddItemColorHandlers(RegisterColorHandlersEvent.Item e) implements ZAddItemColorHandlers {
	@Override
	public void register(ItemColor c, ItemLike... items) {
		e.register(c, items);
	}

	@Override
	public ItemColors getItemColors() {
		return e.getItemColors();
	}

	@Override
	public @Nullable ItemColor getItemColor(ItemLike item) {
		//forge changes the itemcolors guts to support registry replacement i guess
		Holder.Reference<Item> frog = ForgeRegistries.ITEMS.getDelegateOrThrow(item.asItem());
		return ((AccessorItemColors) getItemColors()).quark$getItemColors().get(frog);
	}
}
