package vazkii.quark.base.capability;

import java.util.concurrent.Callable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.addons.oddities.capability.MagnetTracker;
import vazkii.quark.api.ICustomSorting;
import vazkii.quark.api.IMagnetTracker;
import vazkii.quark.api.IPistonCallback;
import vazkii.quark.api.IRuneColorProvider;
import vazkii.quark.api.ITransferManager;
import vazkii.quark.api.QuarkCapabilities;
import vazkii.quark.base.Quark;
import vazkii.quark.base.capability.dummy.DummyMagnetTracker;
import vazkii.quark.base.capability.dummy.DummyPistonCallback;
import vazkii.quark.base.capability.dummy.DummyRuneColor;
import vazkii.quark.base.capability.dummy.DummySorting;
import vazkii.zeta.capability.ZetaCapabilityManager;

public class CapabilityHandler {

	private static final ZetaCapabilityManager mgr = Quark.ZETA.capabilityManager;

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		registerLambda(event, ITransferManager.class, (player) -> false);

		register(event, ICustomSorting.class, DummySorting::new);
		register(event, IPistonCallback.class, DummyPistonCallback::new);
		register(event, IMagnetTracker.class, DummyMagnetTracker::new);
		register(event, IRuneColorProvider.class, DummyRuneColor::new);
	}
	
	private static <T> void registerLambda(RegisterCapabilitiesEvent event, Class<T> clazz, T provider) {
		register(event, clazz, () -> provider);
	}

	private static <T> void register(RegisterCapabilitiesEvent event, Class<T> clazz, Callable<T> provider) {
		event.register(clazz);
	}

	private static final ResourceLocation DROPOFF_MANAGER = new ResourceLocation(Quark.MOD_ID, "dropoff");
	private static final ResourceLocation SORTING_HANDLER = new ResourceLocation(Quark.MOD_ID, "sort");
	private static final ResourceLocation MAGNET_TRACKER = new ResourceLocation(Quark.MOD_ID, "magnet_tracker");
	private static final ResourceLocation RUNE_COLOR_HANDLER = new ResourceLocation(Quark.MOD_ID, "rune_color");

	@SubscribeEvent
	public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
		Item item = event.getObject().getItem();

		if(item instanceof ICustomSorting impl)
			mgr.attachCapability(event, SORTING_HANDLER, QuarkCapabilities.SORTING, impl);

		if(item instanceof IRuneColorProvider impl)
			mgr.attachCapability(event, RUNE_COLOR_HANDLER, QuarkCapabilities.RUNE_COLOR, impl);
	}

	@SubscribeEvent
	public static void attachTileCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
		if(event.getObject() instanceof ITransferManager impl)
			mgr.attachCapability(event, DROPOFF_MANAGER, QuarkCapabilities.TRANSFER, impl);
	}
	
	@SubscribeEvent 
	public static void attachWorldCapabilities(AttachCapabilitiesEvent<Level> event) {
		Level world = event.getObject();
		mgr.attachCapability(event, MAGNET_TRACKER, QuarkCapabilities.MAGNET_TRACKER_CAPABILITY, new MagnetTracker(world));
	}
}
