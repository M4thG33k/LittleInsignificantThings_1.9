package com.m4thg33k.lit.network.packets;

import com.m4thg33k.lit.LIT;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class LITPackets {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(LIT.MODID);

    public static void init()
    {
        INSTANCE.registerMessage(PacketHandlerNBT.class,PacketNBT.class,0, Side.CLIENT);
    }
}
