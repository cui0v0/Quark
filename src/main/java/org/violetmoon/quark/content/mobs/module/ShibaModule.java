package org.violetmoon.quark.content.mobs.module;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements.Type;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.config.Config;
import org.violetmoon.quark.base.config.type.CompoundBiomeConfig;
import org.violetmoon.quark.base.config.type.EntitySpawnConfig;
import org.violetmoon.quark.base.handler.GeneralConfig;
import org.violetmoon.quark.base.world.EntitySpawnHandler;
import org.violetmoon.quark.content.mobs.client.render.entity.ShibaRenderer;
import org.violetmoon.quark.content.mobs.entity.Shiba;
import org.violetmoon.zeta.advancement.ManualTrigger;
import org.violetmoon.zeta.advancement.modifier.TwoByTwoModifier;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZEntityAttributeCreation;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "mobs")
public class ShibaModule extends ZetaModule {

	public static EntityType<Shiba> shibaType;

	@Config
	public static EntitySpawnConfig spawnConfig = new EntitySpawnConfig(40, 1, 3, CompoundBiomeConfig.fromBiomeTags(false, BiomeTags.IS_MOUNTAIN));

	@Config public static boolean ignoreAreasWithSkylight = false;

	@Hint(key = "shiba_find_low_light") Item torch = Items.TORCH;

	public static ManualTrigger shibaHelpTrigger;

	@LoadEvent
	public final void register(ZRegister event) {
		shibaType = EntityType.Builder.of(Shiba::new, MobCategory.CREATURE)
				.sized(0.8F, 0.8F)
				.clientTrackingRange(8)
				.setCustomClientFactory((spawnEntity, world) -> new Shiba(shibaType, world))
				.build("shiba");
		Quark.ZETA.registry.register(shibaType, "shiba", Registries.ENTITY_TYPE);

		EntitySpawnHandler.registerSpawn(shibaType, MobCategory.CREATURE, Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, spawnConfig);
		EntitySpawnHandler.addEgg(this, shibaType, 0xa86741, 0xe8d5b6, spawnConfig);

		event.getAdvancementModifierRegistry().addModifier(new TwoByTwoModifier(this, ImmutableSet.of(shibaType))
			.setCondition(() -> GeneralConfig.enableAdvancementModification));

		shibaHelpTrigger = event.getAdvancementModifierRegistry().registerManualTrigger("shiba_help");
	}

	@LoadEvent
	public final void entityAttrs(ZEntityAttributeCreation e) {
		e.put(shibaType, Wolf.createAttributes().build());
	}

	@LoadEvent
	public final void clientSetup(ZClientSetup event) {
		EntityRenderers.register(shibaType, ShibaRenderer::new);
	}

}
