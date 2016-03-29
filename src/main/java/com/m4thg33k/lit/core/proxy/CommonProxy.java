package com.m4thg33k.lit.core.proxy;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.blocks.ModBlocks;
import com.m4thg33k.lit.core.init.InitChestTypes;
import com.m4thg33k.lit.core.init.InitFurnaceTypes;
import com.m4thg33k.lit.core.init.ModRecipes;
import com.m4thg33k.lit.gui.LitGuiHandler;
import com.m4thg33k.lit.network.packets.LITPackets;
import com.m4thg33k.lit.network.packets.PacketNBT;
import com.m4thg33k.lit.tiles.ModTiles;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e)
    {
        LITPackets.init();
        ModBlocks.createBlocks();
        InitFurnaceTypes.createFurnaceTypes();
        InitChestTypes.createChestTypes();
    }

    public void init(FMLInitializationEvent e)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(LIT.instance,new LitGuiHandler());
        ModTiles.init();
        ModRecipes.initRecipes();
    }

    public void postInit(FMLPostInitializationEvent e)
    {

    }

    public void handleNBTPacket(PacketNBT pkt)
    {

    }
}
