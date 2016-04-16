package com.m4thg33k.lit.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;

public class LITInventoryCrafting extends InventoryCrafting{

    public LITInventoryCrafting(int width, int height)
    {
        super(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer playerIn) {
                return false;
            }
        }, width, height);
    }
}
