package com.m4thg33k.lit.client.render.tiles;

import com.m4thg33k.lit.core.util.LogHelper;
import com.m4thg33k.lit.tiles.TileImprovedCraftingTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileImprovedCraftingTableRenderer extends TileEntitySpecialRenderer {

    private double factor = 0.1875;

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
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);

        GlStateManager.translate(x,y,z);
        World world = te.getWorld();
        for (int i=0;i<9;i++)
        {
            this.renderItem(world,tile.getStackInSlot(i),(i/3)-1,0,(i%3)-1,0.125);
        }

        ItemStack result = tile.getResult();
        renderItem(tile.getWorld(),result,0,0.5,0,0.5);

        GlStateManager.popMatrix();
    }

    private void renderItem(World world, ItemStack stack, int xIndex,double extraHeight,int zIndex,double scale)
    {
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        if (stack!=null)
        {
//            LogHelper.info("Rendering index: " + index);
            ItemStack single = new ItemStack(stack.getItem(),1,stack.getItemDamage());

            EntityItem entityItem = new EntityItem(world,0.0d,0.0d,0.0d,single);
            entityItem.getEntityItem().stackSize = 1;
            entityItem.hoverStart = 0.0f;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5,1+0.0625+extraHeight,0.5);
            GlStateManager.translate(xIndex*-factor,0,zIndex*factor);
            GlStateManager.disableLighting();

            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

            float rotation = (float)(720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

            GlStateManager.rotate(rotation,0,1,0);
            GlStateManager.scale(scale,scale,scale);
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
