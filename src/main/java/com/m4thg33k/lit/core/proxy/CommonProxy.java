package com.m4thg33k.lit.core.proxy;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.blocks.ItemBlocks.ModItemBlocks;
import com.m4thg33k.lit.blocks.ModBlocks;
import com.m4thg33k.lit.core.event.LITCommonEvents;
import com.m4thg33k.lit.core.init.InitChestTypes;
import com.m4thg33k.lit.core.init.InitFurnaceTypes;
import com.m4thg33k.lit.core.init.ModRecipes;
import com.m4thg33k.lit.core.init.OreDictionaryReg;
import com.m4thg33k.lit.gui.LitGuiHandler;
import com.m4thg33k.lit.items.ModItems;
import com.m4thg33k.lit.lib.LITConfigs;
import com.m4thg33k.lit.network.packets.LITPackets;
import com.m4thg33k.lit.network.packets.PacketNBT;
import com.m4thg33k.lit.tiles.ModTiles;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e)
    {
        LITConfigs.preInit(e);
        LITPackets.init();
        ModItems.createItems();
        ModBlocks.createBlocks();
        ModItemBlocks.create();
        InitFurnaceTypes.createFurnaceTypes();
        InitChestTypes.createChestTypes();
    }

    public void init(FMLInitializationEvent e)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(LIT.instance,new LitGuiHandler());
        ModTiles.init();
        ModRecipes.initRecipes();
        OreDictionaryReg.init();
        MinecraftForge.EVENT_BUS.register(new LITCommonEvents());
    }

    public void postInit(FMLPostInitializationEvent e)
    {

    }

    public void handleNBTPacket(PacketNBT pkt)
    {

    }
}
