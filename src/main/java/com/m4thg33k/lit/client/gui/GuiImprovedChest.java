package com.m4thg33k.lit.client.gui;

import com.m4thg33k.lit.api.chest.ChestTypes;
import com.m4thg33k.lit.inventory.ContainerImprovedChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;

public class GuiImprovedChest  extends GuiContainer{

    private ChestTypes type;

    public GuiImprovedChest(ChestTypes type, IInventory player, IInventory chest)
    {
        super(new ContainerImprovedChest(player,chest,type));
        this.type = type;
        this.xSize = type.getWidth();
        this.ySize = type.getHeight();
        this.allowUserInput = false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
        this.mc.getTextureManager().bindTexture(type.getGuiLocation());
        int x = (width-xSize)/2;
        int y = (height-ySize)/2;
        drawTexturedModalRect(x,y,0,0,xSize,ySize);
    }
}
