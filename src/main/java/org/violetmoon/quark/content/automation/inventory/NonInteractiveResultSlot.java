package org.violetmoon.quark.content.automation.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class NonInteractiveResultSlot extends Slot {
    public NonInteractiveResultSlot(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    @Override
    public void onQuickCraft(@NotNull ItemStack itemStack, @NotNull ItemStack itemStack2) {}

    @Override
    public boolean mayPickup(@NotNull Player player) {
        return false;
    }

    @Override
    public @NotNull Optional<ItemStack> tryRemove(int i, int j, @NotNull Player player) {
        return Optional.empty();
    }

    @Override
    public @NotNull ItemStack safeTake(int i, int j, @NotNull Player player) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack safeInsert(@NotNull ItemStack itemStack) {
        return itemStack;
    }

    @Override
    public @NotNull ItemStack safeInsert(@NotNull ItemStack itemStack, int i) {
        return this.safeInsert(itemStack);
    }

    @Override
    public boolean allowModification(@NotNull Player player) {
        return false;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack itemStack) {
        return false;
    }

    @Override
    public @NotNull ItemStack remove(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack itemStack) {}

    @Override
    public boolean isHighlightable() {
        return false;
    }
}