package com.m4thg33k.lit.tiles;

import com.m4thg33k.lit.lib.Names;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTiles {

    public static void init()
    {
        String prefix = "tile.lit";
        GameRegistry.registerTileEntity(TileImprovedFurnace.class,prefix + Names.IMPROVED_FURNACE);
        GameRegistry.registerTileEntity(TileImprovedChest.class,prefix + Names.IMPROVED_CHEST);
    }
}
