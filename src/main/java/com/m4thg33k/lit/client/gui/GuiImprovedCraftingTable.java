package com.m4thg33k.lit.client.gui;

import com.m4thg33k.lit.inventory.ContainerImprovedCraftingTable;
import com.m4thg33k.lit.tiles.TileImprovedCraftingTable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiImprovedCraftingTable extends GuiContainer{

    private static final ResourceLocation background = new ResourceLocation("textures/gui/container/crafting_table.png");

    private TileImprovedCraftingTable tile;
    private InventoryPlayer playerInventory;

    public GuiImprovedCraftingTable(InventoryPlayer playerInventory,TileImprovedCraftingTable te)
    {
        super(new ContainerImprovedCraftingTable(playerInventory,te));

        this.tile = te;
        this.playerInventory = playerInventory;

        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        String name = "Improved Crafting Table";//tile.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(name,xSize/2-this.fontRendererObj.getStringWidth(name)/2,6,0x404040);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(),8,ySize-96+2,0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
        mc.getTextureManager().bindTexture(background);
        int i = (this.width - this.xSize)/2;
        int j = (this.height - this.ySize)/2;
        this.drawTexturedModalRect(i,j,0,0,this.xSize,this.ySize);
    }
}
