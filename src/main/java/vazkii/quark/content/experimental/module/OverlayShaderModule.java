package vazkii.quark.content.experimental.module;

import vazkii.quark.base.module.LoadModule;
import vazkii.zeta.module.ZetaModule;
import vazkii.quark.base.module.config.Config;

@LoadModule(category = "experimental", enabledByDefault = false)
public class OverlayShaderModule extends ZetaModule {

	
	@Config(description = "Sets the name of the shader to load on a regular basis. This can load any shader the Camera module can (and requires the Camera module enabled to apply said logic).\n"
			+ "Some useful shaders include 'desaturate', 'oversaturate', 'bumpy'\n"
			+ "Colorblind simulation shaders are available in the form of 'deuteranopia', 'protanopia', 'tritanopia', and 'achromatopsia'")
	public static String shader = "none";
	
}
