package vazkii.zetaimplforge.client;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
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
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import vazkii.quark.mixin.client.accessor.AccessorBlockColors;
import vazkii.quark.mixin.client.accessor.AccessorItemColors;
import vazkii.zeta.Zeta;
import vazkii.zeta.client.ZetaClient;
import vazkii.zeta.client.event.ZAddBlockColorHandlers;
import vazkii.zeta.client.event.ZAddItemColorHandlers;
import vazkii.zeta.client.event.ZAddModelLayers;
import vazkii.zeta.client.event.ZAddModels;
import vazkii.zeta.client.event.ZClick;
import vazkii.zeta.client.event.ZClientSetup;
import vazkii.zeta.client.event.ZEndClientTick;
import vazkii.zeta.client.event.ZFirstClientTick;
import vazkii.zeta.client.event.ZGatherTooltipComponents;
import vazkii.zeta.client.event.ZHighlightBlock;
import vazkii.zeta.client.event.ZInputUpdate;
import vazkii.zeta.client.event.ZKey;
import vazkii.zeta.client.event.ZKeyMapping;
import vazkii.zeta.client.event.ZModelBakingCompleted;
import vazkii.zeta.client.event.ZPreTextureStitch;
import vazkii.zeta.client.event.ZRegisterReloadListeners;
import vazkii.zeta.client.event.ZRenderContainerScreen;
import vazkii.zeta.client.event.ZRenderOverlay;
import vazkii.zeta.client.event.ZRenderTick;
import vazkii.zeta.client.event.ZScreenCharacterTyped;
import vazkii.zeta.client.event.ZScreenInit;
import vazkii.zeta.client.event.ZScreenKeyPressed;
import vazkii.zeta.client.event.ZScreenMousePressed;
import vazkii.zeta.client.event.ZScreenshot;
import vazkii.zeta.client.event.ZStartClientTick;
import vazkii.zeta.client.event.ZTooltipComponents;
import vazkii.zetaimplforge.client.event.ForgeZAddBlockColorHandlers;
import vazkii.zetaimplforge.client.event.ForgeZAddItemColorHandlers;
import vazkii.zetaimplforge.client.event.ForgeZAddModelLayers;
import vazkii.zetaimplforge.client.event.ForgeZAddModels;
import vazkii.zetaimplforge.client.event.ForgeZClick;
import vazkii.zetaimplforge.client.event.ForgeZClientSetup;
import vazkii.zetaimplforge.client.event.ForgeZGatherTooltipComponents;
import vazkii.zetaimplforge.client.event.ForgeZHighlightBlock;
import vazkii.zetaimplforge.client.event.ForgeZRenderContainerScreen;
import vazkii.zetaimplforge.client.event.ForgeZRenderTick;
import vazkii.zetaimplforge.client.event.ForgeZScreenCharacterTyped;
import vazkii.zetaimplforge.client.event.ForgeZScreenInit;
import vazkii.zetaimplforge.client.event.ForgeZInputUpdate;
import vazkii.zetaimplforge.client.event.ForgeZKey;
import vazkii.zetaimplforge.client.event.ForgeZKeyMapping;
import vazkii.zetaimplforge.client.event.ForgeZModelBakingCompleted;
import vazkii.zetaimplforge.client.event.ForgeZPreTextureStitch;
import vazkii.zetaimplforge.client.event.ForgeZRenderOverlay;
import vazkii.zetaimplforge.client.event.ForgeZScreenKeyPressed;
import vazkii.zetaimplforge.client.event.ForgeZScreenMousePressed;
import vazkii.zetaimplforge.client.event.ForgeZTooltipComponents;

public class ForgeZetaClient extends ZetaClient {
	public ForgeZetaClient(Zeta z) {
		super(z);
	}

	@Override
	public @Nullable BlockColor getBlockColor(BlockColors bcs, Block block) {
		return ForgeRegistries.BLOCKS.getDelegate(block)
			.map(ref -> ((AccessorBlockColors) bcs).quark$getBlockColors().get(ref))
			.orElse(null);
	}

