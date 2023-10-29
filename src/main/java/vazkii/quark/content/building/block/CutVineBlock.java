package vazkii.quark.content.building.block;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import vazkii.quark.base.block.QuarkVineBlock;
import vazkii.zeta.module.ZetaModule;
import vazkii.zeta.registry.IZetaBlockColorProvider;

public class CutVineBlock extends QuarkVineBlock implements IZetaBlockColorProvider {

	public CutVineBlock(ZetaModule module) {
		super(module, "cut_vine", false);
	}

	@Override
	public boolean canSupportAtFace(@Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull Direction dir) {
		if(dir != Direction.UP) {
			BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(dir);
			BlockState blockstate = level.getBlockState(pos.above());
			return blockstate.is(Blocks.VINE) && blockstate.getValue(booleanproperty);
		}

		return super.canSupportAtFace(level, pos, dir);
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
		return new ItemStack(Items.VINE);
	}

	@Override
	public @Nullable String getBlockColorProviderName() {
		return "vine";
	}

	@Override
	public @Nullable String getItemColorProviderName() {
		return "vine";
	}
}
