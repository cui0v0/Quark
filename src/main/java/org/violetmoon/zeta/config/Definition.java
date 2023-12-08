package org.violetmoon.zeta.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

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
	public final @Nullable SectionDefinition parent;
	public final Collection<String> path;

	//TODO: awkward, set from ConfigObjectMapper
	// Treat this as immutable and for reference only !!!!
	public @Nullable Object hint;

	public Definition(String name, List<String> comment, @Nullable SectionDefinition parent) {
		this.name = name;
		this.parent = parent;

		//TODO lol; mainly sanitizing it so it won't blow up when the forge config system reads me
		this.comment = comment.stream()
			.flatMap(s -> Arrays.stream(s.split("\n")))
			.filter(line -> !line.trim().isEmpty())
			.collect(Collectors.toList());

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
}
