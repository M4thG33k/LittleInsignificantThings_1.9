package com.m4thg33k.lit.inventory;

import com.m4thg33k.lit.blocks.ModBlocks;
import com.m4thg33k.lit.tiles.TileImprovedCraftingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerImprovedCraftingTable extends Container {

    private TileImprovedCraftingTable te;

    public InventoryCrafting craftMatrix = new InventoryCrafting(this,3,3);
    public IInventory craftResult = new InventoryCraftResult();


    public ContainerImprovedCraftingTable(InventoryPlayer playerInventory, TileImprovedCraftingTable tileEntity)
    {
        te = tileEntity;

        this.addSlotToContainer(new SlotCrafting(playerInventory.player,this.craftMatrix,this.craftResult,0,124,35));

        for (int i=0;i<3;i++)
        {
            for (int j=0;j<3;j++)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix,j+i*3,30+j*18,17+i*18));
            }
        }

        for (int k = 0; k < 3; ++k)
        {
            for (int i1 = 0; i1 < 9; ++i1)
            {
                this.addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l)
        {
            this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 142));
        }

        syncInventories(false);

        this.onCraftMatrixChanged(this.craftMatrix);

    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix,this.te.getWorld()));
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        syncInventories(true);
        te.closeInventory(playerIn);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.te.getWorld().getBlockState(te.getPos()).getBlock() == ModBlocks.improvedCraftingTableBlock && playerIn.getDistanceSq(te.getPos().add(0.5,0.5,0.5)) <= 64;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack stack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot!=null && slot.getHasStack())
        {
            ItemStack itemStack = slot.getStack();
            stack = itemStack.copy();

            if (index==0) //crafting output
            {
                if (!this.mergeItemStack(itemStack,10,46,true))
                {
                    return null;
                }

                slot.onSlotChange(itemStack,stack);
            }
            else if (index>=0 && index<37) //main inventory
            {
                if (!this.mergeItemStack(itemStack,37,46,false))
                {
                    return null;
                }
            }
            else if (index >= 37 && index < 46) //hotbar
            {
                if (!this.mergeItemStack(itemStack,10,37,false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemStack,10,46,false)) //crafting grid
            {
                return null;
            }

            if (itemStack.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemStack.stackSize == stack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(playerIn,itemStack);
        }

        this.syncInventories(true);
        return stack;
    }

    protected void syncInventories(boolean toTile)
    {
        if (toTile)
        {
            for (int i=0;i<9;i++)
            {
                te.setInventorySlotContents(i,inventorySlots.get(1+i).getStack());
            }
        }
        else
        {
            for (int i=0;i<9;i++)
            {
                inventorySlots.get(1+i).putStack(te.getStackInSlot(i));
            }
        }
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack,slotIn);
    }
}
