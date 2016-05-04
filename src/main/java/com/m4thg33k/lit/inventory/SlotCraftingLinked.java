package com.m4thg33k.lit.inventory;

import com.m4thg33k.lit.core.util.LogHelper;
import com.m4thg33k.lit.tiles.TileImprovedCraftingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;

public class SlotCraftingLinked extends SlotCrafting {


    TileImprovedCraftingTable linkedInventory;
    protected InventoryCrafting iCrafting;
    int start;
    int end;

    public SlotCraftingLinked(EntityPlayer player, InventoryCrafting crafting, IInventory result, int slotIndex, int xCoord, int yCoord, TileImprovedCraftingTable linkedInventory, int start, int end)
    {
        super(player,crafting,result,slotIndex,xCoord,yCoord);

        iCrafting = crafting;
        this.linkedInventory = linkedInventory;
        this.start = start;
        this.end = end;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        super.onPickupFromSlot(playerIn, stack);


        if (!playerIn.worldObj.isRemote) {
            for (int i = start; i < end; i++) {
                linkedInventory.setInventorySlotContents(i,iCrafting.getStackInSlot(i-start));
//                linkedInventory.decrStackSize(i, 1);
//                if (linkedInventory.getStackInSlot(i)!=null && linkedInventory.getStackInSlot(i).stackSize==0)
//                {
//                    linkedInventory.setInventorySlotContents(i,null);
//                }
            }
        }
        linkedInventory.syncInventories();
//        LogHelper.info(linkedInventory.getResult()==null?"":linkedInventory.getResult().getDisplayName());
//        linkedInventory.syncInventories();
    }
}
