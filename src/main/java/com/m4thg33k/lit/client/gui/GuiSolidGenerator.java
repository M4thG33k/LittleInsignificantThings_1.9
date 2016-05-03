package com.m4thg33k.lit.client.gui;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.inventory.ContainerSolidGenerator;
import com.m4thg33k.lit.lib.Names;
import com.m4thg33k.lit.tiles.TileSolidGenerator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class GuiSolidGenerator extends GuiContainer {

    private TileSolidGenerator tileSolidGenerator;
    private InventoryPlayer playerInventory;

    public GuiSolidGenerator(InventoryPlayer playerInventory, TileSolidGenerator te)
    {
        super(new ContainerSolidGenerator(playerInventory,te));
        this.tileSolidGenerator = te;
        this.playerInventory = playerInventory;

        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int w = (width-xSize)/2;
        int h = (height-ySize)/2;

        String name = tileSolidGenerator.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(name,xSize/2-this.fontRendererObj.getStringWidth(name)/2,6,0x404040);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(),8,ySize-96+2,0x404040);

        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);

        int max = tileSolidGenerator.getMaxEnergyStored(EnumFacing.DOWN);
        int stored = tileSolidGenerator.getEnergyStored(EnumFacing.DOWN);

//        String energy = "" + stored + "/" + max + " RF";
//        this.fontRendererObj.drawString(energy,0,0,0x404040);

        int mX = mouseX-w;
        int mY = mouseY-h;

        if (mX<72 || mX >= 77 || mY<16 || mY>=68)
        {
            return;
        }

        List<String> text = new ArrayList<String>();
        text.add("" + stored + "/" + max + " RF");
        this.drawHoveringText(text,mouseX-w,mouseY-h);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
        mc.getTextureManager().bindTexture(new ResourceLocation(LIT.MODID+":textures/gui/"+ Names.SOLID_GENERATOR + ".png"));

        int k = (width-xSize)/2;
        int l = (height-ySize)/2;

        drawTexturedModalRect(k,l,0,0,xSize,ySize);

        int L;
        L = getEnergyStoredScaled(54);
        this.drawTexturedModalRect(k+71,l+68-L,176,84-L,7,L);

        L = getBurnPercentageScale(13);
        drawTexturedModalRect(k+80,l+66-L,176,13-L,14,L+1);
    }

    private int getEnergyStoredScaled(int pixels)
    {
        double percentage = tileSolidGenerator.getEnergyStored(EnumFacing.DOWN)==0 ? 0 : tileSolidGenerator.getEnergyPercentage();
        return ((int)(percentage*pixels));
    }

    private int getBurnPercentageScale(int pixels)
    {
        return tileSolidGenerator.getBurnPercentage()==0?0:pixels-(int)(pixels*tileSolidGenerator.getBurnPercentage());
    }
}
