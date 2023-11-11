package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;

import javax.annotation.Nullable;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry;

public class ZetaLeavesBlock extends LeavesBlock implements IZetaBlock {
	
	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;
	
	public ZetaLeavesBlock(String name, ZetaModule module, MaterialColor color) {
		super(Block.Properties.of(Material.LEAVES, color)
				.strength(0.2F)
				.randomTicks()
				.sound(SoundType.GRASS)
				.noOcclusion()
				.isValidSpawn((s, r, p, t) -> false)
				.isSuffocating((s, r, p) -> false)
				.isViewBlocking((s, r, p) -> false));

		this.module = module;

		module.zeta.registry.registerBlock(this, name + "_leaves", true);
		module.zeta.registry.setCreativeTab(this, CreativeModeTab.TAB_DECORATIONS);

		module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT_MIPPED);
	}
	
	@Nullable
	@Override
	public ZetaModule getModule() {
		return module;
	}

	@Override
	public ZetaLeavesBlock setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}

}
