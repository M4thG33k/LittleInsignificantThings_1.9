package com.m4thg33k.lit.api.furnace;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

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
    private Object material;
    private boolean overrideFurnace;
    private Object[] alternateCenter;

    public FurnaceTypes(String typeName, double cookTimeFactor, int upgradeCount, double fuelBooster, int maxFuel, ItemStack block, Object material,boolean overrideFurnace,Object... alternateCenter)
    {
        this.typeName = typeName;
        this.cookTimeFactor = cookTimeFactor;
        this.upgradeCount = upgradeCount;
        this.fuelBooster = fuelBooster;
        this.maxFuel = maxFuel;
        this.material = material;
        this.block = block;
        this.overrideFurnace = overrideFurnace;
        this.alternateCenter = new Object[alternateCenter.length];
        System.arraycopy(alternateCenter,0,this.alternateCenter,0,alternateCenter.length);
    }

    public static void addType(String typeName, double cookTimeFactor, int upgradeCount, double fuelBooster, int maxFuel, ItemStack block, Object material,boolean overrideFurnace, Object... alternateCenter)
    {
        FurnaceTypes type = new FurnaceTypes(typeName, cookTimeFactor, upgradeCount, fuelBooster, maxFuel,block,material,overrideFurnace,alternateCenter);
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
        if (!overrideFurnace)
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(block,"m","f","m",'f', Blocks.furnace,'m',material));
        }
        for (Object center : alternateCenter)
        {
            GameRegistry.addRecipe(new ShapedOreRecipe(block,"m","f","m",'f',center,'m',material));
        }
    }
}
