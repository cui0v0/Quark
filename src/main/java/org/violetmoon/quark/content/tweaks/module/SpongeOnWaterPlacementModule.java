package org.violetmoon.quark.content.tweaks.module;

import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.play.entity.player.ZRightClickItem;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;

@ZetaLoadModule(category = "tweaks")
public class SpongeOnWaterPlacementModule extends ZetaModule {

	@Hint Item sponge = Items.SPONGE;
	
	@PlayEvent
	public void onUse(ZRightClickItem event) {
		ItemStack stack = event.getItemStack();
		if(stack.is(Items.SPONGE)) {
			Player player = event.getEntity();
			Level level = event.getLevel();
			InteractionHand hand = event.getHand();
			
			BlockHitResult blockhitresult = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
			BlockPos pos = blockhitresult.getBlockPos();
			
			if(level.getBlockState(pos).is(Blocks.WATER)) {
				BlockHitResult blockhitresult1 = blockhitresult.withPosition(pos);
				InteractionResult result = Items.SPONGE.useOn(new UseOnContext(player, hand, blockhitresult1));
				if(result != InteractionResult.PASS) {
					event.setCanceled(true);
					event.setCancellationResult(result);
				}
			}
		}
	}

}
