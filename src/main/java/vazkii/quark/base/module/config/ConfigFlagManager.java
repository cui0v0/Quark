package vazkii.quark.base.module.config;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import vazkii.quark.base.handler.GeneralConfig;
import vazkii.quark.base.module.sync.SyncedFlagHandler;
import vazkii.zeta.recipe.FlagIngredient;
import vazkii.zeta.Zeta;
import vazkii.zeta.event.ZRegister;
import vazkii.zeta.event.bus.LoadEvent;
import vazkii.zeta.module.ZetaModule;
import vazkii.zeta.registry.CraftingExtensionsRegistry;

import java.util.*;

public final class ConfigFlagManager {

	public final Zeta zeta;

	private final Set<String> allFlags = new HashSet<>();
	private final Map<String, Boolean> flags = new HashMap<>();

	//TODO augh; needed for BrewingRegistry
	public final FlagIngredient.Serializer flagIngredientSerializer = new FlagIngredient.Serializer(this);

	public ConfigFlagManager(Zeta zeta) {
		this.zeta = zeta;

		zeta.loadBus.subscribe(this);
	}

	@LoadEvent
	public void onRegister(ZRegister event) {
		CraftingExtensionsRegistry ext = event.getCraftingExtensionsRegistry();

		//TODO: make these Quark-independent
		ext.registerConditionSerializer(new FlagCondition.Serializer(this, new ResourceLocation(zeta.modid, "flag")));
		//Especially this one, which requires quark advancement config option :/
		ext.registerConditionSerializer(new FlagCondition.Serializer(this, new ResourceLocation(zeta.modid, "advancement_flag"), () -> GeneralConfig.enableQuarkAdvancements));

		FlagLootCondition.FlagSerializer flagSerializer = new FlagLootCondition.FlagSerializer(this);
		Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(zeta.modid, "flag"), flagSerializer.selfType);

		ext.registerIngredientSerializer(new ResourceLocation(zeta.modid, "flag"), flagIngredientSerializer);

		//TODO: make this Quark-independent
		SyncedFlagHandler.setupFlagManager(this);
	}

	public void clear() {
		flags.clear();
	}

	public void putFlag(ZetaModule module, String flag, boolean value) {
		flags.put(flag, value && module.enabled);
		if (!allFlags.contains(flag)) {
			allFlags.add(flag);
		}
	}

	public void putModuleFlag(ZetaModule module) {
		putFlag(module, module.lowercaseName, true);
	}

	public boolean isValidFlag(String flag) {
		return flags.containsKey(flag);
	}

	public boolean getFlag(String flag) {
		Boolean obj = flags.get(flag);
		return obj != null && obj;
	}

	public Set<String> getAllFlags() {
		return allFlags;
	}

}
