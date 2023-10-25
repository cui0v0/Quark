package vazkii.zeta.client.config;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import vazkii.quark.base.module.config.type.inputtable.ConvulsionMatrixConfig;
import vazkii.quark.base.module.config.type.inputtable.RGBColorConfig;
import vazkii.zeta.config.Definition;
import vazkii.zeta.config.SectionDefinition;
import vazkii.zeta.config.ValueDefinition;
import vazkii.zeta.client.config.definition.BooleanClientDefinition;
import vazkii.zeta.client.config.definition.ClientDefinitionExt;
import vazkii.zeta.client.config.definition.ConvulsionMatrixClientDefinition;
import vazkii.zeta.client.config.definition.DoubleClientDefinition;
import vazkii.zeta.client.config.definition.IntegerClientDefinition;
import vazkii.zeta.client.config.definition.RGBClientDefinition;
import vazkii.zeta.client.config.definition.SectionClientDefinition;
import vazkii.zeta.client.config.definition.StringClientDefinition;
import vazkii.zeta.client.config.definition.StringListClientDefinition;

public class ClientConfigManager {
	@SuppressWarnings("unchecked")
	public <D extends Definition> @NotNull ClientDefinitionExt<D> getExt(D def) {
		//TODO: make this expandable, a registry or something, and allow overriding client definitions per-config-value
		// "hint" is a sort-of gesture at this sort of api, but it should be easier for consumers to set, and there should
		// be a way of defining hint -> clientdefinition relationships besides hardcoding them

		if(def.hint instanceof RGBColorConfig)
			return (ClientDefinitionExt<D>) new RGBClientDefinition((SectionDefinition) def);
		else if(def.hint instanceof ConvulsionMatrixConfig convulsion)
			return (ClientDefinitionExt<D>) new ConvulsionMatrixClientDefinition(convulsion, (SectionDefinition) def);

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
			else if(val.defaultValue instanceof List<?>)
				return (ClientDefinitionExt<D>) new StringListClientDefinition(); //Just hope it's a list of strings!!!11
		}

		//This cast is unsound, but Default never actually uses its argument, so it's fineeeeee, right
		return (ClientDefinitionExt<D>) new ClientDefinitionExt.Default();
	}
}
