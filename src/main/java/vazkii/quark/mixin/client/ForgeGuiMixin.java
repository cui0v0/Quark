package vazkii.quark.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraftforge.client.gui.overlay.ForgeGui;
import vazkii.quark.base.Quark;
import vazkii.quark.content.client.module.ElytraIndicatorModule;

@Mixin(ForgeGui.class)
public class ForgeGuiMixin {

	@ModifyConstant(method = "renderArmor", constant = @Constant(intValue = 20), remap = false)
	private static int renderArmor(int curr) {
		ElytraIndicatorModule module = Quark.ZETA.modules.get(ElytraIndicatorModule.class);
		return module == null ? curr : module.getArmorLimit(curr);
	}
	
}
