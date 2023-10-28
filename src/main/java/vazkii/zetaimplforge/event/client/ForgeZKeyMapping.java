package vazkii.zetaimplforge.event.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import vazkii.zeta.client.event.ZKeyMapping;

public record ForgeZKeyMapping(RegisterKeyMappingsEvent e) implements ZKeyMapping {
	@Override
	public KeyMapping register(KeyMapping key) {
		e.register(key);
		return key;
	}
}
