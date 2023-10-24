package vazkii.zeta.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChangeSet implements IZetaConfigInternals {
	private final IZetaConfigInternals internals;

	private record Entry<T>(ValueDefinition<T> valueDef, T currentValue, T nextValue) { }
	private final Map<ValueDefinition<?>, Entry<?>> changes = new HashMap<>();

	public ChangeSet(IZetaConfigInternals internals) {
		this.internals = internals;
	}

	// Changing single values

	@Override
	public <T> void set(ValueDefinition<T> valueDef, T nextValue) {
		T currentValue = internals.get(valueDef);
		if(Objects.equals(currentValue, nextValue))
			removeChange(valueDef);
		else
			changes.put(valueDef, new Entry<>(valueDef, currentValue, nextValue));
	}

	public void toggle(ValueDefinition<Boolean> boolDef) {
		set(boolDef, !get(boolDef));
	}

	public <T> void resetToDefault(ValueDefinition<T> valueDef) {
		set(valueDef, valueDef.defaultValue);
	}

	public <T> void removeChange(ValueDefinition<T> valueDef) {
		changes.remove(valueDef);
	}

	// Changing whole sections

	public void resetToDefault(SectionDefinition sectionDef) {
		sectionDef.getValues().forEach(this::resetToDefault);
		sectionDef.getSubsections().forEach(this::resetToDefault);
	}

	public void removeChange(SectionDefinition sectionDef) {
		sectionDef.getValues().forEach(this::removeChange);
		sectionDef.getSubsections().forEach(this::removeChange);
	}

	// Dirtiness

	public <T> boolean isDirty(ValueDefinition<T> valueDef) {
		return changes.containsKey(valueDef);
	}

	public boolean isDirty(SectionDefinition sectionDefinition) {
		return sectionDefinition.getValues().stream().anyMatch(this::isDirty) ||
			sectionDefinition.getSubsections().stream().anyMatch(this::isDirty);
	}

	public boolean isDirty(Definition def) {
		if(def instanceof ValueDefinition<?> val)
			return isDirty(val);
		else
			return isDirty((SectionDefinition) def);
	}

	public int changeCount() {
		return changes.size();
	}

	// Getting data as if the changes were applied

	@Override
	public <T> T get(ValueDefinition<T> definition) {
		@SuppressWarnings("unchecked")
		Entry<T> entry = (Entry<T>) changes.get(definition);

		if(entry != null)
			return entry.nextValue;
		else
			return internals.get(definition);
	}

	@Override
	public void refresh(Definition thing) {
		// NO-OP
	}

	// Application

	public void applyAllChanges() {
		changes.values().forEach(this::applyOneChange);
		changes.clear();
	}

	private <T> void applyOneChange(Entry<T> entry) {
		internals.set(entry.valueDef, entry.nextValue);
	}
}
