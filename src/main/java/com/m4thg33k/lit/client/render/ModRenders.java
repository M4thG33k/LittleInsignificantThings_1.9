package com.m4thg33k.lit.client.render;

import com.m4thg33k.lit.client.render.tiles.TileImprovedChestRenderer;
import com.m4thg33k.lit.tiles.TileImprovedChest;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModRenders {

    public static void init()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileImprovedChest.class,new TileImprovedChestRenderer());
    }
}