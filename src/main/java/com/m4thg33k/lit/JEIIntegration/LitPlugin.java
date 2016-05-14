package com.m4thg33k.lit.JEIIntegration;

import com.m4thg33k.lit.blocks.ModBlocks;
import com.m4thg33k.lit.client.gui.GuiImprovedCraftingTable;
import com.m4thg33k.lit.client.gui.GuiImprovedFurnace;
import com.m4thg33k.lit.inventory.ContainerImprovedCraftingTable;
import com.m4thg33k.lit.inventory.ContainerImprovedFurnace;
import mezz.jei.api.*;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@JEIPlugin
public class LitPlugin extends BlankModPlugin{
    @Override
    public void register(@Nonnull IModRegistry registry) {
        IItemRegistry itemRegistry = registry.getItemRegistry();
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();

        jeiHelpers.getItemBlacklist().addItemToBlacklist(new ItemStack(ModBlocks.blockDeath));

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
        registry.addRecipeClickArea(GuiImprovedFurnace.class,77,34,22,16,VanillaRecipeCategoryUid.SMELTING,VanillaRecipeCategoryUid.FUEL);

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.improvedCraftingTableBlock),VanillaRecipeCategoryUid.CRAFTING);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.improvedFurnaceBlock),VanillaRecipeCategoryUid.SMELTING,VanillaRecipeCategoryUid.FUEL);


        recipeTransferRegistry.addRecipeTransferHandler(ContainerImprovedCraftingTable.class,VanillaRecipeCategoryUid.CRAFTING,1,9,10,36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerImprovedFurnace.class,VanillaRecipeCategoryUid.SMELTING,1,1,3,36);
        recipeTransferRegistry.addRecipeTransferHandler(ContainerImprovedFurnace.class,VanillaRecipeCategoryUid.FUEL,0,1,3,36);
//        registry.addRecipes(CraftingManager.getInstance().getRecipeList());
    }
}
