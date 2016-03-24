package com.m4thg33k.lit.api.furnace;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

/**
 * used to create different types of improved furnaces
 * all types should be established after (for recipe sake)
 */

public class FurnaceTypes {

    public static final ArrayList<FurnaceTypes> allTypes = new ArrayList<FurnaceTypes>();

    private String typeName;
    private double cookTimeFactor;
    private int upgradeCount;
    private double fuelBooster;
    private int maxFuel;
    private ItemStack block;
    private ItemStack material;

    public FurnaceTypes(String typeName, double cookTimeFactor, int upgradeCount, double fuelBooster, int maxFuel, ItemStack block, ItemStack material)
    {
        this.typeName = typeName;
        this.cookTimeFactor = cookTimeFactor;
        this.upgradeCount = upgradeCount;
        this.fuelBooster = fuelBooster;
        this.maxFuel = maxFuel;
        this.material = material;
        this.block = block;
    }

    public static void addType(String typeName, double cookTimeFactor, int upgradeCount, double fuelBooster, int maxFuel, ItemStack block, ItemStack material)
    {
        FurnaceTypes type = new FurnaceTypes(typeName, cookTimeFactor, upgradeCount, fuelBooster, maxFuel,block,material);
        allTypes.add(type);
    }

    public static FurnaceTypes getTypeByName(String name)
    {
        for (FurnaceTypes type : allTypes)
        {
            if (type.typeName.equals(name))
            {
                return type;
            }
        }
        return null;
    }

    public static void registerRecipes()
    {
        for (FurnaceTypes type : allTypes)
        {
            type.registerRecipe();
        }
    }

    public String getTypeName()
    {
        return typeName;
    }

    public double getCookTimeFactor()
    {
        return cookTimeFactor;
    }

    public double getFuelBooster()
    {
        return fuelBooster;
    }

    public int getUpgradeCount()
    {
        return upgradeCount;
    }

    public int getMaxFuel()
    {
        return maxFuel;
    }

    public void registerRecipe()
    {
        GameRegistry.addRecipe(block," m "," f "," m ",'f', Blocks.furnace,'m',material);
    }
}
