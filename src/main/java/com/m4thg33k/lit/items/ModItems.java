package com.m4thg33k.lit.items;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

    public static ItemFurnaceUpgrade itemFurnaceUpgrade = new ItemFurnaceUpgrade();

    public static void createItems()
    {
        GameRegistry.register(itemFurnaceUpgrade);
    }
}
