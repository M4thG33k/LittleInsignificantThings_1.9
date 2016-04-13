package com.m4thg33k.lit.client.render.registers;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.blocks.ModBlocks;
import com.m4thg33k.lit.items.ModItems;
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

        //items
        for (int i=0;i<3;i++)
        {
            ModelLoader.setCustomModelResourceLocation(ModItems.itemFurnaceUpgrade,i,new ModelResourceLocation(LIT.MODID+":"+Names.FURNACE_UPGRADE,"meta="+i));
        }
    }
}
