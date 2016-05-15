package com.m4thg33k.lit.blocks.ItemBlocks;

import com.m4thg33k.lit.blocks.ModBlocks;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItemBlocks {

    public static final BetterFurnaceItemBlock betterFurnaceItemBlock = new BetterFurnaceItemBlock(ModBlocks.betterFurnaceBlock);

    public static void create()
    {
        GameRegistry.register(betterFurnaceItemBlock);
    }
}
