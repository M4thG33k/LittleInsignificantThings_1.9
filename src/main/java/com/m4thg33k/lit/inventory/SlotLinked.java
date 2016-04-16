package com.m4thg33k.lit.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotLinked extends Slot{

    private IInventory linkedInventory;
    private int linkedIndex;

    public SlotLinked(IInventory inventory,int index, int xPos, int yPos, IInventory inventory2, int index2)
    {
        super(inventory,index,xPos,yPos);

        this.linkedInventory = inventory2;
        this.linkedIndex = index2;
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
        ItemStack stack = this.getStack();
        if (stack!=null && stack.stackSize>0) {
            linkedInventory.setInventorySlotContents(linkedIndex, this.getStack());
        }
        else
        {
            linkedInventory.setInventorySlotContents(linkedIndex,null);
//            this.putStack(null);
        }
    }


}
