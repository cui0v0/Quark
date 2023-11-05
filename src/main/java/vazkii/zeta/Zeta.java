package vazkii.zeta;

import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import vazkii.zeta.registry.BrewingRegistry;
import vazkii.zeta.client.ClientTicker;
import vazkii.zeta.config.IZetaConfigInternals;
import vazkii.zeta.config.SectionDefinition;
import vazkii.zeta.config.ConfigManager;
import vazkii.zeta.event.bus.IZetaLoadEvent;
import vazkii.zeta.event.bus.IZetaPlayEvent;
import vazkii.zeta.event.bus.LoadEvent;
import vazkii.zeta.event.bus.ZetaEventBus;
import vazkii.zeta.event.bus.PlayEvent;
import vazkii.zeta.module.ModuleFinder;
import vazkii.zeta.module.ZetaCategory;
import vazkii.zeta.module.ZetaModuleManager;
import vazkii.zeta.network.ZetaNetworkHandler;
import vazkii.zeta.registry.CraftingExtensionsRegistry;
import vazkii.zeta.registry.DyeablesRegistry;
import vazkii.zeta.registry.RenderLayerRegistry;
import vazkii.zeta.registry.ZetaRegistry;
import vazkii.zeta.util.ZetaSide;

/**
 * do not touch forge OR quark from this package, it will later be split off
 */
public abstract class Zeta {
	public static final Logger GLOBAL_LOG = LogManager.getLogger("zeta");

	public Zeta(String modid, Logger log, ZetaSide side) {
		this.log = log;

		this.modid = modid;
		this.side = side;
		this.loadBus = new ZetaEventBus<>(LoadEvent.class, IZetaLoadEvent.class, log);
		this.playBus = new ZetaEventBus<>(PlayEvent.class, IZetaPlayEvent.class, null);

		this.modules = createModuleManager();
		this.registry = createRegistry();
		this.renderLayerRegistry = createRenderLayerRegistry();
		this.dyeables = createDyeablesRegistry();
		this.craftingExtensions = createCraftingExtensionsRegistry();
		this.brewingRegistry = createBrewingRegistry();
	}

	public final Logger log;

	public final String modid;
	public final ZetaSide side;
	public final ZetaEventBus<IZetaLoadEvent> loadBus;
	public final ZetaEventBus<IZetaPlayEvent> playBus;
	public final ZetaModuleManager modules;

	public final ZetaRegistry registry;
	public final RenderLayerRegistry renderLayerRegistry;
	public final DyeablesRegistry dyeables;
	public final CraftingExtensionsRegistry craftingExtensions;
	public final BrewingRegistry brewingRegistry;

	public ConfigManager configManager; //This could do with being split up into various pieces?
	public IZetaConfigInternals configInternals;

	public void loadModules(Iterable<ZetaCategory> categories, ModuleFinder finder, Object rootPojo) {
		modules.initCategories(categories);
		modules.load(finder);

		//The reason why there's a circular dependency between configManager and configInternals:
		// - ConfigManager determines the shape and layout of the config file
		// - The platform-specific configInternals loads the actual values, from the platform-specfic config file
		// - Only then can ConfigManager do the initial config load

		this.configManager = new ConfigManager(this, rootPojo);
		this.configInternals = makeConfigInternals(configManager.getRootConfig());
		this.configManager.onReload();
	}

	// modloader services
	public abstract boolean isModLoaded(String modid);
	public abstract @Nullable String getModDisplayName(String modid);

	// config
	public abstract IZetaConfigInternals makeConfigInternals(SectionDefinition rootSection);

	// general xplat stuff
	public ZetaModuleManager createModuleManager() {
		return new ZetaModuleManager(this);
	}
	public abstract ZetaRegistry createRegistry();
	public RenderLayerRegistry createRenderLayerRegistry() {
		return new RenderLayerRegistry();
	}
	public abstract CraftingExtensionsRegistry createCraftingExtensionsRegistry();
	public DyeablesRegistry createDyeablesRegistry() {
		return new DyeablesRegistry(this);
	}
	public abstract BrewingRegistry createBrewingRegistry();
	public abstract ZetaNetworkHandler createNetworkHandler(String modid, int protocolVersion);

	// misc "ah fuck i need to interact with the modloader" stuff
	public abstract boolean fireRightClickBlock(Player player, InteractionHand hand, BlockPos pos, BlockHitResult bhr);
	public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
		//forge has a funky little extension for this
		return stack.getItem() instanceof ElytraItem && ElytraItem.isFlyEnabled(stack);
	}
	public boolean isEnderMask(ItemStack stack, Player player, EnderMan enderboy) {
		return stack.getItem() == Items.CARVED_PUMPKIN;
	}
	public boolean canShear(ItemStack stack) {
		return stack.getItem() instanceof ShearsItem;
	}
	public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
		return EnchantmentHelper.getTagEnchantmentLevel(enchantment, stack);
	}
	public Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
		return EnchantmentHelper.deserializeEnchantments(stack.getEnchantmentTags());
	}
	@SuppressWarnings("deprecation") //forge ext
	public int getEnchantmentValue(ItemStack stack) {
		return stack.getItem().getEnchantmentValue();
	}

	public abstract int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType);

	// Let's Jump
	public abstract void start();
}
