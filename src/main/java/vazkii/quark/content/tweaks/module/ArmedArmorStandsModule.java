package vazkii.quark.content.tweaks.module;

import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import vazkii.zeta.event.ZEntityConstruct;
import vazkii.zeta.event.bus.PlayEvent;
import vazkii.zeta.module.ZetaLoadModule;
import vazkii.zeta.module.ZetaModule;
import vazkii.zeta.util.Hint;

/**
 * @author WireSegal
 * Created at 8:40 AM on 8/27/19.
 */
@ZetaLoadModule(category = "tweaks")
public class ArmedArmorStandsModule extends ZetaModule {

	@Hint Item armor_stand = Items.ARMOR_STAND;

	@PlayEvent
	public void entityConstruct(ZEntityConstruct event) {
		if(event.getEntity() instanceof ArmorStand stand) {
			if(!stand.isShowArms())
				setShowArms(stand, true);
		}
	}

	private void setShowArms(ArmorStand e, boolean showArms) {
		e.getEntityData().set(ArmorStand.DATA_CLIENT_FLAGS, setBit(e.getEntityData().get(ArmorStand.DATA_CLIENT_FLAGS), 4, showArms));
	}

	private byte setBit(byte status, int bitFlag, boolean value) {
		if (value)
			status = (byte)(status | bitFlag);
		else
			status = (byte)(status & ~bitFlag);

		return status;
	}
}
