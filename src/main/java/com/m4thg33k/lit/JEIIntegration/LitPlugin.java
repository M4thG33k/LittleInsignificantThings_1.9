package com.m4thg33k.lit.JEIIntegration;

import com.m4thg33k.lit.blocks.ModBlocks;
import com.m4thg33k.lit.client.gui.GuiImprovedCraftingTable;
import com.m4thg33k.lit.inventory.ContainerImprovedCraftingTable;
import mezz.jei.api.*;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;

@JEIPlugin
public class LitPlugin extends BlankModPlugin{
    @Override
    public void register(@Nonnull IModRegistry registry) {
        IItemRegistry itemRegistry = registry.getItemRegistry();
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();

        jeiHelpers.getNbtIgnoreList().ignoreNbtTagNames(
                "AttributeModifiers",
                "CanDestroy",
                "CanPlaceOn",
                "display",
                "HideFlags",
                "RepairCost",
                "Unbreakable"
        );
        jeiHelpers.getNbtIgnoreList().ignoreNbtTagNames(Item.getItemFromBlock(ModBlocks.improvedChestBlock),"Items");

        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        //registry.addRecipeCategories(new CraftingRecipeCategory(guiHelper));

//        registry.addRecipeHandlers(
//                new ShapedOreRecipeHandler(),
//                new ShapedRecipesHandler(),
//                new ShapelessOreRecipeHandler(),
//                new ShapelessRecipesHandler());

        registry.addRecipeClickArea(GuiImprovedCraftingTable.class,88,32,28,23, VanillaRecipeCategoryUid.CRAFTING);

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

        recipeTransferRegistry.addRecipeTransferHandler(ContainerImprovedCraftingTable.class,VanillaRecipeCategoryUid.CRAFTING,1,9,10,36);

//        registry.addRecipes(CraftingManager.getInstance().getRecipeList());
    }
}
