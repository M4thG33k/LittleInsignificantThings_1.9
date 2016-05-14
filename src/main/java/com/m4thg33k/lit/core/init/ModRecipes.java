package com.m4thg33k.lit.core.init;

import com.m4thg33k.lit.api.chest.ChestTypes;
import com.m4thg33k.lit.api.furnace.FurnaceTypes;
import com.m4thg33k.lit.blocks.ModBlocks;
import com.m4thg33k.lit.lib.LITConfigs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {

    public static void initRecipes()
    {
        FurnaceTypes.registerRecipes();
        ChestTypes.regRecipes();

        if (LITConfigs.USE_ALTERNATE_CRAFTING_TABLE_RECIPE)
        {
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.improvedCraftingTableBlock,1),new ItemStack(Blocks.crafting_table,1),new ItemStack(Items.flint,1)); //just in case of recipe conflicts
        }
        else
        {
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.improvedCraftingTableBlock,1),new ItemStack(Blocks.crafting_table,1));
        }

        GameRegistry.addRecipe(new ItemStack(ModBlocks.solidGeneratorBlock,1),"t","f","t",'t',new ItemStack(Blocks.redstone_torch,1),'f',new ItemStack(ModBlocks.improvedFurnaceBlock,1));
        GameRegistry.addRecipe(new ItemStack(ModBlocks.charcoalBlock,1),"ccc","ccc","ccc",'c',new ItemStack(Items.coal,1,1));
        GameRegistry.addShapelessRecipe(new ItemStack(Items.coal,9,1),new ItemStack(ModBlocks.charcoalBlock,1));

    }
}
