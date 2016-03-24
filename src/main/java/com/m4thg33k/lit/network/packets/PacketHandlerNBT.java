package com.m4thg33k.lit.network.packets;

import com.m4thg33k.lit.LIT;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerNBT implements IMessageHandler<PacketNBT,IMessage> {

    @Override
    public IMessage onMessage(PacketNBT message, MessageContext ctx) {
        LIT.proxy.handleNBTPacket(message);

        return null;
    }
}
