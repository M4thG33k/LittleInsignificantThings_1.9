package com.m4thg33k.lit.client.render.registers;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.blocks.ModBlocks;
import com.m4thg33k.lit.items.ModItems;
import com.m4thg33k.lit.lib.EnumBetterFurnaceType;
import com.m4thg33k.lit.lib.Names;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ItemRenderRegister {

    public static void registerItemRenderer()
    {
        //itemblocks
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.improvedFurnaceBlock),0,new ModelResourceLocation(LIT.MODID+":"+ Names.IMPROVED_FURNACE,"facing=north,on=false"));

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.improvedChestBlock),0,new ModelResourceLocation(LIT.MODID+":"+Names.IMPROVED_CHEST,"inventory"));

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.improvedCraftingTableBlock),0,new ModelResourceLocation(LIT.MODID+":"+Names.IMPROVED_CRAFTING_TABLE,"inventory"));

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.solidGeneratorBlock),0,new ModelResourceLocation(LIT.MODID+":"+Names.SOLID_GENERATOR,"facing=north,on=false"));

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.charcoalBlock),0,new ModelResourceLocation(LIT.MODID+":"+Names.CHARCOAL_BLOCK,"inventory"));

        for (EnumBetterFurnaceType type : EnumBetterFurnaceType.values())
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.betterFurnaceBlock),type.ordinal(),new ModelResourceLocation(LIT.MODID+":"+Names.BETTER_FURNACE,"facing=north,on=false,variant="+type.getName()));
        }

        //items
        for (int i=0;i<3;i++)
        {
            ModelLoader.setCustomModelResourceLocation(ModItems.itemFurnaceUpgrade,i,new ModelResourceLocation(LIT.MODID+":"+Names.FURNACE_UPGRADE,"meta="+i));
        }
    }
}
