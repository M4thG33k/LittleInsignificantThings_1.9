package com.m4thg33k.lit.client.render.tiles;

import com.m4thg33k.lit.api.chest.ChestTypes;
import com.m4thg33k.lit.blocks.ImprovedChestBlock;
import com.m4thg33k.lit.blocks.ModBlocks;
import com.m4thg33k.lit.tiles.TileImprovedChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TileImprovedChestRenderer extends TileEntitySpecialRenderer{

    private ModelChest model;

    public TileImprovedChestRenderer()
    {
        model = new ModelChest();
    }

    public void render(TileImprovedChest tile,double x,double y, double z, float partialTick, int breakStage)
    {
        if (tile==null)
        {
            return;
        }

        int facing = 3;
        ChestTypes type = tile.getType();

        if (tile.hasWorldObj() && tile.getWorld().getBlockState(tile.getPos()).getBlock() instanceof ImprovedChestBlock)
        {
            facing = tile.getFacing().ordinal();
            type = tile.getType();
        }

        if (breakStage>=0)
        {
            bindTexture(DESTROY_STAGES[breakStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            bindTexture(type.getModelTexture());
        }
        GlStateManager.pushMatrix();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GlStateManager.scale(1.0F, -1F, -1F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        int k = 0;
        if (facing == 2) {
            k = 180;
        }
        if (facing == 3) {
            k = 0;
        }
        if (facing == 4) {
            k = 90;
        }
        if (facing == 5) {
            k = -90;
        }
        GlStateManager.rotate(k, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        float lidangle = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * partialTick;
        lidangle = 1.0F - lidangle;
        lidangle = 1.0F - lidangle * lidangle * lidangle;
        model.chestLid.rotateAngleX = -((lidangle * 3.141593F) / 2.0F);
        // Render the chest itself
        model.renderAll();
        if (breakStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }

        GlStateManager.popMatrix();
        GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        render((TileImprovedChest)te,x,y,z,partialTicks,destroyStage);
    }
}
