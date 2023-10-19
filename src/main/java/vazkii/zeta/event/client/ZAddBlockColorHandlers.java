package vazkii.zeta.event.client;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.level.block.Block;
import vazkii.zeta.event.bus.IZetaLoadEvent;

public interface ZAddBlockColorHandlers extends IZetaLoadEvent {
	void register(BlockColor c, Block... blocks);
	BlockColors getBlockColors();
}
