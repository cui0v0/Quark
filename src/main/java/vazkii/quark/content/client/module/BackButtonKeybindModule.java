package vazkii.quark.content.client.module;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.platform.InputConstants.Type;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.resources.language.I18n;
import vazkii.quark.base.QuarkClient;
import vazkii.zeta.client.event.ZScreenInit;
import vazkii.zeta.client.event.ZScreenKeyPressed;
import vazkii.zeta.client.event.ZScreenMousePressed;
import vazkii.zeta.event.bus.PlayEvent;
import vazkii.zeta.module.ZetaLoadModule;
import vazkii.zeta.module.ZetaModule;
import vazkii.zeta.event.bus.LoadEvent;
import vazkii.zeta.client.event.ZKeyMapping;

@ZetaLoadModule(category = "client")
public class BackButtonKeybindModule extends ZetaModule {

	@ZetaLoadModule(clientReplacement = true)
	public static class Client extends BackButtonKeybindModule {

		private KeyMapping backKey;
		private List<GuiEventListener> listeners;

		@LoadEvent
		public void registerKeybinds(ZKeyMapping event) {
			//TODO ZETA: dunno if this predicate works lol
			backKey = event.initMouse("quark.keybind.back", 4, QuarkClient.MISC_GROUP, (key) -> key.getType() != Type.MOUSE || key.getValue() != 0);
		}

		@PlayEvent
		public void openGui(ZScreenInit.Pre event) {
			listeners = event.getListenersList();
		}

		@PlayEvent
		public void onKeyInput(ZScreenKeyPressed.Post event) {
			if(backKey.getKey().getType() == Type.KEYSYM && event.getKeyCode() == backKey.getKey().getValue())
				clicc();
		}

		@PlayEvent
		public void onMouseInput(ZScreenMousePressed.Post event) {
			int btn = event.getButton();
			if(backKey.getKey().getType() == Type.MOUSE && btn != GLFW.GLFW_MOUSE_BUTTON_LEFT && btn == backKey.getKey().getValue())
				clicc();
		}

		private void clicc() {
			ImmutableSet<String> buttons = ImmutableSet.of(
				I18n.get("gui.back"),
				I18n.get("gui.done"),
				I18n.get("gui.cancel"),
				I18n.get("gui.toTitle"),
				I18n.get("gui.toMenu"),
				I18n.get("quark.gui.config.save"));

			// Iterate this way to ensure we match the more important back buttons first
			for(String b : buttons)
				for(GuiEventListener listener : listeners) {
					if(listener instanceof Button w) {
						if(w.getMessage() != null && w.getMessage().getString().equals(b) && w.visible && w.active) {
							w.onClick(0, 0);
							return;
						}
					}
				}

			Minecraft mc = Minecraft.getInstance();
			if(mc.level != null)
				mc.setScreen(null);
		}

	}

}
