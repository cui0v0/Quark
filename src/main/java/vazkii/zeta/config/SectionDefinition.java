package vazkii.zeta.config;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SectionDefinition extends Definition {
	public final Map<String, SectionDefinition> subsections = new LinkedHashMap<>();
	public final Map<String, ValueDefinition<?>> values = new LinkedHashMap<>();

	public SectionDefinition(String name, List<String> comment) {
		super(name, comment);
	}

	public SectionDefinition getOrCreateSubsection(String name, List<String> comment) {
		return subsections.computeIfAbsent(name, __ -> new SectionDefinition(name, comment));
	}

	public <T> ValueDefinition<T> addValue(String name, List<String> comment, T defaultValue) {
		ValueDefinition<T> val = new ValueDefinition<>(name, comment, defaultValue);
		addValue(val);
		return val;
	}

	public void addSubsection(SectionDefinition subsection) {
		subsections.put(subsection.name, subsection);
	}

	public void addValue(ValueDefinition<?> value) {
		values.put(value.name, value);
	}

	public Collection<SectionDefinition> getSubsections() {
		return subsections.values();
	}

	public Collection<ValueDefinition<?>> getValues() {
		return values.values();
	}

	@Override
	public String toString() {
		return "SectionDefinition{name='" + name + "'}";
	}
}
