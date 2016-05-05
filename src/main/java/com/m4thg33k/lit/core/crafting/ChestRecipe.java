package com.m4thg33k.lit.core.crafting;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ChestRecipe extends ShapedOreRecipe {

    public ChestRecipe(Block result, Object... recipe)
    {
        super(result, recipe);
    }
    public ChestRecipe(Item result, Object... recipe)
    {
        super(result, recipe);
    }
    public ChestRecipe(ItemStack result, Object... recipe)
    {
        super(result,recipe);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1) {
        ItemStack toReturn = super.getCraftingResult(var1);


        for (int i=0;i<var1.getSizeInventory();i++)
        {
            ItemStack stack = var1.getStackInSlot(i);
            if (stack!=null && stack.hasTagCompound() && stack.getTagCompound().hasKey("Items"))
            {
                toReturn.setTagCompound(stack.getTagCompound());
            }
        }

        return toReturn;
    }
}
