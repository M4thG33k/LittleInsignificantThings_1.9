package com.m4thg33k.lit.client.gui;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.inventory.ContainerImprovedFurnace;
import com.m4thg33k.lit.lib.Names;
import com.m4thg33k.lit.tiles.TileImprovedFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class GuiImprovedFurnace extends GuiContainer {

    private TileImprovedFurnace tileImprovedFurnace;
    private InventoryPlayer playerInventory;

    public GuiImprovedFurnace(InventoryPlayer playerInventory, TileImprovedFurnace te)
    {
        super(new ContainerImprovedFurnace(playerInventory,te));
        tileImprovedFurnace = te;
        this.xSize = 176;
        this.ySize = 166;
        this.playerInventory = playerInventory;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int w = (width-xSize)/2;
        int h = (height-ySize)/2;

        String name = tileImprovedFurnace.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(name,xSize/2-this.fontRendererObj.getStringWidth(name)/2,6,0x404040);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(),8,ySize-96+2,0x404040);

        int mX = mouseX - w;
        int mY = mouseY - h;

        if (mX<43 || mX >= 50 || mY<15 || mY >= 69)
        {
            return;
        }
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);

        int stored = ((ContainerImprovedFurnace)this.inventorySlots).storedFuel;
        int max = ((ContainerImprovedFurnace)this.inventorySlots).maxFuel;

//        this.fontRendererObj.drawString(stored + "," + max,mX+10,mY+10,0x404040);
        double perc = Math.floor(((double)stored/((double)max))*10000)/100;

        String smelt = "Able to smelt %d items.";
        String percent = "Fuel Level At: " + perc + "%.";
        smelt = String.format(smelt,((ContainerImprovedFurnace)this.inventorySlots).getNumSmeltable);

        List<String> text = new ArrayList<String>();
        text.add(percent);
        text.add(smelt);

        this.drawHoveringText(text,mX,mY);

//        int textWidth = this.fontRendererObj.getStringWidth(smelt);

//        mc.getTextureManager().bindTexture(new ResourceLocation(LIT.MODID+":textures/gui/"+ Names.IMPROVED_FURNACE + ".png"));
//        this.drawTexturedModalRect(mX-textWidth-2,mY-12,0,180,textWidth+4,18);

//        this.fontRendererObj.drawStringWithShadow(percent,mX-textWidth,mY-10,0x404040);
//        this.fontRendererObj.drawString(percent,mX-textWidth,mY-10,0x404040);
//        this.fontRendererObj.drawString(smelt,mX-textWidth,mY-2,0x404040);
//        this.fontRendererObj.drawString("Able to smelt " + ((ContainerImprovedFurnace)this.inventorySlots).getNumSmeltable + " items.",mouseX-w,mouseY-h,0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
        mc.getTextureManager().bindTexture(new ResourceLocation(LIT.MODID+":textures/gui/"+ Names.IMPROVED_FURNACE + ".png"));

        int k = (width - xSize)/2;
        int l = (height - ySize)/2;

        drawTexturedModalRect(k,l,0,0,xSize,ySize);
        int L;
        L = getBurnLeftScaled(13);
        drawTexturedModalRect(k+53,l+48-L,176,13-L,14,L+1);

        L = getCookProgressScaled(24);
        this.drawTexturedModalRect(k+76,l+34,176,14,L+1,16);

        L = getStoredFuelScaled(54);
        this.drawTexturedModalRect(k+43,l+68-L,176,84-L,7,L);

        drawUpgrades(k,l);
    }

    private int getStoredFuelScaled(int pixels)
    {
        int i = ((ContainerImprovedFurnace)this.inventorySlots).storedFuel;
        int j = ((ContainerImprovedFurnace)this.inventorySlots).maxFuel;
//        LogHelper.info("i,j: " + i + "," + j);
        return (j!=0 && i!=0) ? i*pixels/j : 0;
    }

    private int getCookProgressScaled(int pixels)
    {
        int i =((ContainerImprovedFurnace)this.inventorySlots).cookTime;
        int j = ((ContainerImprovedFurnace)this.inventorySlots).totalItemCookTime;
        return j!=0 && i!=0 ? i*pixels/j:0;
    }

    private int getBurnLeftScaled(int pixels)
    {
        int i = ((ContainerImprovedFurnace)this.inventorySlots).totalBurnTime;
        int j = ((ContainerImprovedFurnace)this.inventorySlots).burnTime;
        return j!=0 && i!=0 ? j*pixels/i : 0;
    }

    private void drawUpgrades(int k, int l)
    {
        int num = tileImprovedFurnace.getUpgradeCount();
        int numInstalled = tileImprovedFurnace.getNumUpgradesInstalled();
        ItemStack[] upgrades = tileImprovedFurnace.getUpgrades();

        mc.getTextureManager().bindTexture(new ResourceLocation(LIT.MODID+":textures/gui/"+ Names.IMPROVED_FURNACE + ".png"));
        for (int i=0; i<num;i++){
            this.drawTexturedModalRect(k+153,l+5+20*i,183,31,18,18);
        }

        for(int i=0;i<numInstalled;i++)
        {
            this.itemRender.renderItemAndEffectIntoGUI(upgrades[i],k+154,l+6+20*i);
        }
    }
}
