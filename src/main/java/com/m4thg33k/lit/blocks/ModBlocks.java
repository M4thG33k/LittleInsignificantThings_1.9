package com.m4thg33k.lit.blocks;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.lib.Names;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    public static ImprovedFurnaceBlock improvedFurnaceBlock = new ImprovedFurnaceBlock();
    public static ImprovedChestBlock improvedChestBlock = new ImprovedChestBlock();
    public static ImprovedCraftingTableBlock improvedCraftingTableBlock = new ImprovedCraftingTableBlock();

    public static void createBlocks()
    {
        GameRegistry.register(improvedFurnaceBlock);
        GameRegistry.register(new ItemBlock(improvedFurnaceBlock).setRegistryName(LIT.MODID,Names.IMPROVED_FURNACE));

        GameRegistry.register(improvedChestBlock);
        GameRegistry.register(new ItemBlock(improvedChestBlock).setRegistryName(LIT.MODID,Names.IMPROVED_CHEST));

        GameRegistry.register(improvedCraftingTableBlock);
        GameRegistry.register(new ItemBlock(improvedCraftingTableBlock).setRegistryName(LIT.MODID,Names.IMPROVED_CRAFTING_TABLE));
    }
}
