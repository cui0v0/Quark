package vazkii.zetaimplforge.client;

import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import vazkii.zeta.Zeta;
import vazkii.zeta.client.ZetaClient;
import vazkii.zeta.client.event.ZEndClientTick;
import vazkii.zeta.client.event.ZFirstClientTick;
import vazkii.zeta.client.event.ZRegisterReloadListeners;
import vazkii.zeta.client.event.ZScreenshot;
import vazkii.zetaimplforge.event.client.ForgeZAddBlockColorHandlers;
import vazkii.zetaimplforge.event.client.ForgeZAddItemColorHandlers;
import vazkii.zetaimplforge.event.client.ForgeZAddModelLayers;
import vazkii.zetaimplforge.event.client.ForgeZAddModels;
import vazkii.zetaimplforge.event.client.ForgeZClick;
import vazkii.zetaimplforge.event.client.ForgeZClientSetup;
import vazkii.zetaimplforge.event.client.ForgeZHighlightBlock;
import vazkii.zetaimplforge.event.client.ForgeZRenderContainerScreen;
import vazkii.zetaimplforge.event.client.ForgeZRenderTick;
import vazkii.zetaimplforge.event.client.ForgeZScreenCharacterTyped;
import vazkii.zetaimplforge.event.client.ForgeZScreenInit;
import vazkii.zetaimplforge.event.client.ForgeZInputUpdate;
import vazkii.zetaimplforge.event.client.ForgeZKey;
import vazkii.zetaimplforge.event.client.ForgeZKeyMapping;
import vazkii.zetaimplforge.event.client.ForgeZModelBakingCompleted;
import vazkii.zetaimplforge.event.client.ForgeZPreTextureStitch;
import vazkii.zetaimplforge.event.client.ForgeZRenderOverlay;
import vazkii.zetaimplforge.event.client.ForgeZScreenKeyPressed;
import vazkii.zetaimplforge.event.client.ForgeZScreenMousePressed;
import vazkii.zetaimplforge.event.client.ForgeZTooltipComponents;

public class ForgeZetaClient extends ZetaClient {
	public ForgeZetaClient(Zeta z) {
		super(z);
	}

	@Override
	public void start() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.addListener(this::registerBlockColors);
		bus.addListener(this::registerItemColors);
		bus.addListener(this::clientSetup);
		bus.addListener(this::registerReloadListeners);
		bus.addListener(this::modelBake);
		bus.addListener(this::modelLayers);
		bus.addListener(this::textureStitch);
		bus.addListener(this::registerKeybinds);
		bus.addListener(this::registerAdditionalModels);
		bus.addListener(this::registerClientTooltipComponentFactories);

		MinecraftForge.EVENT_BUS.addListener(this::renderTick);
		MinecraftForge.EVENT_BUS.addListener(this::clientTick);
		MinecraftForge.EVENT_BUS.addListener(this::clicc);
		MinecraftForge.EVENT_BUS.addListener(this::prece);
		MinecraftForge.EVENT_BUS.addListener(this::screenshot);
		MinecraftForge.EVENT_BUS.addListener(this::movementInputUpdate);
		MinecraftForge.EVENT_BUS.addListener(this::renderBlockHighlight);

		MinecraftForge.EVENT_BUS.addListener(this::renderContainerScreenForeground);
		MinecraftForge.EVENT_BUS.addListener(this::renderContainerScreenBackground);
		MinecraftForge.EVENT_BUS.addListener(this::screenCharacterTypedPre);
		MinecraftForge.EVENT_BUS.addListener(this::screenCharacterTypedPost);
		MinecraftForge.EVENT_BUS.addListener(this::screenInitPre);
		MinecraftForge.EVENT_BUS.addListener(this::screenInitPost);
		MinecraftForge.EVENT_BUS.addListener(this::screenKeyPressedPre);
		MinecraftForge.EVENT_BUS.addListener(this::screenKeyPressedPost);
		MinecraftForge.EVENT_BUS.addListener(this::screenMousePressedPre);
		MinecraftForge.EVENT_BUS.addListener(this::screenMousePressedPost);

