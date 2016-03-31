package com.m4thg33k.lit.api.chest;

import com.m4thg33k.lit.core.util.LogHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

public class ChestTypes {

    public static final ArrayList<ChestTypes> allTypes = new ArrayList<ChestTypes>();

    private String typeName;
    private int size;
    private boolean isExpResistant;
    private ResourceLocation guiLocation;
    private int width;
    private int height;
    private int rowCount;
    private int rowLength;
    private ResourceLocation modelTexture;
    private ItemStack material;
    private ItemStack block;
    private boolean useCheapRecipe;
    private boolean overrideCenter;
    private ItemStack[] alternateCenters;


    public ChestTypes(String typeName, int size, boolean isExpResistant,ResourceLocation guiLocation, int width, int height,int rowCount,int rowLength,ResourceLocation modelTexture,ItemStack material,ItemStack block,boolean useCheapRecipe,boolean overrideCenter, Object... alternateCenters)
    {
        this.typeName = typeName;
        this.size = size;
        this.isExpResistant = isExpResistant;
        this.guiLocation = guiLocation;
        this.width = width;
        this.height = height;
        this.rowCount = rowCount;
        this.rowLength = rowLength;
        this.modelTexture = modelTexture;
        this.material = material.copy();
        this.block = block.copy();
        this.useCheapRecipe = useCheapRecipe;
        this.overrideCenter = overrideCenter;
        this.alternateCenters = new ItemStack[alternateCenters.length];
        for (int i=0;i<alternateCenters.length;i++)
        {
            this.alternateCenters[i] = ((ItemStack)alternateCenters[i]).copy();
        }
    }

    public static void addType(String typeName,int size,boolean isExpResistant,ResourceLocation guiLocation,int width,int height,int rowCount,int rowLength,ResourceLocation modelTexture,ItemStack material,ItemStack block,boolean useCheapRecipe,boolean overrideCenter,Object... alternateCenters)
    {
        if (ChestTypes.getTypeByName(typeName)!=null)
        {
            LogHelper.error("Error! Chest type: " + typeName + " has already been registered! Skipping!");
            return;
        }
        ChestTypes type = new ChestTypes(typeName,size,isExpResistant,guiLocation,width,height,rowCount,rowLength,modelTexture,material,block,useCheapRecipe,overrideCenter,alternateCenters);
        allTypes.add(type);
    }

    public static ChestTypes getTypeByName(String name)
    {
        for (ChestTypes type : allTypes)
        {
            if (type.typeName.equals(name))
            {
                return type;
            }
        }
        return null;
    }
    public String getTypeName()
    {
        return typeName;
    }

    public int getSize()
    {
        return size;
    }

    public boolean isExplosionResistant()
    {
        return isExpResistant;
    }

    public ResourceLocation getGuiLocation()
    {
        return guiLocation;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getRowCount()
    {
        return rowCount;
    }

    public int getRowLength()
    {
        return rowLength;
    }

    public ResourceLocation getModelTexture()
    {
        return modelTexture;
    }

    public ItemStack getMaterial()
    {
        return material;
    }

    public ItemStack getBlock()
    {
        return block;
    }

    public boolean isUseCheapRecipe()
    {
        return useCheapRecipe;
    }

    private void regRecipe(boolean cheap)
    {
        if (cheap) {
            if (!overrideCenter) {
                GameRegistry.addRecipe(this.getBlock(), " m ", " c ", " m ", 'm', this.getMaterial(), 'c', Blocks.chest);
            }
            for (ItemStack center : alternateCenters)
            {
                GameRegistry.addRecipe(this.getBlock(), " m ", " c ", " m ", 'm', this.getMaterial(), 'c', center);
            }
        }

        else {
            if (!overrideCenter)
            {
                GameRegistry.addRecipe(this.getBlock(), "mmm", "mcm", "mmm", 'm', this.getMaterial(), 'c', Blocks.chest);
            }
            for (ItemStack center : alternateCenters)
            {
                GameRegistry.addRecipe(this.getBlock(), "mmm", "mcm", "mmm", 'm', this.getMaterial(), 'c', center);
            }
        }
    }

    public static void regRecipes()
    {
        for (ChestTypes type: allTypes)
        {
            type.regRecipe(type.isUseCheapRecipe());
        }
    }
}
