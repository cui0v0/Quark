package org.violetmoon.quark.base.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.*;

import org.jetbrains.annotations.NotNull;

import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.tools.module.AmbientDiscsModule;
import org.violetmoon.zeta.item.IZetaItem;
import org.violetmoon.zeta.module.ZetaModule;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class QuarkMusicDiscItem extends RecordItem implements IZetaItem {

	private final ZetaModule module;
	public final boolean isAmbient;
	public final Supplier<SoundEvent> soundSupplier;

	private BooleanSupplier enabledSupplier = () -> true;

	public QuarkMusicDiscItem(int comparatorValue, Supplier<SoundEvent> sound, String name, ZetaModule module, int lengthInTicks) {
		//TODO: This constructor (with the supplier) is a Forge extension
		super(comparatorValue, sound, (new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC).rarity(Rarity.RARE), lengthInTicks);

		Quark.ZETA.registry.registerItem(this, "music_disc_" + name);
		this.module = module;
		this.isAmbient = lengthInTicks == Integer.MAX_VALUE;
		this.soundSupplier = sound;
	}

	@Override
	public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> items) {
		if(isEnabled() || group == CreativeModeTab.TAB_SEARCH)
			super.fillItemCategory(group, items);
	}

	@Override
	public QuarkMusicDiscItem setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public ZetaModule getModule() {
		return module;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}

}
