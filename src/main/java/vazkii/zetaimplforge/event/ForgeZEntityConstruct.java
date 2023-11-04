package vazkii.zetaimplforge.event;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import vazkii.zeta.event.ZEntityConstruct;

public class ForgeZEntityConstruct implements ZEntityConstruct {
	private final EntityEvent.EntityConstructing e;

	public ForgeZEntityConstruct(EntityEvent.EntityConstructing e) {
		this.e = e;
	}

	@Override
	public Entity getEntity() {
		return e.getEntity();
	}
}
