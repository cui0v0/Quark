package vazkii.zeta.config.client;

import org.jetbrains.annotations.NotNull;
import vazkii.zeta.config.Definition;
import vazkii.zeta.config.SectionDefinition;
import vazkii.zeta.config.ValueDefinition;

public class ClientConfigManager {
	@SuppressWarnings("unchecked")
	public <D extends Definition> @NotNull ClientDefinitionExt<D> getExt(D def) {
		//TODO: make this expandable, a registry or something, and allow overriding client definitions per-config-value

		if(def instanceof SectionDefinition)
			return (ClientDefinitionExt<D>) new SectionClientDefinition();
		else if(def instanceof ValueDefinition<?> val) {
			if(val.defaultValue instanceof Boolean)
				return (ClientDefinitionExt<D>) new BooleanClientDefinition();
			else if(val.defaultValue instanceof String)
				return (ClientDefinitionExt<D>) new StringClientDefinition();
			else if(val.defaultValue instanceof Integer)
				return (ClientDefinitionExt<D>) new IntegerClientDefinition();
			else if(val.defaultValue instanceof Double)
				return (ClientDefinitionExt<D>) new DoubleClientDefinition();
		}

		//This cast is unsound, but Default never actually looks at its argument, so it's fineeeeee, right
		return (ClientDefinitionExt<D>) new ClientDefinitionExt.Default();
	}
}
