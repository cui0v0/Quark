package vazkii.quark.base.proxy;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.quark.base.Quark;
import vazkii.quark.base.capability.CapabilityHandler;
import vazkii.quark.base.handler.*;
import vazkii.quark.base.handler.advancement.QuarkAdvancementHandler;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.base.module.sync.SyncedFlagHandler;
import vazkii.quark.base.network.QuarkNetwork;
import vazkii.quark.base.recipe.*;
import vazkii.quark.base.world.EntitySpawnHandler;
import vazkii.quark.base.world.WorldGenHandler;
import vazkii.quark.base.module.LegacyQuarkModuleFinder;
import vazkii.zeta.event.ZCommonSetup;
import vazkii.zeta.event.ZConfigChanged;
import vazkii.zeta.event.ZRegister;
import vazkii.zeta.event.bus.LoadEvent;
import vazkii.zeta.module.ZetaCategory;
import vazkii.zetaimplforge.module.ModFileScanDataModuleFinder;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class CommonProxy {

	private int lastConfigChange = -11;
	public static boolean jingleTheBells = false;
	private boolean configGuiSaving = false;

	public void start() {
		ForgeRegistries.RECIPE_SERIALIZERS.register(Quark.MOD_ID + ":exclusion", ExclusionRecipe.SERIALIZER);

		Quark.ZETA.loadBus
			.subscribe(BrewingHandler.class)
			.subscribe(ContributorRewardHandler.class)
			.subscribe(CreativeTabHandler.class)
			.subscribe(ModuleLoader.INSTANCE)
			.subscribe(QuarkNetwork.class)
			.subscribe(QuarkSounds.class)
			.subscribe(ToolInteractionHandler.class)
			.subscribe(WoodSetHandler.class)
			.subscribe(WorldGenHandler.class)
			.subscribe(FuelHandler.class)
			.subscribe(UndergroundBiomeHandler.class)
			.subscribe(this);

		//Formerly @EventBusSubscribers - gathered here to make them more visible
		FMLJavaModLoadingContext.get().getModEventBus().register(EntityAttributeHandler.class);
		MinecraftForge.EVENT_BUS.register(CapabilityHandler.class);
		MinecraftForge.EVENT_BUS.register(ContributorRewardHandler.class);
		MinecraftForge.EVENT_BUS.register(FuelHandler.class);
		MinecraftForge.EVENT_BUS.register(QuarkAdvancementHandler.class);
		MinecraftForge.EVENT_BUS.register(RecipeCrawlHandler.class);
		MinecraftForge.EVENT_BUS.register(ToolInteractionHandler.class);

		Quark.ZETA.loadModules(
			List.of(
				new ZetaCategory("automation", Items.REDSTONE),
				new ZetaCategory("building", Items.BRICKS),
				new ZetaCategory("management", Items.CHEST),
				new ZetaCategory("tools", Items.IRON_PICKAXE),
				new ZetaCategory("tweaks", Items.NAUTILUS_SHELL),
				new ZetaCategory("world", Items.GRASS_BLOCK),
				new ZetaCategory("mobs", Items.PIG_SPAWN_EGG),
				new ZetaCategory("client", Items.ENDER_EYE),
				new ZetaCategory("experimental", Items.TNT),
				new ZetaCategory("oddities", Items.CHORUS_FRUIT, Quark.ODDITIES_ID)
			),
			new ModFileScanDataModuleFinder(Quark.MOD_ID).and(new LegacyQuarkModuleFinder()),
			GeneralConfig.INSTANCE
		);

		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::configChanged);
		bus.addListener(this::registerCapabilities);
		WorldGenHandler.registerBiomeModifier(bus);

		LocalDateTime now = LocalDateTime.now();
		if (now.getMonth() == Month.DECEMBER && now.getDayOfMonth() >= 16 || now.getMonth() == Month.JANUARY && now.getDayOfMonth() <= 2)
			jingleTheBells = true;
	}

	@LoadEvent
	public void setup(ZCommonSetup event) {
		handleQuarkConfigChange();
	}

	@LoadEvent
	public void register(ZRegister heck) {
		Quark.ZETA.weirdConfigSingleton.getConfigFlagManager().registerConfigBoundElements();
	}

	//forge event
	public void configChanged(ModConfigEvent event) {
		if(event.getConfig().getModId().equals(Quark.MOD_ID)
			&& Quark.ZETA.ticker_SHOULD_NOT_BE_HERE.ticksInGame - lastConfigChange > 10
			&& !configGuiSaving) {
			lastConfigChange = Quark.ZETA.ticker_SHOULD_NOT_BE_HERE.ticksInGame;
			handleQuarkConfigChange();
		}
	}

	public void setConfigGuiSaving(boolean saving) {
		configGuiSaving = saving;
		lastConfigChange = Quark.ZETA.ticker_SHOULD_NOT_BE_HERE.ticksInGame;
	}

	//TODO: mess, find a better spot for this
	public void handleQuarkConfigChange() {
		//ModuleLoader.INSTANCE.configChanged();
		Quark.ZETA.weirdConfigSingleton.runDatabindings(Quark.ZETA.configInternals);
		//Quark.ZETA.weirdConfigSingleton.getConfigFlagManager().clear();
		Quark.ZETA.loadBus.fire(new ZConfigChanged());
		if(ModuleLoader.INSTANCE.onConfigReloadJEI != null)
			ModuleLoader.INSTANCE.onConfigReloadJEI.run();
		EntitySpawnHandler.refresh();
		SyncedFlagHandler.sendFlagInfoToPlayers();
	}

	//forge event
	public void registerCapabilities(RegisterCapabilitiesEvent event) {
		CapabilityHandler.registerCapabilities(event);
	}

	/**
	 * Use an item WITHOUT sending the use to the server. This will cause ghost interactions if used incorrectly!
	 */
	public InteractionResult clientUseItem(Player player, Level level, InteractionHand hand, BlockHitResult hit) {
		return InteractionResult.PASS;
	}

	public boolean isClientPlayerHoldingShift() {
		return false;
	}
}
