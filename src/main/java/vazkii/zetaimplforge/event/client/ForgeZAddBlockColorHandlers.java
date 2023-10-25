package vazkii.zetaimplforge.event.client;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import vazkii.zeta.event.bus.FiredAs;
import vazkii.zeta.client.event.ZAddBlockColorHandlers;

@FiredAs(ZAddBlockColorHandlers.class)
public record ForgeZAddBlockColorHandlers(RegisterColorHandlersEvent.Block e) implements ZAddBlockColorHandlers {
	@Override
	public void register(BlockColor blockColor, Block... blocks) {
		e.register(blockColor, blocks);
	}

	@Override
	public BlockColors getBlockColors() {
		return e.getBlockColors();
	}
}
