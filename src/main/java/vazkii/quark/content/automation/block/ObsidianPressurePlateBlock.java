package vazkii.quark.content.automation.block;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import vazkii.quark.base.block.QuarkPressurePlateBlock;
import vazkii.quark.base.module.QuarkModule;

/**
 * @author WireSegal
 * Created at 9:47 PM on 10/8/19.
 */
public class ObsidianPressurePlateBlock extends QuarkPressurePlateBlock {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public ObsidianPressurePlateBlock(String regname, QuarkModule module, CreativeModeTab creativeTab, Properties properties, BlockSetType setType) {
		super(null /*Sensitivity is unused*/, regname, module, creativeTab, properties, setType);
		this.registerDefaultState(defaultBlockState().setValue(POWERED, false));
	}

	@Override
	protected int getSignalStrength(@Nonnull Level worldIn, @Nonnull BlockPos pos) {
		AABB bounds = TOUCH_AABB.move(pos);
		List<? extends Entity> entities = worldIn.getEntitiesOfClass(Player.class, bounds);

		if (!entities.isEmpty()) {
			for(Entity entity : entities) {
				if (!entity.isIgnoringBlockTriggers()) {
					return 15;
				}
			}
		}

		return 0;
	}
}