	@Override
	public @Nullable ItemColor getItemColor(ItemColors ics, ItemLike itemlike) {
		return ForgeRegistries.ITEMS.getDelegate(itemlike.asItem())
			.map(ref -> ((AccessorItemColors) ics).quark$getItemColors().get(ref))
			.orElse(null);
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
		MinecraftForge.EVENT_BUS.addListener(this::gatherTooltipComponents);

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

		MinecraftForge.EVENT_BUS.addListener(this::renderGameOverlayNeitherPreNorPost);
		MinecraftForge.EVENT_BUS.addListener(this::renderGameOverlayPre);
		MinecraftForge.EVENT_BUS.addListener(this::renderGameOverlayPost);
	}

	public void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		ZAddBlockColorHandlers e = loadBus.fire(new ForgeZAddBlockColorHandlers(event), ZAddBlockColorHandlers.class);
		loadBus.fire(e.makePostEvent(), ZAddBlockColorHandlers.Post.class);
	}

	public void registerItemColors(RegisterColorHandlersEvent.Item event) {
		ZAddItemColorHandlers e = loadBus.fire(new ForgeZAddItemColorHandlers(event), ZAddItemColorHandlers.class);
		loadBus.fire(e.makePostEvent(), ZAddItemColorHandlers.Post.class);
	}

	public void clientSetup(FMLClientSetupEvent event) {
		loadBus.fire(new ForgeZClientSetup(event), ZClientSetup.class);
	}

	public void registerReloadListeners(RegisterClientReloadListenersEvent event) {
		loadBus.fire(new ZRegisterReloadListeners(event::registerReloadListener), ZRegisterReloadListeners.class);
	}

	public void modelBake(ModelEvent.BakingCompleted event) {
		loadBus.fire(new ForgeZModelBakingCompleted(event), ZModelBakingCompleted.class);
	}

	public void modelLayers(EntityRenderersEvent.AddLayers event) {
		loadBus.fire(new ForgeZAddModelLayers(event), ZAddModelLayers.class);
	}

	public void textureStitch(TextureStitchEvent.Pre event) {
		loadBus.fire(new ForgeZPreTextureStitch(event), ZPreTextureStitch.class);
	}

	public void registerKeybinds(RegisterKeyMappingsEvent event) {
		loadBus.fire(new ForgeZKeyMapping(event), ZKeyMapping.class);
	}

	public void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
		loadBus.fire(new ForgeZAddModels(event), ZAddModels.class);
	}

	public void registerClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
		loadBus.fire(new ForgeZTooltipComponents(event), ZTooltipComponents.class);
	}

	//TODO: move ticker stuff out of forge event handlers, subscribe to them from zeta
	// Also these events are a mess lol
	public void renderTick(TickEvent.RenderTickEvent e) {
		playBus.fire(new ForgeZRenderTick(e), ZRenderTick.class);

		if(e.phase == TickEvent.Phase.START)
			ticker.startRenderTick(e.renderTickTime);
		else {
			ticker.endRenderTick();
		}
	}

	boolean clientTicked = false;
	public void clientTick(TickEvent.ClientTickEvent event) {
		if(event.phase == TickEvent.Phase.START) {
			playBus.fire(new ZStartClientTick());
		} else {
			ticker.endClientTick();

			if(!clientTicked) {
				loadBus.fire(new ZFirstClientTick());
				clientTicked = true;
			}

			playBus.fire(new ZEndClientTick());
		}
	}

	public void clicc(InputEvent.MouseButton e) {
		playBus.fire(new ForgeZClick(e), ZClick.class);
	}

	public void prece(InputEvent.Key e) {
		playBus.fire(new ForgeZKey(e), ZKey.class);
	}

	public void screenshot(ScreenshotEvent e) {
		playBus.fire(new ZScreenshot());
	}

	public void movementInputUpdate(MovementInputUpdateEvent e) {
		playBus.fire(new ForgeZInputUpdate(e), ZInputUpdate.class);
	}

	public void renderBlockHighlight(RenderHighlightEvent.Block e) {
		playBus.fire(new ForgeZHighlightBlock(e), ZHighlightBlock.class);
	}

	public void gatherTooltipComponents(RenderTooltipEvent.GatherComponents e) {
		playBus.fire(new ForgeZGatherTooltipComponents(e), ZGatherTooltipComponents.class);
	}

	public void renderContainerScreenForeground(ContainerScreenEvent.Render.Foreground e) {
		playBus.fire(new ForgeZRenderContainerScreen.Foreground(e), ZRenderContainerScreen.Foreground.class);
	}

	public void renderContainerScreenBackground(ContainerScreenEvent.Render.Background e) {
		playBus.fire(new ForgeZRenderContainerScreen.Background(e), ZRenderContainerScreen.Background.class);
	}

	public void screenCharacterTypedPre(ScreenEvent.CharacterTyped.Pre e) {
		playBus.fire(new ForgeZScreenCharacterTyped.Pre(e), ZScreenCharacterTyped.Pre.class);
	}

	public void screenCharacterTypedPost(ScreenEvent.CharacterTyped.Post e) {
		playBus.fire(new ForgeZScreenCharacterTyped.Post(e), ZScreenCharacterTyped.Post.class);
	}

	public void screenInitPre(ScreenEvent.Init.Pre e) {
		playBus.fire(new ForgeZScreenInit.Pre(e), ZScreenInit.Pre.class);
	}

	public void screenInitPost(ScreenEvent.Init.Post e) {
		playBus.fire(new ForgeZScreenInit.Post(e), ZScreenInit.Post.class);
	}
	
	public void screenKeyPressedPre(ScreenEvent.KeyPressed.Pre e) {
		playBus.fire(new ForgeZScreenKeyPressed.Pre(e), ZScreenKeyPressed.Pre.class);
	}

	public void screenKeyPressedPost(ScreenEvent.KeyPressed.Post e) {
		playBus.fire(new ForgeZScreenKeyPressed.Post(e), ZScreenKeyPressed.Post.class);
	}

	public void screenMousePressedPre(ScreenEvent.MouseButtonPressed.Pre e) {
		playBus.fire(new ForgeZScreenMousePressed.Pre(e), ZScreenMousePressed.Pre.class);
	}

	public void screenMousePressedPost(ScreenEvent.MouseButtonPressed.Post e) {
		playBus.fire(new ForgeZScreenMousePressed.Post(e), ZScreenMousePressed.Post.class);
	}

	//TODO: This probably should have been a PRE/POST event (just copying quark here)
	public void renderGameOverlayNeitherPreNorPost(RenderGuiOverlayEvent e) {
		if(e.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type())
			playBus.fire(new ForgeZRenderOverlay.Crosshair(e), ZRenderOverlay.Crosshair.class);
		else if(e.getOverlay() == VanillaGuiOverlay.HOTBAR.type())
			playBus.fire(new ForgeZRenderOverlay.Hotbar(e), ZRenderOverlay.Hotbar.class);
	}

	public void renderGameOverlayPre(RenderGuiOverlayEvent.Pre e) {
		if(e.getOverlay() == VanillaGuiOverlay.ARMOR_LEVEL.type())
			playBus.fire(new ForgeZRenderOverlay.ArmorLevel.Pre(e), ZRenderOverlay.ArmorLevel.Pre.class);
		else if(e.getOverlay() == VanillaGuiOverlay.CHAT_PANEL.type())
			playBus.fire(new ForgeZRenderOverlay.Chat.Pre(e), ZRenderOverlay.Chat.Pre.class);
		else if(e.getOverlay() == VanillaGuiOverlay.HOTBAR.type())
			playBus.fire(new ForgeZRenderOverlay.Hotbar.Pre(e), ZRenderOverlay.Hotbar.Pre.class);
	}

	public void renderGameOverlayPost(RenderGuiOverlayEvent.Post e) {
		if(e.getOverlay() == VanillaGuiOverlay.ARMOR_LEVEL.type())
			playBus.fire(new ForgeZRenderOverlay.ArmorLevel.Post(e), ZRenderOverlay.ArmorLevel.Post.class);
		else if(e.getOverlay() == VanillaGuiOverlay.CHAT_PANEL.type())
			playBus.fire(new ForgeZRenderOverlay.Chat.Post(e), ZRenderOverlay.Chat.Post.class);
		else if(e.getOverlay() == VanillaGuiOverlay.HOTBAR.type())
			playBus.fire(new ForgeZRenderOverlay.Hotbar.Post(e), ZRenderOverlay.Hotbar.Post.class);
	}
}
