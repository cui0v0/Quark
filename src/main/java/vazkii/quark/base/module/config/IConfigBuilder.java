package vazkii.quark.base.module.config;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraftforge.common.ForgeConfigSpec;

public interface IConfigBuilder {

	<T> ForgeConfigSpec configure(Function<IConfigBuilder, T> consumer);
	void push(String s, Object holderObject);
	void pop();
	void comment(String s);

	<T> ForgeConfigSpec.ConfigValue<List<? extends T>> defineList(String name, List<? extends T> default_, Supplier<List<? extends T>> getter, Predicate<Object> predicate);
	<T> ForgeConfigSpec.ConfigValue<T> defineObj(String name, T default_, Supplier<T> getter, Predicate<Object> predicate);

	default ForgeConfigSpec.ConfigValue<Boolean> defineBool(String name, Supplier<Boolean> getter, boolean default_) {
		return defineObj(name, default_, getter, x -> true);
	}

}
