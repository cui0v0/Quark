package vazkii.zeta.config;

import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

//TODO: maybe we need "boolean equals(T thing1, T thing2)"
public class ValueDefinition<T> extends Definition {
	public final T defaultValue;

	public final @Nullable Predicate<Object> validator;

	public ValueDefinition(String name, List<String> comment, T defaultValue, @Nullable Predicate<Object> validator) {
		super(name, comment);
		this.defaultValue = defaultValue;
		this.validator = validator;
	}

	public ValueDefinition(String name, List<String> comment, T defaultValue) {
		this(name, comment, defaultValue, null);
	}

	public boolean validate(Object underTest) {
		//you HAVE to start with this condition lest forge's config api explode into a million pieces
		if(underTest == null || !defaultValue.getClass().isAssignableFrom(underTest.getClass()))
			return false;
		else if(validator == null)
			return true;
		else
			return validator.test(underTest);
	}

	@Override
	public String toString() {
		return "ValueDefinition{" + name + "}";
	}
}
