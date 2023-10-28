package vazkii.zetaimplforge.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.zeta.event.ZCommonSetup;
import vazkii.zeta.registry.BrewingRegistry;
import vazkii.quark.mixin.accessor.AccessorPotionBrewing;
import vazkii.zeta.event.bus.LoadEvent;
import vazkii.zetaimplforge.ForgeZeta;

public class ForgeBrewingRegistry extends BrewingRegistry {
	public ForgeBrewingRegistry(ForgeZeta zeta) {
		super(zeta);

		zeta.loadBus.subscribe(this);
	}

	private record DelayedPotion(Potion input, Supplier<Ingredient> reagentSupplier, Potion output) {
		void register() {
			AccessorPotionBrewing.quark$getPotionMixes().add(new PotionBrewing.Mix<>(ForgeRegistries.POTIONS, input, reagentSupplier.get(), output));
		}
	}
	private List<DelayedPotion> delayedPotions = new ArrayList<>();
	private boolean okToRegisterImmediately = false;

	@Override
	public void addBrewingRecipe(Potion input, Supplier<Ingredient> reagentSupplier, Potion output) {
		DelayedPotion d = new DelayedPotion(input, reagentSupplier, output);

		if(okToRegisterImmediately)
			d.register();
		else
			delayedPotions.add(d);
	}

	@LoadEvent
	public void commonSetup(ZCommonSetup event) {
		event.enqueueWork(() -> {
			okToRegisterImmediately = true;
			delayedPotions.forEach(DelayedPotion::register);
			delayedPotions = null;
		});
	}
}
