package org.violetmoon.quark.base.network;

import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.chat.MessageSignature;

import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.network.message.*;
import org.violetmoon.quark.base.network.message.experimental.PlaceVariantUpdateMessage;
import org.violetmoon.quark.base.network.message.oddities.HandleBackpackMessage;
import org.violetmoon.quark.base.network.message.oddities.MatrixEnchanterOperationMessage;
import org.violetmoon.quark.base.network.message.oddities.ScrollCrateMessage;
import org.violetmoon.quark.base.network.message.structural.C2SLoginFlag;
import org.violetmoon.quark.base.network.message.structural.C2SUpdateFlag;
import org.violetmoon.quark.base.network.message.structural.S2CLoginFlag;
import org.violetmoon.quark.base.network.message.structural.S2CUpdateFlag;
import org.violetmoon.quark.content.tweaks.module.LockRotationModule;
import org.violetmoon.zeta.network.ZetaNetworkDirection;
import org.violetmoon.zeta.network.ZetaNetworkHandler;

import java.time.Instant;
import java.util.BitSet;

public final class QuarkNetwork {

	public static final int PROTOCOL_VERSION = 3;

	public static void init() {
		ZetaNetworkHandler network = Quark.ZETA.createNetworkHandler(PROTOCOL_VERSION);
		Quark.ZETA.network = network;

		network.getSerializer().mapHandlers(Instant.class, (buf, field) -> buf.readInstant(), (buf, field, instant) -> buf.writeInstant(instant));
		network.getSerializer().mapHandlers(MessageSignature.class, (buf, field) -> new MessageSignature(buf.accessByteBufWithCorrectSize()), (buf, field, signature) -> MessageSignature.write(buf, signature));
		network.getSerializer().mapHandlers(LastSeenMessages.Update.class, (buf, field) -> new LastSeenMessages.Update(buf), (buf, field, update) -> update.write(buf));
		network.getSerializer().mapHandlers(BitSet.class, (buf, field) -> BitSet.valueOf(buf.readLongArray()), (buf, field, bitSet) -> buf.writeLongArray(bitSet.toLongArray()));

		// Base Quark
		network.register(SortInventoryMessage.class, ZetaNetworkDirection.PLAY_TO_SERVER);
		network.register(InventoryTransferMessage.class, ZetaNetworkDirection.PLAY_TO_SERVER);
		network.register(DoubleDoorMessage.class, ZetaNetworkDirection.PLAY_TO_SERVER);
		network.register(HarvestMessage.class, ZetaNetworkDirection.PLAY_TO_SERVER);
		network.register(RequestEmoteMessage.class, ZetaNetworkDirection.PLAY_TO_SERVER);
		network.register(ChangeHotbarMessage.class, ZetaNetworkDirection.PLAY_TO_SERVER);
		network.register(SetLockProfileMessage.class, ZetaNetworkDirection.PLAY_TO_SERVER);
		network.register(ShareItemC2SMessage.class, ZetaNetworkDirection.PLAY_TO_SERVER);
		network.register(ScrollOnBundleMessage.class, ZetaNetworkDirection.PLAY_TO_SERVER);
		network.getSerializer().mapHandlers(LockRotationModule.LockProfile.class, LockRotationModule.LockProfile::readProfile, LockRotationModule.LockProfile::writeProfile);

		// Oddities
		network.register(HandleBackpackMessage.class, ZetaNetworkDirection.PLAY_TO_SERVER);
		network.register(MatrixEnchanterOperationMessage.class, ZetaNetworkDirection.PLAY_TO_SERVER);
		network.register(ScrollCrateMessage.class, ZetaNetworkDirection.PLAY_TO_SERVER);

		// Experimental
		network.register(PlaceVariantUpdateMessage.class, ZetaNetworkDirection.PLAY_TO_SERVER);

		// Clientbound
		network.register(DoEmoteMessage.class, ZetaNetworkDirection.PLAY_TO_CLIENT);
		network.register(UpdateTridentMessage.class, ZetaNetworkDirection.PLAY_TO_CLIENT);
		network.register(ShareItemS2CMessage.class, ZetaNetworkDirection.PLAY_TO_CLIENT);

		// Flag Syncing
		network.register(S2CUpdateFlag.class, ZetaNetworkDirection.PLAY_TO_CLIENT);
		network.register(C2SUpdateFlag.class, ZetaNetworkDirection.PLAY_TO_SERVER);

		// Login
		network.registerLogin(S2CLoginFlag.class, ZetaNetworkDirection.LOGIN_TO_CLIENT, 98, true, S2CLoginFlag::generateRegistryPackets);
		network.registerLogin(C2SLoginFlag.class, ZetaNetworkDirection.LOGIN_TO_SERVER, 99, false, null);
	}

}
