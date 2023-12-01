package org.violetmoon.quark.content.world.feature;

import java.util.Comparator;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.violetmoon.quark.content.world.module.AncientWoodModule;

//for Ancient Trees, TODO register me
public class AncientTreeTopperDecorator extends TreeDecorator {

	public static final Codec<AncientTreeTopperDecorator> CODEC = Codec.unit(AncientTreeTopperDecorator::new);
	public static final TreeDecoratorType<AncientTreeTopperDecorator> TYPE = new TreeDecoratorType<>(CODEC);

	//TODO register me
	@Override
	protected TreeDecoratorType<?> type() {
		//return TYPE;
		return null;
	}

	@Override
	public void place(Context ctx) {
		Optional<BlockPos> highestLog = ctx.logs().stream().max(Comparator.comparingInt(Vec3i::getY));
		if(highestLog.isPresent()) {
			BlockPos top = highestLog.get();

			ImmutableSet<BlockPos> leafPos = ImmutableSet.of(
				top.above(), top.east(), top.west(), top.north(), top.south()
			);

			BlockState state = AncientWoodModule.ancient_leaves.defaultBlockState();
			leafPos.forEach(p -> {
				if(ctx.isAir(p))
					ctx.setBlock(p, state);
			});
		}
	}

}
