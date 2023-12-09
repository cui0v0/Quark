package org.violetmoon.zeta.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.Nullable;

/**
 * Common superclass of a... "thing" in a config definition (a value or section).
 *
 * @see org.violetmoon.zeta.config.SectionDefinition
 * @see org.violetmoon.zeta.config.ValueDefinition
 */
public abstract class Definition {
	public final String name;
	public final List<String> comment;

	//late-bound from the SectionDefinition constructor (yeah)
	public @Nullable SectionDefinition parent = null;
	public Collection<String> path = Collections.emptyList();

	//TODO: awkward, set from ConfigObjectMapper, used in ClientConfigManager
	// Treat this as immutable and for reference only !!!!
	public @Nullable Object hint;

	public Definition(Definition.Builder<?, ? extends Definition> builder) {
		this.name = Preconditions.checkNotNull(builder.name, "Definitions require a name");
		this.comment = builder.comment;
		this.hint = builder.hint;
	}

	public void setParent(SectionDefinition parent) {
		this.parent = parent;

		if(parent == null)
			path = Collections.emptyList(); //TODO: skipping the "root" SectionDefinition in a clumsy way
		else {
			path = new ArrayList<>(parent.path);
			path.add(name);
			((ArrayList<?>) path).trimToSize();
		}
	}

	public String[] commentToArray() {
		return comment == null ? new String[0] : comment.toArray(new String[0]);
	}

	public String commentToString() {
		return comment == null ? "" : String.join("\n", comment);
	}

	private static final boolean translationDebug = System.getProperty("zeta.configTranslations", null) != null;

	//note this is SHARED code, so i cant directly use i18n
	public final String getGuiDisplayName(Function<String, String> i18nDotGet) {
		String transKey = path.stream()
			.map(s -> s.toLowerCase(Locale.ROOT).replace(" ", "_").replaceAll("[^A-Za-z0-9_]", ""))
			.collect(Collectors.joining(".", "quark.config.", ".name"));

		if(translationDebug)
			return transKey;

		String localized = i18nDotGet.apply(transKey);
		if(localized.isEmpty() || localized.equals(transKey)) //no user-specified translation
			return name;

		return localized;
	}

	// i hope this isnt too complicated...
	public abstract static class Builder<B extends Builder<B, T>, T extends Definition> {
		protected @Nullable String name;
		protected List<String> comment = new ArrayList<>(2);
		protected @Nullable Object hint;

		public abstract T build();

		public B name(String name) {
			this.name = name;
			return downcast();
		}

		public B comment(String comment) {
			return comment(List.of(comment));
		}

		public B comment(List<String> comment) {
			comment.stream()
				.flatMap(line -> Stream.of(line.split("\n")))
				.filter(line -> !line.trim().isEmpty())
				.forEach(this.comment::add);

			return downcast();
		}

		public B hint(Object hint) {
			this.hint = hint;
			return downcast();
		}

		@SuppressWarnings("unchecked")
		protected <X> X downcast() {
			return (X) this;
		}
	}
}
