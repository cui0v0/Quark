package vazkii.quark.base.module.config.type.inputtable;

import vazkii.quark.base.client.config.screen.inputtable.IInputtableConfigType;
import vazkii.quark.base.module.config.type.AbstractConfigType;

public abstract class AbstractInputtableType<T extends IInputtableConfigType<T>> extends AbstractConfigType implements IInputtableConfigType<T> {

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void markDirty(boolean dirty) {
		//nope
	}
}
