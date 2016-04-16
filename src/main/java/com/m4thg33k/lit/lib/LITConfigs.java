package com.m4thg33k.lit.lib;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class LITConfigs {

    public static Configuration config;
    public static boolean USE_ALTERNATE_CRAFTING_TABLE_RECIPE;


    public static void preInit(FMLPreInitializationEvent event)
    {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config .load();

        Property useAlternateCraftingTableRecipe = config.get(Configuration.CATEGORY_GENERAL,"useAlternateCraftingTableRecipe",true);
        useAlternateCraftingTableRecipe.setComment("Set to true to disable the recipe requiring just the vanilla table and replace it with one that requires flint (defaults to true to avoid conflict with TCon");
        USE_ALTERNATE_CRAFTING_TABLE_RECIPE = useAlternateCraftingTableRecipe.getBoolean(true);


        config.save();
    }
}
