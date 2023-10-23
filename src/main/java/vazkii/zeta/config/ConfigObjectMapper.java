package vazkii.zeta.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;
import vazkii.quark.base.module.config.Config;
import vazkii.quark.base.module.config.type.IConfigType;
import vazkii.zeta.module.ZetaModule;

public class ConfigObjectMapper {
	public static List<Field> walkModuleFields(Class<?> clazz) {
		List<Field> list = new ArrayList<>();
		while(clazz != ZetaModule.class && clazz != Object.class) {
			Field[] fields = clazz.getDeclaredFields();
			list.addAll(Arrays.asList(fields));

			clazz = clazz.getSuperclass();
		}

		return list;
	}

	//TODO: interaction with ConfigFlagManager
	public static void readInto(SectionDefinition sect, Object obj, Consumer<Consumer<IZetaConfigInternals>> fieldUpdaters) {
		for(Field field : walkModuleFields(obj.getClass())) {
			Config config = field.getAnnotation(Config.class);

			if(config == null)
				continue;

			field.setAccessible(true);

			//name
			String name = config.name();
			if(name.isEmpty())
				name = WordUtils.capitalizeFully(field.getName().replaceAll("(?<=.)([A-Z])", " $1"));

			//comments
			Config.Min min = field.getDeclaredAnnotation(Config.Min.class);
			Config.Max max = field.getDeclaredAnnotation(Config.Max.class);

			List<String> comment = new ArrayList<>(4);
			if(!config.description().isEmpty())
				comment.addAll(List.of(config.description().split("\n")));

			if(min != null || max != null) {
				NumberFormat format = DecimalFormat.getNumberInstance(Locale.ROOT);
				String minPart = min == null ? "(" : ((min.exclusive() ? "(" : "[") + format.format(min.value()));
				String maxPart = max == null ? ")" : (format.format(max.value()) + (max.exclusive() ? ")" : "]"));
				comment.add("Allowed values: " + minPart + "," + maxPart);
			}

			//default value
			Object defaultValue = getField(obj, field);

			//validators
			Config.Condition condition = field.getDeclaredAnnotation(Config.Condition.class);
			Predicate<Object> restriction = restrict(min, max, condition);

			if(defaultValue instanceof IConfigType configType) {
				String asSectionName = name.toLowerCase(Locale.ROOT).replace(" ", "_");
				SectionDefinition subsection = sect.getOrCreateSubsection(asSectionName, comment);

				readInto(subsection, configType, fieldUpdaters);

				//TODO: add a field updater for IConfigType (call its onReload)
			} else {
				ValueDefinition<?> def = new ValueDefinition<>(name, comment, defaultValue, restriction);

				//register a field updater
				fieldUpdaters.accept(z -> setField(obj, field, z.get(def)));

				//add it to the config tree
				sect.addValue(def);
			}
		}
	}

	private static Object getField(Object owner, Field field) {
		Object receiver = Modifier.isStatic(field.getModifiers()) ? null : owner;

		try {
			return field.get(receiver);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	private static void setField(Object owner, Field field, Object value) {
		Object receiver = Modifier.isStatic(field.getModifiers()) ? null : owner;

		try {
			field.set(receiver, value);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	private static Predicate<Object> restrict(@Nullable Config.Min min, @Nullable Config.Max max, @Nullable Config.Condition condition) {
		double minVal = min == null ? -Double.MAX_VALUE : min.value();
		double maxVal = max == null ? Double.MAX_VALUE : max.value();
		boolean minExclusive = min != null && min.exclusive();
		boolean maxExclusive = max != null && max.exclusive();

		Predicate<Object> pred = (o) -> restrict(o, minVal, minExclusive, maxVal, maxExclusive);
		if(condition != null){
			try {
				Constructor<? extends Predicate<Object>> constr = condition.value().getDeclaredConstructor();
				constr.setAccessible(true);
				Predicate<Object> additionalPredicate = constr.newInstance();
				pred = pred.and(additionalPredicate);
			} catch (Exception e) {
				throw new IllegalArgumentException("Failed to parse config Predicate annotation: ", e);
			}
		}
		return pred;
	}

	private static boolean restrict(Object o, double minVal, boolean minExclusive, double maxVal, boolean maxExclusive) {
		if (o == null)
			return false;

		if (o instanceof Number num) {
			double val = num.doubleValue();
			if (minExclusive) {
				if (minVal >= val)
					return false;
			} else if (minVal > val)
				return false;

			if (maxExclusive) {
				if (maxVal <= val)
					return false;
			} else if (maxVal < val)
				return false;
		}

		return true;
	}
}
