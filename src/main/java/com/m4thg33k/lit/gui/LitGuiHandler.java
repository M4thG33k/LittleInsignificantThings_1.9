package com.m4thg33k.lit.gui;

import com.m4thg33k.lit.client.gui.GuiImprovedChest;
import com.m4thg33k.lit.client.gui.GuiImprovedCraftingTable;
import com.m4thg33k.lit.client.gui.GuiImprovedFurnace;
import com.m4thg33k.lit.client.gui.GuiSolidGenerator;
import com.m4thg33k.lit.inventory.ContainerImprovedChest;
import com.m4thg33k.lit.inventory.ContainerImprovedCraftingTable;
import com.m4thg33k.lit.inventory.ContainerImprovedFurnace;
import com.m4thg33k.lit.inventory.ContainerSolidGenerator;
import com.m4thg33k.lit.tiles.TileImprovedChest;
import com.m4thg33k.lit.tiles.TileImprovedCraftingTable;
import com.m4thg33k.lit.tiles.TileImprovedFurnace;
import com.m4thg33k.lit.tiles.TileSolidGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class LitGuiHandler implements IGuiHandler{

    public static final int IMPROVED_FURNACE_GUI = 0;
    public static final int IMPROVED_CHEST_GUI = 1;
    public static final int IMPROVED_CRAFTING_TABLE = 2;
    public static final int SOLID_GENERATOR_GUI = 3;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID)
        {
            case 0:
                return new ContainerImprovedFurnace(player.inventory,(TileImprovedFurnace)world.getTileEntity(new BlockPos(x,y,z)));
            case 1:
                return new ContainerImprovedChest(player.inventory,(TileImprovedChest)world.getTileEntity(new BlockPos(x,y,z)),((TileImprovedChest)world.getTileEntity(new BlockPos(x,y,z))).getType());
            case 2:
                return new ContainerImprovedCraftingTable(player.inventory,(TileImprovedCraftingTable)world.getTileEntity(new BlockPos(x,y,z)));
            case 3:
                return new ContainerSolidGenerator(player.inventory,(TileSolidGenerator)world.getTileEntity(new BlockPos(x,y,z)));
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID)
        {
            case 0:
                return new GuiImprovedFurnace(player.inventory,(TileImprovedFurnace)world.getTileEntity(new BlockPos(x,y,z)));
            case 1:
                return new GuiImprovedChest(((TileImprovedChest)world.getTileEntity(new BlockPos(x,y,z))).getType(),player.inventory,(TileImprovedChest)world.getTileEntity(new BlockPos(x,y,z)));
            case 2:
                return new GuiImprovedCraftingTable(player.inventory,(TileImprovedCraftingTable)world.getTileEntity(new BlockPos(x,y,z)));
            case 3:
                return new GuiSolidGenerator(player.inventory,(TileSolidGenerator)world.getTileEntity(new BlockPos(x,y,z)));
            default:
                return null;
        }
    }
}
