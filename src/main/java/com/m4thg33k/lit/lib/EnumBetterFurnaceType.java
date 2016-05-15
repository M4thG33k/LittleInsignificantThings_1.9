package com.m4thg33k.lit.lib;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public enum EnumBetterFurnaceType implements IStringSerializable {
    IRON("Iron",0.5,1,1.25,30000, new ItemStack(Items.iron_ingot,1)),
    GOLD("Gold",0.25,1,1.25,40000,new ItemStack(Items.gold_ingot,1)),
    DIAMOND("Diamond",0.25,2,1.5,80000, new ItemStack(Items.diamond,1)),
    REDSTONE("Redstone",0.125,0,1.25,30000,new ItemStack(Blocks.redstone_block,1)),
    LAPIS("Lapis",0.25,3,1.0,20000,new ItemStack(Blocks.lapis_block,1));

    protected final String name;
    protected final double speedMult;
    protected final int numUpgrades;
    protected final double fuelEfficiencyMult;
    protected final int fuelCap;
    protected final ItemStack ingredient;
    EnumBetterFurnaceType(String name,double speedMult,int numUpgrades,double fuelEfficiencyMult,int fuelCap,ItemStack ingredient)
    {
        this.name = name;
        this.speedMult = speedMult;
        this.numUpgrades = numUpgrades;
        this.fuelEfficiencyMult = fuelEfficiencyMult;
        this.fuelCap = fuelCap;
        this.ingredient = ingredient.copy();
    }




    @Override
    public String getName() {
        return this.name.toLowerCase();
    }

    public String getTypeName()
    {
        return this.name;
    }

    public double getSpeedMult() {
        return speedMult;
    }

    public int getNumUpgrades() {
        return numUpgrades;
    }

    public double getFuelEfficiencyMult() {
        return fuelEfficiencyMult;
    }

    public int getFuelCap() {
        return fuelCap;
    }

    public ItemStack getIngredient() {
        return ingredient.copy();
    }

    public String[] getDisplayInfo()
    {
        return new String[]{""+getSpeedMult(),""+getFuelEfficiencyMult(),""+getNumUpgrades(),""+(Math.floor(100*((0.0+getFuelCap())/(1600*64*getFuelEfficiencyMult())))/100)};
    }
}
