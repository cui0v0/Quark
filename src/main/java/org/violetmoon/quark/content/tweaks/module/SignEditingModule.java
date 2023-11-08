package org.violetmoon.quark.content.tweaks.module;

import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.module.config.Config;
import org.violetmoon.quark.base.network.QuarkNetwork;
import org.violetmoon.quark.base.network.message.EditSignMessage;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.bus.ZResult;
import org.violetmoon.zeta.event.play.entity.player.ZRightClickBlock;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;

@ZetaLoadModule(category = "tweaks")
public class SignEditingModule extends ZetaModule {

	@Hint(key = "sign_editing") TagKey<Item> signs = ItemTags.SIGNS;
	
	@Config public static boolean requiresEmptyHand = false;

	@PlayEvent
	public void onInteract(ZRightClickBlock event) {
		if(event.getUseBlock() == ZResult.DENY)
			return;

		BlockEntity tile = event.getLevel().getBlockEntity(event.getPos());
		Player player = event.getPlayer();
		ItemStack stack = player.getMainHandItem();

		if(player instanceof ServerPlayer serverPlayer
				&& tile instanceof SignBlockEntity sign
				&& !doesSignHaveCommand(sign)
				&& (!requiresEmptyHand || stack.isEmpty())
				&& !(stack.getItem() instanceof DyeItem)
				&& !(stack.getItem() == Items.GLOW_INK_SAC)
				&& !Registry.BLOCK.getKey(tile.getBlockState().getBlock()).getNamespace().equals("signbutton")
				&& player.mayUseItemAt(event.getPos(), event.getFace(), event.getItemStack())
				&& !event.getEntity().isDiscrete()) {

			sign.setAllowedPlayerEditor(player.getUUID());
			sign.isEditable = true;

			QuarkNetwork.sendToPlayer(new EditSignMessage(event.getPos()), serverPlayer);

			event.setCanceled(true);
			event.setCancellationResult(InteractionResult.SUCCESS);
		}
	}

	private boolean doesSignHaveCommand(SignBlockEntity sign) {
		for(Component itextcomponent : sign.messages) {
			Style style = itextcomponent == null ? null : itextcomponent.getStyle();
			if (style != null && style.getClickEvent() != null) {
				ClickEvent clickevent = style.getClickEvent();
				if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
					return true;
				}
			}
		}

		return false;
	}

	//Not a client replacement module
	public static class Client {

		public static void openSignGuiClient(BlockPos pos) {
			if(!Quark.ZETA.modules.isEnabled(SignEditingModule.class))
				return;

			Minecraft mc = Minecraft.getInstance();
			BlockEntity tile = mc.level.getBlockEntity(pos);

			if(tile instanceof SignBlockEntity sign)
				mc.player.openTextEdit(sign);
		}
	}

}
