package org.violetmoon.quark.base.network.message;

import net.minecraft.server.level.ServerPlayer;

import java.io.Serial;

import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.handler.ContributorRewardHandler;
import org.violetmoon.quark.base.network.QuarkNetwork;
import org.violetmoon.zeta.network.IZetaMessage;
import org.violetmoon.zeta.network.IZetaNetworkEventContext;

public class RequestEmoteMessage implements IZetaMessage {

	@Serial
	private static final long serialVersionUID = -8569122937119059414L;

	public String emote;

	public RequestEmoteMessage() {}

	public RequestEmoteMessage(String emote) {
		this.emote = emote;
	}

	@Override
	public boolean receive(IZetaNetworkEventContext context) {
		ServerPlayer player = context.getSender();
		if(player != null && player.server != null)
			context.enqueueWork(() -> Quark.ZETA.network.sendToAllPlayers(
					new DoEmoteMessage(emote, player.getUUID(), ContributorRewardHandler.getTier(player)),
					player.server));
		return true;
	}

}
