package vazkii.zeta.client.event;

import java.util.Map;
import java.util.function.Function;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.level.block.Block;
import vazkii.zeta.event.bus.IZetaLoadEvent;

public interface ZAddBlockColorHandlers extends IZetaLoadEvent {
	void register(BlockColor c, Block... blocks);
	void registerNamed(Function<Block, BlockColor> c, String... names);
	BlockColors getBlockColors();

	Post makePostEvent();
	interface Post extends ZAddBlockColorHandlers {
		Map<String, Function<Block, BlockColor>> getNamedBlockColors();
	}
}
