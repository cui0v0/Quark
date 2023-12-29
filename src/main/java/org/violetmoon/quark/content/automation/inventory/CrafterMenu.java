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

package org.violetmoon.quark.content.automation.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.inventory.BackpackMenu;
import org.violetmoon.quark.content.automation.block.CrafterBlock;
import org.violetmoon.quark.content.automation.module.CrafterModule;

public class CrafterMenu extends AbstractContainerMenu implements ContainerListener {
	private final ResultContainer resultContainer = new ResultContainer();
	private final ContainerData containerData;
	private final Player player;
	private final CraftingContainer container;

	public CrafterMenu(int i, Inventory inventory) {
		super(CrafterModule.menuType, i);
		this.player = inventory.player;
		this.containerData = new SimpleContainerData(10);
		this.container = new TransientCraftingContainer(this, 3, 3);
		this.addSlots(inventory);
	}

	public CrafterMenu(int i, Inventory inventory, CraftingContainer craftingContainer, ContainerData containerData) {
		super(CrafterModule.menuType, i);
		this.player = inventory.player;
		this.containerData = containerData;
		this.container = craftingContainer;
		checkContainerSize(craftingContainer, 9);
		craftingContainer.startOpen(inventory.player);
		this.addSlots(inventory);
		this.addSlotListener(this);
	}

	public static CrafterMenu fromNetwork(int windowId, Inventory playerInventory, FriendlyByteBuf buf) {
		return new CrafterMenu(windowId, playerInventory);
	}

	private void addSlots(Inventory inventory) {
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				int k = j + i * 3;
				this.addSlot(new CrafterSlot(this.container, k, 26 + j * 18, 17 + i * 18, this));
			}
		}

		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(int i = 0; i < 9; ++i) {
			this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
		}

		this.addSlot(new NonInteractiveResultSlot(this.resultContainer, 0, 134, 35));
		this.addDataSlots(this.containerData);
		this.refreshRecipeResult();
	}

	public void setSlotState(int i, boolean bl) {
		CrafterSlot crafterSlot = (CrafterSlot) this.getSlot(i);
		this.containerData.set(crafterSlot.index, bl ? 0 : 1);
		this.broadcastChanges();
	}

	public boolean isSlotDisabled(int i) {
		if (i > -1 && i < 9) {
			return this.containerData.get(i) == 1;
		} else {
			return false;
		}
	}

	public boolean isPowered() {
		return this.containerData.get(9) == 1;
	}

	@Override
	public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(i);
		if (slot != null && slot.hasItem()) {
			ItemStack itemStack2 = slot.getItem();
			itemStack = itemStack2.copy();
			if (i < 9) {
				if (!this.moveItemStackTo(itemStack2, 9, 45, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(itemStack2, 0, 9, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemStack2);
		}

		return itemStack;
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return this.container.stillValid(player);
	}

	private void refreshRecipeResult() {
		Player player = this.player;
		if (player instanceof ServerPlayer serverPlayer) {
			Level levelx = serverPlayer.level();
			ItemStack itemStack = CrafterBlock.getPotentialResults(levelx, this.container)
					.map(craftingRecipe -> craftingRecipe.assemble(this.container, player.level().registryAccess()))
					.orElse(ItemStack.EMPTY);
			this.resultContainer.setItem(0, itemStack);
		}
	}

	public Container getContainer() {
		return this.container;
	}

	@Override
	public void slotChanged(@NotNull AbstractContainerMenu abstractContainerMenu, int i, @NotNull ItemStack itemStack) {
		this.refreshRecipeResult();
	}

	@Override
	public void dataChanged(@NotNull AbstractContainerMenu abstractContainerMenu, int i, int j) {}
}
