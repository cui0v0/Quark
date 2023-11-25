package org.violetmoon.quark.content.building.block;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import org.violetmoon.quark.content.building.block.be.VariantChestBlockEntity;
import org.violetmoon.quark.content.building.module.VariantChestsModule;
import org.violetmoon.quark.content.building.module.VariantChestsModule.IChestTextureProvider;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class VariantChestBlock extends ChestBlock implements IZetaBlock, IChestTextureProvider {

	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	protected final String path;

	public VariantChestBlock(String prefix, String type, ZetaModule module, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier, Properties props) {
		super(props, supplier);
		String resloc = (prefix != null ? prefix + "_" : "") + type + "_chest";
		module.zeta.registry.registerBlock(this, resloc, true);
		module.zeta.registry.setCreativeTab(this, "DECORATIONS");

		this.module = module;

		path = (isCompat() ? "compat/" : "") + type + "/";
	}

	public VariantChestBlock(String type, ZetaModule module, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier, Properties props) {
		this(null, type, module, supplier, props);
	}

	protected boolean isCompat() {
		return false;
	}

	@Override
	public int getFlammabilityZeta(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 0;
	}

	@Override
	public boolean isFlammableZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return false;
	}

	@Override
	public VariantChestBlock setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}

	@Nullable
	@Override
	public ZetaModule getModule() {
		return module;
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new VariantChestBlockEntity(pos, state);
	}

	@Override
	public String getChestTexturePath() {
		return "quark_variant_chests/" + path;
	}

	@Override
	public boolean isTrap() {
		return false;
	}

	public static class Compat extends VariantChestBlock {

		public Compat(String type, String mod, ZetaModule module, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier, Properties props) {
			super(type, module, supplier, props);
			setCondition(() -> ModList.get().isLoaded(mod));
		}

		@Override
		protected boolean isCompat() {
			return true;
		}
	}

}
