package vazkii.zeta.registry;

import net.minecraft.client.color.item.ItemColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @deprecated zeta - This API relies on OnlyIn stripping to be side-safe, so has been stubbed.
 *             Please use IZetaItemColorProvider, and register "named" color provider creators in
 *             ZAddItemColorHandlers event.
 */
@Deprecated(forRemoval = true)
public interface IZetaItemColorProvider_OLD {

	@OnlyIn(Dist.CLIENT)
	public ItemColor getItemColor();

}