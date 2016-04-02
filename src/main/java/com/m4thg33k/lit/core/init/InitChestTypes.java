package com.m4thg33k.lit.core.init;

import com.m4thg33k.lit.api.chest.ChestTypes;
import com.m4thg33k.lit.blocks.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class InitChestTypes {

    public static void createChestTypes()
    {
        ChestTypes.addType("Improved",36,false,new ResourceLocation("lit","textures/gui/ImprovedChestGui.png"),184,166,4,9,new ResourceLocation("lit","textures/model/ImprovedChest.png"),new ItemStack(Items.flint,1),new ItemStack(Item.getItemFromBlock(ModBlocks.improvedChestBlock),1),true,false,false,"");
    }
}