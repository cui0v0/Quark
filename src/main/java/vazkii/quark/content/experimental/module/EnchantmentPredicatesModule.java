package vazkii.quark.content.experimental.module;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import vazkii.quark.base.Quark;
import vazkii.zeta.module.ModuleSide;
import vazkii.zeta.module.ZetaLoadModule;
import vazkii.zeta.module.ZetaModule;
import vazkii.quark.base.module.config.Config;
import vazkii.zeta.event.bus.LoadEvent;
import vazkii.zeta.client.event.ZClientSetup;
import vazkii.zeta.util.RegistryUtil;

import java.util.List;

@ZetaLoadModule(category = "experimental", enabledByDefault = false, side = ModuleSide.CLIENT_ONLY,
description = "This feature generates Resource Pack Item Model predicates on the items defined in 'Items to Change'\n"
		+ "for the Enchantments defined in 'Enchantments to Register'.\n\n"
		+ "Example: if 'minecraft:silk_touch' is added to 'Enchantments to Register', and 'minecraft:netherite_pickaxe'\n"
		+ "is added to 'Items to Change', then a predicate named 'quark_has_enchant_minecraft_silk_touch' will be available\n"
		+ "to the netherite_pickaxe.json item model, whose value will be the enchantment level.")
public class EnchantmentPredicatesModule extends ZetaModule {

	@Config
	public static List<String> itemsToChange = Lists.newArrayList();

	@Config
	public static List<String> enchantmentsToRegister = Lists.newArrayList();

	@LoadEvent
	public void clientSetup(ZClientSetup e) {
		if(enabled) {
			e.enqueueWork(() -> {
				List<Item> items = RegistryUtil.massRegistryGet(itemsToChange, Registry.ITEM);
				List<Enchantment> enchants = RegistryUtil.massRegistryGet(enchantmentsToRegister, Registry.ENCHANTMENT);

				for(Enchantment enchant : enchants) {
					ResourceLocation enchantRes = Registry.ENCHANTMENT.getKey(enchant);
					ResourceLocation name = new ResourceLocation(Quark.MOD_ID + "_has_enchant_" + enchantRes.getNamespace() + "_" + enchantRes.getPath());
					ItemPropertyFunction fun = (stack, level, entity, i) -> EnchantmentHelper.getTagEnchantmentLevel(enchant, stack);

					for(Item item : items)
						ItemProperties.register(item, name, fun);
				}
			});
		}
	}


}
