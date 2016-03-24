package com.m4thg33k.lit.blocks;

import com.m4thg33k.lit.lib.Names;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    public static ImprovedFurnaceBlock improvedFurnaceBlock = new ImprovedFurnaceBlock();

    public static void createBlocks()
    {
        GameRegistry.registerBlock(improvedFurnaceBlock, Names.IMPROVED_FURNACE);
    }
}
