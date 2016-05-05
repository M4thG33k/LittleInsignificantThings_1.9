package com.m4thg33k.lit.api.chest;

import com.m4thg33k.lit.core.crafting.ChestRecipe;
import com.m4thg33k.lit.core.util.LogHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

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
    private Object material;
    private ItemStack block;
    private boolean useCheapRecipe;
    private boolean overrideCenter;
    private Object[] alternateCenters;


    public ChestTypes(String typeName, int size, boolean isExpResistant,ResourceLocation guiLocation, int width, int height,int rowCount,int rowLength,ResourceLocation modelTexture,Object material,ItemStack block,boolean useCheapRecipe,boolean overrideCenter,Object... alternateCenters)
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
        this.material = material;
        this.block = block.copy();
        this.useCheapRecipe = useCheapRecipe;
        this.overrideCenter = overrideCenter;
        this.alternateCenters = new Object[alternateCenters.length];
        System.arraycopy(alternateCenters,0,this.alternateCenters,0,alternateCenters.length);
    }

    public static void addType(String typeName,int size,boolean isExpResistant,ResourceLocation guiLocation,int width,int height,int rowCount,int rowLength,ResourceLocation modelTexture,Object material,ItemStack block,boolean useCheapRecipe,boolean overrideCenter,Object... alternateCenters)
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

    public ItemStack getBlock()
    {
        return block;
    }

    private void regRecipe()
    {
        if (this.useCheapRecipe) {
            if (!overrideCenter) {
                GameRegistry.addRecipe(new ChestRecipe(this.getBlock(), "m", "c", "m", 'm', material, 'c', "chestWood"));// Blocks.chest);
            }
            for (Object center : alternateCenters)
            {
                GameRegistry.addRecipe(new ChestRecipe(this.getBlock(), "m", "c", "m", 'm', material, 'c', center));
            }
        }

        else {
            if (!overrideCenter)
            {
                GameRegistry.addRecipe(new ChestRecipe(this.getBlock(), "mmm", "mcm", "mmm", 'm', material, 'c', "chestWood"));// Blocks.chest);
            }
            for (Object center : alternateCenters)
            {
                GameRegistry.addRecipe(new ChestRecipe(this.getBlock(), "mmm", "mcm", "mmm", 'm', material, 'c', center));
            }
        }
//        if (this.useCheapRecipe) {
//            if (!overrideCenter) {
//                GameRegistry.addRecipe(new ShapedOreRecipe(this.getBlock(), "m", "c", "m", 'm', material, 'c', "chestWood"));// Blocks.chest);
//            }
//            for (Object center : alternateCenters)
//            {
//                GameRegistry.addRecipe(new ShapedOreRecipe(this.getBlock(), "m", "c", "m", 'm', material, 'c', center));
//            }
//        }
//
//        else {
//            if (!overrideCenter)
//            {
//                GameRegistry.addRecipe(new ShapedOreRecipe(this.getBlock(), "mmm", "mcm", "mmm", 'm', material, 'c', "chestWood"));// Blocks.chest);
//            }
//            for (Object center : alternateCenters)
//            {
//                GameRegistry.addRecipe(new ShapedOreRecipe(this.getBlock(), "mmm", "mcm", "mmm", 'm', material, 'c', center));
//            }
//        }
    }

    public static void regRecipes()
    {
        for (ChestTypes type: allTypes)
        {
            type.regRecipe();
        }
    }
}
