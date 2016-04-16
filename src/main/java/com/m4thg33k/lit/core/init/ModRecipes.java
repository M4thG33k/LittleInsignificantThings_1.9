package com.m4thg33k.lit.core.init;

import com.m4thg33k.lit.api.chest.ChestTypes;
import com.m4thg33k.lit.api.furnace.FurnaceTypes;
import com.m4thg33k.lit.blocks.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {

    public static void initRecipes()
    {
        FurnaceTypes.registerRecipes();
        ChestTypes.regRecipes();

        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.improvedCraftingTableBlock,1),new ItemStack(Blocks.crafting_table,1));
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.improvedCraftingTableBlock,1),new ItemStack(Blocks.crafting_table,1),new ItemStack(Items.flint,1)); //just in case of recipe conflicts
    }
}
