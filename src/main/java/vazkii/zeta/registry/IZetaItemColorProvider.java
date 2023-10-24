package vazkii.zeta.registry;

import net.minecraft.client.color.item.ItemColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Deprecated //not side-safe
public interface IZetaItemColorProvider {

	@OnlyIn(Dist.CLIENT)
	public ItemColor getItemColor();

}