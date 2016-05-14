package com.m4thg33k.lit;

import com.m4thg33k.lit.blocks.ModBlocks;
import com.m4thg33k.lit.core.proxy.CommonProxy;
import com.m4thg33k.lit.core.util.LogHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = LIT.MODID,name = LIT.MODNAME,version = LIT.VERSION)
public class LIT {

    public static final String MODID = "lit";
    public static final String VERSION = "@VERSION@";
    public static final String MODNAME = "LIT";

    public static boolean isBaublesInstalled = false;

    @Mod.Instance
    public static LIT instance = new LIT();

    @SidedProxy(clientSide = "com.m4thg33k.lit.core.proxy.ClientProxy",serverSide = "com.m4thg33k.lit.core.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e)
    {
        proxy.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e)
    {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e)
    {
        proxy.postInit(e);
        isBaublesInstalled = Loader.isModLoaded("Baubles");
    }

    public static CreativeTabs tabLIT = new CreativeTabs("tabLIT") {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(ModBlocks.improvedFurnaceBlock);
        }
    };

}
