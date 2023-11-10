package org.violetmoon.zeta.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ManualTrigger extends SimpleCriterionTrigger<ManualTrigger.Instance> {

	final ResourceLocation id;

	public ManualTrigger(ResourceLocation id) {
		this.id = id;
	}

	public void trigger(ServerPlayer player) {
		trigger(player, instance -> true);
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	protected Instance createInstance(JsonObject json, EntityPredicate.Composite predicate, DeserializationContext ctx) {
		return new Instance(id, predicate);
	}

	public static class Instance extends AbstractCriterionTriggerInstance {

		public Instance(ResourceLocation id, EntityPredicate.Composite predicate) {
			super(id, predicate);
		}

	}

}
