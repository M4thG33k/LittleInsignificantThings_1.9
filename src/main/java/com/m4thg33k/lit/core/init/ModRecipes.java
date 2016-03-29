package com.m4thg33k.lit.core.init;

import com.m4thg33k.lit.api.chest.ChestTypes;
import com.m4thg33k.lit.api.furnace.FurnaceTypes;

public class ModRecipes {

    public static void initRecipes()
    {
        FurnaceTypes.registerRecipes();
        ChestTypes.regRecipes();
    }
}
