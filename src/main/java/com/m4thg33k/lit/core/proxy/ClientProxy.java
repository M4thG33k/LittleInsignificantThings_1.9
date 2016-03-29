package com.m4thg33k.lit.core.proxy;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.client.render.ModRenders;
import com.m4thg33k.lit.client.render.registers.BlockRenderRegister;
import com.m4thg33k.lit.client.render.registers.ItemRenderRegister;
import com.m4thg33k.lit.network.packets.PacketNBT;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        ItemRenderRegister.registerItemRenderer();
        OBJLoader.INSTANCE.addDomain(LIT.MODID);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);

        BlockRenderRegister.registerBlockRenderer();
        ModRenders.init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    @Override
    public void handleNBTPacket(PacketNBT pkt) {
        World world = Minecraft.getMinecraft().theWorld;
        Minecraft.getMinecraft().theWorld.getTileEntity(pkt.pos).readFromNBT(pkt.compound);
        Minecraft.getMinecraft().theWorld.notifyBlockUpdate(pkt.pos,world.getBlockState(pkt.pos),world.getBlockState(pkt.pos),0);
    }
}
