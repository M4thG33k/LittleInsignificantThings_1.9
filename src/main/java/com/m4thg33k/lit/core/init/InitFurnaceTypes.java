package com.m4thg33k.lit.core.init;

import com.m4thg33k.lit.api.furnace.FurnaceTypes;
import com.m4thg33k.lit.blocks.ModBlocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class InitFurnaceTypes {

    public static void createFurnaceTypes()
    {
        FurnaceTypes.addType("Improved",1.0,0,1.0,20000,new ItemStack(ModBlocks.improvedFurnaceBlock,1,0), new ItemStack(Items.flint,1,0));
    }
}
