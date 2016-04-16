package com.m4thg33k.lit.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;

public class SlotCraftingLinked extends SlotCrafting {

    IInventory linkedInventory;
    int start;
    int end;

    public SlotCraftingLinked(EntityPlayer player, InventoryCrafting crafting, IInventory result,int slotIndex,int xCoord, int yCoord,IInventory linkedInventory,int start,int end)
    {
        super(player,crafting,result,slotIndex,xCoord,yCoord);

        this.linkedInventory = linkedInventory;
        this.start = start;
        this.end = end;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        super.onPickupFromSlot(playerIn, stack);

        for (int i=start;i<end;i++)
        {
            linkedInventory.decrStackSize(i,1);
        }
    }
}
