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

    public static SolidGeneratorBlock solidGeneratorBlock = new SolidGeneratorBlock();
    public static CharcoalBlock charcoalBlock = new CharcoalBlock();

    public static BlockDeath blockDeath = new BlockDeath();

    public static BetterFurnaceBlock betterFurnaceBlock = new BetterFurnaceBlock();

    public static void createBlocks()
    {
        GameRegistry.register(improvedFurnaceBlock);
        GameRegistry.register(new ItemBlock(improvedFurnaceBlock).setRegistryName(LIT.MODID,Names.IMPROVED_FURNACE));

        GameRegistry.register(improvedChestBlock);
        GameRegistry.register(new ItemBlock(improvedChestBlock).setRegistryName(LIT.MODID,Names.IMPROVED_CHEST));

        GameRegistry.register(improvedCraftingTableBlock);
        GameRegistry.register(new ItemBlock(improvedCraftingTableBlock).setRegistryName(LIT.MODID,Names.IMPROVED_CRAFTING_TABLE));

        GameRegistry.register(solidGeneratorBlock);
        GameRegistry.register(new ItemBlock(solidGeneratorBlock).setRegistryName(LIT.MODID,Names.SOLID_GENERATOR));

        GameRegistry.register(charcoalBlock);
        GameRegistry.register(new ItemBlock(charcoalBlock).setRegistryName(LIT.MODID,Names.CHARCOAL_BLOCK));

        GameRegistry.register(blockDeath);
        GameRegistry.register(new ItemBlock(blockDeath).setRegistryName(LIT.MODID,Names.DEATH_BLOCK));

        GameRegistry.register(betterFurnaceBlock);

    }
}
