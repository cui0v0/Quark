package org.violetmoon.quark.content.automation.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CrafterSlot extends Slot {
    private final CrafterMenu menu;

    public CrafterSlot(Container container, int i, int j, int k, CrafterMenu crafterMenu) {
        super(container, i, j, k);
        this.menu = crafterMenu;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack itemStack) {
        return !this.menu.isSlotDisabled(this.index) && super.mayPlace(itemStack);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.menu.slotsChanged(this.container);
    }
}