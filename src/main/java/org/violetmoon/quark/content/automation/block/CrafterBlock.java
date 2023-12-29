/*
 * The Cool MIT License (CMIT)
 *
 * Copyright (c) 2023 Emi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, as long as the person is cool, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * The person is cool.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.violetmoon.quark.content.automation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.automation.block.be.CrafterBlockEntity;
import org.violetmoon.quark.content.automation.module.CrafterModule;
import org.violetmoon.quark.content.automation.util.RecipeCache;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;

import java.util.Optional;

public class CrafterBlock extends ZetaBlock implements EntityBlock {
	public static final BooleanProperty CRAFTING = BooleanProperty.create("crafting");
	public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
	private static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;
	private static final RecipeCache RECIPE_CACHE = new RecipeCache(10);

	public CrafterBlock(String regname, @Nullable ZetaModule module, Properties properties) {
		super(regname, module, properties);
		this.registerDefaultState(
				this.stateDefinition
						.any()
						.setValue(ORIENTATION, FrontAndTop.NORTH_UP)
						.setValue(TRIGGERED, Boolean.FALSE)
						.setValue(CRAFTING, Boolean.FALSE)
		);

		if(module == null) //auto registration below this line
			return;

		setCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS);
	}

	@Override
	public boolean hasAnalogOutputSignal(@NotNull BlockState blockState) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos) {
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		return blockEntity instanceof CrafterBlockEntity crafterBlockEntity ? crafterBlockEntity.getRedstoneSignal() : 0;
	}

	@Override
	public void neighborChanged(BlockState blockState, Level level, @NotNull BlockPos blockPos,
								@NotNull Block block, @NotNull BlockPos blockPos2, boolean bl) {
		boolean neighborSignal = level.hasNeighborSignal(blockPos);
		boolean triggered = blockState.getValue(TRIGGERED);
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		if (neighborSignal && !triggered) {
			level.scheduleTick(blockPos, this, 4);
			level.setBlock(blockPos, blockState.setValue(TRIGGERED, Boolean.TRUE), 2);
			this.setBlockEntityTriggered(blockEntity, true);
		} else if (!neighborSignal && triggered) {
			level.setBlock(blockPos, blockState.setValue(TRIGGERED, Boolean.FALSE).setValue(CRAFTING, Boolean.FALSE), 2);
			this.setBlockEntityTriggered(blockEntity, false);
		}
	}

	@Override
	public void tick(@NotNull BlockState blockState, @NotNull ServerLevel serverLevel,
					 @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
		this.dispenseFrom(blockState, serverLevel, blockPos);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState blockState,
																  @NotNull BlockEntityType<T> blockEntityType) {
		return level.isClientSide ? null : createTickerHelper(blockEntityType, CrafterModule.blockEntityType, CrafterBlockEntity::serverTick);
	}

	private void setBlockEntityTriggered(@Nullable BlockEntity blockEntity, boolean bl) {
		if (blockEntity instanceof CrafterBlockEntity crafterBlockEntity) {
			crafterBlockEntity.setTriggered(bl);
		}
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		CrafterBlockEntity crafterBlockEntity = new CrafterBlockEntity(blockPos, blockState);
		crafterBlockEntity.setTriggered(blockState.hasProperty(TRIGGERED) && blockState.getValue(TRIGGERED));
		return crafterBlockEntity;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
		Direction direction = blockPlaceContext.getNearestLookingDirection().getOpposite();

		Direction direction2 = switch(direction) {
			case DOWN -> blockPlaceContext.getHorizontalDirection().getOpposite();
			case UP -> blockPlaceContext.getHorizontalDirection();
			case NORTH, SOUTH, WEST, EAST -> Direction.UP;
		};
		return this.defaultBlockState()
				.setValue(ORIENTATION, FrontAndTop.fromFrontAndTop(direction, direction2))
				.setValue(TRIGGERED, blockPlaceContext.getLevel().hasNeighborSignal(blockPlaceContext.getClickedPos()));
	}

	@Override
	public void setPlacedBy(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState,
							LivingEntity livingEntity, ItemStack itemStack) {
		if (itemStack.hasCustomHoverName()) {
			BlockEntity be = level.getBlockEntity(blockPos);
			if (be instanceof CrafterBlockEntity crafterBlockEntity) {
				crafterBlockEntity.setCustomName(itemStack.getHoverName());
			}
		}

		if (blockState.getValue(TRIGGERED)) {
			level.scheduleTick(blockPos, this, 4);
		}
	}

	@Override
	public void onRemove(@NotNull BlockState blockState, @NotNull Level level,
						 @NotNull BlockPos blockPos, @NotNull BlockState blockState2, boolean bl) {
		//fixme
		//Containers.dropContents(level, blockPos, CrafterMenu.getContainer());
		super.onRemove(blockState, level, blockPos, blockState2, bl);
	}

	protected void dispenseFrom(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos) {
		BlockEntity optional = serverLevel.getBlockEntity(blockPos);
		if (optional instanceof CrafterBlockEntity crafterBlockEntity) {
			Optional<CraftingRecipe> optionalx = getPotentialResults(serverLevel, crafterBlockEntity);
			if (optionalx.isEmpty()) {
				serverLevel.levelEvent(1050, blockPos, 0);
			} else {
				crafterBlockEntity.setCraftingTicksRemaining(6);
				serverLevel.setBlock(blockPos, blockState.setValue(CRAFTING, Boolean.TRUE), 2);
				CraftingRecipe craftingRecipe = optionalx.get();
				ItemStack itemStack = craftingRecipe.assemble(crafterBlockEntity, serverLevel.registryAccess());
				this.dispenseItem(serverLevel, blockPos, crafterBlockEntity, itemStack, blockState);
				craftingRecipe.getRemainingItems(crafterBlockEntity)
						.forEach(itemStackx -> this.dispenseItem(serverLevel, blockPos, crafterBlockEntity, itemStackx, blockState));
				crafterBlockEntity.getItems().forEach(itemStackx -> {
					if (!itemStackx.isEmpty()) {
						itemStackx.shrink(1);
					}
				});
				crafterBlockEntity.setChanged();
			}
		}
	}

	public static Optional<CraftingRecipe> getPotentialResults(Level level, CraftingContainer craftingContainer) {
		return RECIPE_CACHE.get(level, craftingContainer);
	}

	private void dispenseItem(Level level, BlockPos blockPos, CrafterBlockEntity crafterBlockEntity, ItemStack itemStack, BlockState blockState) {
		Direction direction = blockState.getValue(ORIENTATION).front();
		Container container = HopperBlockEntity.getContainerAt(level, blockPos.relative(direction));
		ItemStack itemStack2 = itemStack.copy();
		if (container != null && (container instanceof CrafterBlockEntity || itemStack.getCount() > container.getMaxStackSize())) {
			while(!itemStack2.isEmpty()) {
				ItemStack itemStack3 = itemStack2.copyWithCount(1);
				ItemStack itemStack4 = HopperBlockEntity.addItem(crafterBlockEntity, container, itemStack3, direction.getOpposite());
				if (!itemStack4.isEmpty()) {
					break;
				}

				itemStack2.shrink(1);
			}
		} else if (container != null) {
			while(!itemStack2.isEmpty()) {
				int i = itemStack2.getCount();
				itemStack2 = HopperBlockEntity.addItem(crafterBlockEntity, container, itemStack2, direction.getOpposite());
				if (i == itemStack2.getCount()) {
					break;
				}
			}
		}

		if (!itemStack2.isEmpty()) {
			Vec3 vec3 = Vec3.atCenterOf(blockPos).relative(direction, 0.7);
			DefaultDispenseItemBehavior.spawnItem(level, itemStack2, 6, direction, vec3);
			level.levelEvent(1049, blockPos, 0);
			level.levelEvent(2010, blockPos, direction.get3DDataValue());
		}
	}

	@Override
	public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
		return RenderShape.MODEL;
	}

	@Override
	public @NotNull BlockState rotate(BlockState blockState, Rotation rotation) {
		return blockState.setValue(ORIENTATION, rotation.rotation().rotate(blockState.getValue(ORIENTATION)));
	}

	@Override
	public @NotNull BlockState mirror(BlockState blockState, Mirror mirror) {
		return blockState.setValue(ORIENTATION, mirror.rotation().rotate(blockState.getValue(ORIENTATION)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ORIENTATION, TRIGGERED, CRAFTING);
	}
}
