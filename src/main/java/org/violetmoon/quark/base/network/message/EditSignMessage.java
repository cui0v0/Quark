package org.violetmoon.quark.base.network.message;

import net.minecraft.core.BlockPos;

import java.io.Serial;

import org.violetmoon.quark.content.tweaks.module.SignEditingModule;
import org.violetmoon.zeta.network.IZetaMessage;
import org.violetmoon.zeta.network.IZetaNetworkEventContext;

public class EditSignMessage implements IZetaMessage {

	@Serial
	private static final long serialVersionUID = -329145938273036832L;

	public BlockPos pos;

	public EditSignMessage() {}

	public EditSignMessage(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public boolean receive(IZetaNetworkEventContext context) {
		context.enqueueWork(() -> SignEditingModule.Client.openSignGuiClient(pos));

		return true;
	}

}
