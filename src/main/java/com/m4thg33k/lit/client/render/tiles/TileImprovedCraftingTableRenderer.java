package com.m4thg33k.lit.client.render.tiles;

import com.m4thg33k.lit.tiles.TileImprovedCraftingTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileImprovedCraftingTableRenderer extends TileEntitySpecialRenderer {

    public TileImprovedCraftingTableRenderer()
    {

    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        if (te==null)
        {
            return;
        }

        TileImprovedCraftingTable tile = (TileImprovedCraftingTable)te;


        GlStateManager.pushMatrix();
        ItemStack stack = tile.getStackInSlot(0);

        GlStateManager.translate(x,y,z);
        this.renderItem(te.getWorld(),stack,0);

        GlStateManager.popMatrix();
    }

    private void renderItem(World world, ItemStack stack, int index)
    {
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        if (stack!=null)
        {
            GlStateManager.translate(0.5,1.5,0.5);
            EntityItem entityItem = new EntityItem(world,0.0d,0.0d,0.0d,stack);
            entityItem.getEntityItem().stackSize = 1;
            entityItem.hoverStart = 0.0f;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

//            GlStateManager.scale(0.25,0.25,0.25);
            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            itemRenderer.renderItem(entityItem.getEntityItem(),ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
}
