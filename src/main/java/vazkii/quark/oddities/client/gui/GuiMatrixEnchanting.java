package vazkii.quark.oddities.client.gui;

import java.awt.peer.ListPeer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiScrollingList;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.util.ClientTicker;
import vazkii.quark.base.lib.LibMisc;
import vazkii.quark.base.network.message.MessageMatrixEnchanterOperation;
import vazkii.quark.oddities.inventory.ContainerMatrixEnchanting;
import vazkii.quark.oddities.inventory.EnchantmentMatrix;
import vazkii.quark.oddities.inventory.EnchantmentMatrix.Piece;
import vazkii.quark.oddities.tile.TileMatrixEnchanter;

public class GuiMatrixEnchanting extends GuiContainer {

	private static final ResourceLocation BACKGROUND = new ResourceLocation(LibMisc.MOD_ID, "textures/misc/matrix_enchanting.png");

	InventoryPlayer playerInv;
	TileMatrixEnchanter enchanter;
	
	PieceList pieceList;
	
	int selectedPiece = -1;
	int gridHoverX, gridHoverY;
	List<Integer> listPieces = null;
	
	public GuiMatrixEnchanting(InventoryPlayer playerInv, TileMatrixEnchanter enchanter) {
		super(new ContainerMatrixEnchanting(playerInv, enchanter));
		this.playerInv = playerInv;
		this.enchanter = enchanter;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		selectedPiece = -1;
		addButton(new GuiButton(0, guiLeft, guiTop - 24, 40, 20, "Add"));
		pieceList = new PieceList(this, 29, 64, guiTop + 11, guiLeft + 139, 22);
	}

	@Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(BACKGROUND);
        int i = guiLeft;
        int j = guiTop;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(enchanter.getDisplayName().getUnformattedText(), 12, 5, 4210752);
		fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
		
		if(enchanter.matrix != null) {
			listPieces = enchanter.matrix.benchedPieces;
			renderMatrixGrid(enchanter.matrix);
		}
	}
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        
        if(enchanter.matrix != null) {
        	RenderHelper.enableGUIStandardItemLighting();
            pieceList.drawScreen(mouseX, mouseY, ClientTicker.partialTicks);
        }
        
        renderHoveredToolTip(mouseX, mouseY);
    }
	
	@Override
	public void handleMouseInput() throws IOException {
		int mouseX = Mouse.getEventX() * width / mc.displayWidth;
		int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
		
		if(enchanter.matrix != null)
			pieceList.handleMouseInput(mouseX, mouseY);
		
		int gridMouseX = mouseX - guiLeft - 86;
		int gridMouseY = mouseY - guiTop - 11;
		
		gridHoverX = gridMouseX / 10;
		gridHoverY = gridMouseY / 10;
		if(gridHoverX < 0 || gridHoverX > 4 || gridHoverY < 0 || gridHoverY > 4) {
			gridHoverX = -1;
			gridHoverY = -1;
		}
		
		super.handleMouseInput();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		if(mouseButton == 0 && gridHoverX != -1) { // left click
			if(selectedPiece != -1) {
				place(selectedPiece, gridHoverX, gridHoverY);
				selectedPiece = -1;
			} else {
				int hover = enchanter.matrix.matrix[gridHoverX][gridHoverY];
				remove(hover);
				selectedPiece = hover;
			}
		}
		
	}
	
	private void renderMatrixGrid(EnchantmentMatrix matrix) {
        mc.getTextureManager().bindTexture(BACKGROUND);
		GlStateManager.pushMatrix();
		GlStateManager.translate(86, 11, 0);
		
		for(int i : matrix.placedPieces) {
			Piece piece = getPiece(i);
			GlStateManager.pushMatrix();
			GlStateManager.translate(piece.x * 10, piece.y * 10, 0);
			renderPiece(piece, 1F);
			GlStateManager.popMatrix();
		}
		
		if(selectedPiece != -1 && gridHoverX != -1) {
			Piece piece = getPiece(selectedPiece);
			if(piece != null && matrix.canPlace(piece, gridHoverX, gridHoverY)) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(gridHoverX * 10, gridHoverY * 10, 0);
				float a = (float) ((Math.sin(ClientTicker.total * 0.2) + 1) * 0.4 + 0.4); 
				renderPiece(piece, a);
				GlStateManager.popMatrix();
			}
		}
		
		GlStateManager.popMatrix();
	}
	
	private void renderPiece(Piece piece, float a) {
		float r = (float) ((piece.color >> 16) & 0xFF) / 255F;
		float g = (float) ((piece.color >> 8) & 0xFF) / 255F;
		float b = (float) (piece.color & 0xFF) / 255F;
		GlStateManager.color(r, g, b, a);
		
		for(int[] block : piece.blocks)
			renderBlock(block[0], block[1], piece.type);
		
		GlStateManager.color(1F, 1F, 1F);
	}
	
	private void renderBlock(int x, int y, int type) {
        drawTexturedModalRect(x * 10, y * 10, 11 + type * 10, ySize, 10, 10);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		add();
	}
	
	public void add() {
		send(TileMatrixEnchanter.OPER_ADD, 0, 0, 0);
	}
	
	public void place(int id, int x, int y) {
		send(TileMatrixEnchanter.OPER_PLACE, id, x, y);
	}
	
	public void remove(int id) {
		send(TileMatrixEnchanter.OPER_REMOVE, id, 0, 0);
	}
	
	public void rotate(int id) {
		send(TileMatrixEnchanter.OPER_REMOVE, id, 0, 0);
	}
	
	private void send(int operation, int arg0, int arg1, int arg2) {
		MessageMatrixEnchanterOperation message = new MessageMatrixEnchanterOperation(operation, arg0, arg1, arg2);
		NetworkHandler.INSTANCE.sendToServer(message);
	}
	
	private Piece getPiece(int id) {
		EnchantmentMatrix matrix = enchanter.matrix;
		if(matrix != null)
			return matrix.pieces.get(id);
		
		return null;
	}
	
	public static class PieceList extends GuiScrollingList {

		private GuiMatrixEnchanting parent;
		
		public PieceList(GuiMatrixEnchanting parent, int width, int height, int top, int left, int entryHeight) {
			super(parent.mc, width, height, top, top + height, left, entryHeight, parent.width, parent.height);
			this.parent = parent;
		}
		
		@Override
		protected int getSize() {
			return parent.listPieces == null ? 0 : parent.listPieces.size();
		}

		@Override
		protected void elementClicked(int index, boolean doubleClick) {
			int id = parent.listPieces.get(index);
			if(parent.selectedPiece == id)
				parent.selectedPiece = -1;
			else parent.selectedPiece = id;
		}

		@Override
		protected boolean isSelected(int index) {
			int id = parent.listPieces.get(index);
			return parent.selectedPiece == id;
		}

		@Override
		protected void drawBackground() {
			
		}

		@Override
		protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
			int id = parent.listPieces.get(slotIdx);
			
			Piece piece = parent.getPiece(id);
			if(piece != null) {
		        parent.mc.getTextureManager().bindTexture(BACKGROUND);
				GlStateManager.pushMatrix();
				GlStateManager.translate(left + (listWidth - 7) / 2, slotTop + slotHeight / 2, 0);
				GlStateManager.scale(0.5, 0.5, 0.5);
				GlStateManager.translate(-4, -8, 0);
				parent.renderPiece(piece, 1F);
				GlStateManager.popMatrix();
			}
		}
		
	}
	
}
