package vazkii.zeta.registry;

import net.minecraft.client.color.block.BlockColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@Deprecated(forRemoval = true) //not side-safe
public interface IZetaBlockColorProvider extends IZetaItemColorProvider {

	@OnlyIn(Dist.CLIENT)
	public BlockColor getBlockColor();

}