package vazkii.quark.content.tweaks.module;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import vazkii.zeta.event.ZLivingDeath;
import vazkii.zeta.event.bus.PlayEvent;
import vazkii.zeta.module.ZetaLoadModule;
import vazkii.zeta.module.ZetaModule;
import vazkii.zeta.util.Hint;
import vazkii.zeta.event.ZConfigChanged;
import vazkii.zeta.event.bus.LoadEvent;

@ZetaLoadModule(category = "tweaks")
public class SlimesToMagmaCubesModule extends ZetaModule {

	private static final String TAG_MAGMAED = "quark:damaged_by_magma";

	@Hint Item magma_cream = Items.MAGMA_CREAM;

	public static boolean staticEnabled;

	@LoadEvent
	public final void configChanged(ZConfigChanged event) {
		staticEnabled = enabled;
	}

	@PlayEvent
	public void onDeath(ZLivingDeath event) {
		if(event.getEntity().getType() == EntityType.SLIME && event.getSource() == DamageSource.HOT_FLOOR)
			event.getEntity().getPersistentData().putBoolean(TAG_MAGMAED, true);
	}

	public static EntityType<? extends Slime> getSlimeType(EntityType<? extends Slime> prev, Slime slime) {
		if(!staticEnabled)
			return prev;

		if(slime.getPersistentData().getBoolean(TAG_MAGMAED))
			return EntityType.MAGMA_CUBE;

		return prev;
	}

}
