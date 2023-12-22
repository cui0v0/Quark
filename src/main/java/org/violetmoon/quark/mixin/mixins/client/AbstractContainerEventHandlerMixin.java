package org.violetmoon.quark.mixin.mixins.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.ChatScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.violetmoon.quark.content.tweaks.client.screen.widgets.TranslucentButton;

@Mixin(AbstractContainerEventHandler.class)
public class AbstractContainerEventHandlerMixin {
	/*
	 * @author IThundxr
	 * @reason Without this while in the chat screen using arrow keys will focus off of the chat bar and onto the button
	 * This is a quick hacky fix to prevent this. Relates to issue (#4436) https://github.com/VazkiiMods/Quark/issues/4436
	 */
	@Inject(method = "setFocused", at = @At("HEAD"), cancellable = true)
	private void quark$doNotFocusInChatScreen(GuiEventListener guiEventListener, CallbackInfo ci) {
		if((Object) this instanceof ChatScreen chatScreen) {
			if(guiEventListener instanceof Button && guiEventListener.getClass() == TranslucentButton.class) {
				for(GuiEventListener element : chatScreen.children()) {
					if(element instanceof EditBox) {
						chatScreen.setFocused(element);
						break;
					}
				}
				ci.cancel();
			}
		}
	}
}
