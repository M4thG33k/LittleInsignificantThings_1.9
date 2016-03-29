package com.m4thg33k.lit.inventory;

import com.m4thg33k.lit.api.chest.ChestTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerImprovedChest extends Container {

    private ChestTypes type;
    private EntityPlayer player;
    private IInventory chest;

    public ContainerImprovedChest(IInventory player, IInventory chest, ChestTypes type)
    {
        this.chest = chest;
        this.player = ((InventoryPlayer)player).player;
        this.type = type;
        this.chest.openInventory(this.player);
        layoutContainer(player,chest,type);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return chest.isUseableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemStack = null;
        Slot slot = inventorySlots.get(index);
        if (slot!=null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();
            if (index<type.getSize())
            {
                if (!mergeItemStack(stack,type.getSize(),inventorySlots.size(),true))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(stack,0,type.getSize(),false))
            {
                return null;
            }
            if (stack.stackSize==0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        return itemStack;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        chest.closeInventory(playerIn);
    }

    protected void layoutContainer(IInventory playerInventory,IInventory chestInventory, ChestTypes type)
    {
        for (int chestRow=0;chestRow<type.getRowCount();chestRow++)
        {
            for (int chestCol=0;chestCol<type.getRowLength();chestCol++)
            {
                addSlotToContainer(new Slot(chestInventory,chestCol+chestRow*type.getRowLength(),12+chestCol*18,8+chestRow*18));
            }
        }

        int leftCol = (type.getWidth()-162)/2+1;
        for (int playerInvRows=0;playerInvRows<3;playerInvRows++)
        {
            for (int playerInvCols=0;playerInvCols<9;playerInvCols++)
            {
                addSlotToContainer(new Slot(playerInventory,playerInvCols+playerInvRows*9+9,leftCol+playerInvCols*18,type.getHeight()-(4-playerInvRows)*18-10));
            }
        }

        for (int hotbar=0;hotbar<9;hotbar++)
        {
            addSlotToContainer(new Slot(playerInventory,hotbar,leftCol+hotbar*18,type.getHeight()-24));
        }
    }

    public EntityPlayer getPlayer()
    {
        return player;
    }

    public int getNumColumns()
    {
        return type.getRowLength();
    }
}
