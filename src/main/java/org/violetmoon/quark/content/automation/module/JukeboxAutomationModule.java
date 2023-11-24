package org.violetmoon.quark.content.automation.module;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import org.jetbrains.annotations.NotNull;

import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.play.loading.ZAttachCapabilities;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "automation")
public class JukeboxAutomationModule extends ZetaModule {

	@Hint Item jukebox = Items.JUKEBOX;

	private static final ResourceLocation JUKEBOX_ITEM_HANDLER = new ResourceLocation(Quark.MOD_ID, "jukebox_item_handler");

	@LoadEvent
	public void setup(ZCommonSetup e) {
		MusicDiscBehaviour behaviour = new MusicDiscBehaviour();
		e.enqueueWork(() -> BuiltInRegistries.ITEM.forEach(i -> {
			if (i instanceof RecordItem)
				DispenserBlock.DISPENSER_REGISTRY.put(i, behaviour);
		}));
	}

	@PlayEvent
	public void attachCaps(ZAttachCapabilities.BlockEntityCaps event) {
		if(event.getObject() instanceof JukeboxBlockEntity jukebox)
			event.addCapabilityForgeApi(JUKEBOX_ITEM_HANDLER, new JukeboxItemHandler(jukebox));
	}

	public record JukeboxItemHandler(JukeboxBlockEntity tile) implements ICapabilityProvider, IItemHandler {

		@Override
		public int getSlots() {
			return 1;
		}

		@NotNull
		@Override
		public ItemStack getStackInSlot(int slot) {
			return tile.getItem(0);
		}

		@NotNull
		@Override
		public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
			return stack;
		}

		@NotNull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			ItemStack stackAt = getStackInSlot(slot);
			if (!stackAt.isEmpty()) {
				ItemStack copy = stackAt.copy();
				if (!simulate) {
					tile.getLevel().levelEvent(LevelEvent.SOUND_PLAY_JUKEBOX_SONG, tile.getBlockPos(), 0);
					tile.setItem(0, ItemStack.EMPTY);

					BlockState state = tile.getBlockState().setValue(JukeboxBlock.HAS_RECORD, false);
					tile.getLevel().setBlock(tile.getBlockPos(), state, 1 | 2);
				}

				return copy;
			}

			return ItemStack.EMPTY;
		}

		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}

		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			return false;
		}

		@NotNull
		@Override
		public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
			if (cap == ForgeCapabilities.ITEM_HANDLER)
				return LazyOptional.of(() -> this).cast();

			return LazyOptional.empty();
		}

	}

	public static class MusicDiscBehaviour extends OptionalDispenseItemBehavior {

		@NotNull
		@Override
		protected ItemStack execute(BlockSource source, @NotNull ItemStack stack) {
			Direction dir = source.getBlockState().getValue(DispenserBlock.FACING);
			BlockPos pos = source.getPos().relative(dir);
			Level world = source.getLevel();
			BlockState state = world.getBlockState(pos);

			if(state.getBlock() == Blocks.JUKEBOX) {
				JukeboxBlockEntity jukebox = (JukeboxBlockEntity) world.getBlockEntity(pos);
				if (jukebox != null) {
					ItemStack currentRecord = jukebox.getItem(0);
					((JukeboxBlock) (state.getBlock())).setRecord(null, world, pos, state, stack);
					world.levelEvent(null, LevelEvent.SOUND_PLAY_JUKEBOX_SONG, pos, Item.getId(stack.getItem()));

					return currentRecord;
				}
			}

			return super.execute(source, stack);
		}

	}

}
