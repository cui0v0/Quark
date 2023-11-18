package org.violetmoon.quark.base.world;

import org.jetbrains.annotations.NotNull;

import org.violetmoon.quark.base.world.generator.IGenerator;
import org.violetmoon.zeta.module.ZetaModule;

public record WeightedGenerator(ZetaModule module,
								IGenerator generator,
								int weight) implements Comparable<WeightedGenerator> {

	@Override
	public int compareTo(@NotNull WeightedGenerator o) {
		int diff = weight - o.weight;
		if (diff != 0)
			return diff;

		return hashCode() - o.hashCode();
	}

	@Override
	public int hashCode() {
		return generator.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || (obj instanceof WeightedGenerator gen && gen.generator == generator);
	}

}
