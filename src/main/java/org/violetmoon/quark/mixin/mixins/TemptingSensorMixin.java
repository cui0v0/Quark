package org.violetmoon.quark.mixin.mixins;

import java.util.List;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.violetmoon.quark.content.automation.module.FeedingTroughModule;

@Mixin(TemptingSensor.class)
public class TemptingSensorMixin {

	@Unique private static final int RATE = 20; //TODO: I think this needs to be applied every tick

	@ModifyExpressionValue(
		method = "doTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/PathfinderMob;)V",
		at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;collect(Ljava/util/stream/Collector;)Ljava/lang/Object;")
	)
	//Mixin doesn't statically know the type and defaults to Object for some reason. I think the injector matches before a CHECKCAST.
	public Object quark$findTroughs(Object playersErased, ServerLevel level, PathfinderMob mob) {
		@SuppressWarnings("unchecked")
		List<Player> players = (List<Player>) playersErased;

		if(mob instanceof Animal animal) {
			Player first = players.isEmpty() ? null : players.get(0);
			Player replacement = FeedingTroughModule.modifyTemptingSensor(first, (TemptingSensor) (Object) this, animal, level);

			//Collectors.toList returns a mutable list, so it's okay to modify it. This is technically a Java implementation detail.
			if(first != replacement)
				if(players.isEmpty())
					players.add(replacement);
				else
					players.set(0, replacement);
		}

		return players;
	}
}
