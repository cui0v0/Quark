package vazkii.zeta.config;

import java.util.List;
import java.util.function.Predicate;

public class ValueDefinition<T> extends Definition {
	public final T defaultValue;

	public final Predicate<Object> validator;

	public ValueDefinition(String name, List<String> comment, T defaultValue, Predicate<Object> validator) {
		super(name, comment);
		this.defaultValue = defaultValue;
		this.validator = validator;
	}

	public ValueDefinition(String name, List<String> comment, T defaultValue) {
		this(name, comment, defaultValue, __ -> true);
	}

	@Override
	public String toString() {
		return "ValueDefinition{name='" + name + "'}";
	}
}