		MinecraftForge.EVENT_BUS.addListener(this::renderGameOverlay);
		MinecraftForge.EVENT_BUS.addListener(this::renderGameOverlayPre);
		MinecraftForge.EVENT_BUS.addListener(this::renderGameOverlayPost);
	}

	public void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		loadBus.fire(new ForgeZAddBlockColorHandlers(event));
		zeta.registry.submitBlockColors(event::register); //TODO: can be removed if IZetaBlockColorProvider goes
	}

	public void registerItemColors(RegisterColorHandlersEvent.Item event) {
		loadBus.fire(new ForgeZAddItemColorHandlers(event));
		zeta.registry.submitItemColors(event::register); //TODO: can be removed if IZetaItemColorProvider goes
	}

	public void clientSetup(FMLClientSetupEvent event) {
		loadBus.fire(new ForgeZClientSetup(event));
	}

	public void registerReloadListeners(RegisterClientReloadListenersEvent event) {
		loadBus.fire(new ZRegisterReloadListeners(event::registerReloadListener));
	}

	public void modelBake(ModelEvent.BakingCompleted event) {
		loadBus.fire(new ForgeZModelBakingCompleted(event));
	}

	public void modelLayers(EntityRenderersEvent.AddLayers event) {
		loadBus.fire(new ForgeZAddModelLayers(event));
	}

	public void textureStitch(TextureStitchEvent.Pre event) {
		loadBus.fire(new ForgeZPreTextureStitch(event));
	}

	public void registerKeybinds(RegisterKeyMappingsEvent event) {
		loadBus.fire(new ForgeZKeyMapping(event));
	}

	public void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
		loadBus.fire(new ForgeZAddModels(event));
	}

	public void registerClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
		loadBus.fire(new ForgeZTooltipComponents(event));
	}

	//TODO: move ticker stuff out of forge event handlers, subscribe to them from zeta
	public void renderTick(TickEvent.RenderTickEvent e) {
		playBus.fire(new ForgeZRenderTick(e));

		if(e.phase == TickEvent.Phase.START)
			ticker.startRenderTick(e.renderTickTime);
		else {
			ticker.endRenderTick();
		}
	}

	boolean clientTicked = false;
	public void clientTick(TickEvent.ClientTickEvent event) {
		if(event.phase == TickEvent.Phase.END) {
			ticker.endClientTick();

			if(!clientTicked) {
				loadBus.fire(new ZFirstClientTick());
				clientTicked = true;
			}

			playBus.fire(new ZEndClientTick());
		}
	}

	public void clicc(InputEvent.MouseButton e) {
		playBus.fire(new ForgeZClick(e));
	}

	public void prece(InputEvent.Key e) {
		playBus.fire(new ForgeZKey(e));
	}

	public void screenshot(ScreenshotEvent e) {
		playBus.fire(new ZScreenshot());
	}

	public void movementInputUpdate(MovementInputUpdateEvent e) {
		playBus.fire(new ForgeZInputUpdate(e));
	}

	public void renderBlockHighlight(RenderHighlightEvent.Block e) {
		playBus.fire(new ForgeZHighlightBlock(e));
	}

	public void renderContainerScreenForeground(ContainerScreenEvent.Render.Foreground e) {
		playBus.fire(new ForgeZRenderContainerScreen.Foreground(e));
	}

	public void renderContainerScreenBackground(ContainerScreenEvent.Render.Background e) {
		playBus.fire(new ForgeZRenderContainerScreen.Background(e));
	}

	public void screenCharacterTypedPre(ScreenEvent.CharacterTyped.Pre e) {
		playBus.fire(new ForgeZScreenCharacterTyped.Pre(e));
	}

	public void screenCharacterTypedPost(ScreenEvent.CharacterTyped.Post e) {
		playBus.fire(new ForgeZScreenCharacterTyped.Post(e));
	}

	public void screenInitPre(ScreenEvent.Init.Pre e) {
		playBus.fire(new ForgeZScreenInit.Pre(e));
	}

	public void screenInitPost(ScreenEvent.Init.Post e) {
		playBus.fire(new ForgeZScreenInit.Post(e));
	}
	
	public void screenKeyPressedPre(ScreenEvent.KeyPressed.Pre e) {
		playBus.fire(new ForgeZScreenKeyPressed.Pre(e));
	}

	public void screenKeyPressedPost(ScreenEvent.KeyPressed.Post e) {
		playBus.fire(new ForgeZScreenKeyPressed.Post(e));
	}

	public void screenMousePressedPre(ScreenEvent.MouseButtonPressed.Pre e) {
		playBus.fire(new ForgeZScreenMousePressed.Pre(e));
	}

	public void screenMousePressedPost(ScreenEvent.MouseButtonPressed.Post e) {
		playBus.fire(new ForgeZScreenMousePressed.Post(e));
	}

	//TODO: This probably should have been a PRE/POST event (just copying quark here)
	public void renderGameOverlay(RenderGuiOverlayEvent e) {
		if(e.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type())
			playBus.fire(new ForgeZRenderOverlay.Crosshair(e));
		else if(e.getOverlay() == VanillaGuiOverlay.HOTBAR.type())
			playBus.fire(new ForgeZRenderOverlay.Hotbar(e));
	}

	public void renderGameOverlayPre(RenderGuiOverlayEvent.Pre e) {
		if(e.getOverlay() == VanillaGuiOverlay.CHAT_PANEL.type())
			playBus.fire(new ForgeZRenderOverlay.Chat.Pre(e));
	}

	public void renderGameOverlayPost(RenderGuiOverlayEvent.Post e) {
		if(e.getOverlay() == VanillaGuiOverlay.CHAT_PANEL.type())
			playBus.fire(new ForgeZRenderOverlay.Chat.Post(e));
	}
}
