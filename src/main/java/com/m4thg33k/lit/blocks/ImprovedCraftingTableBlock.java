package com.m4thg33k.lit.blocks;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.gui.LitGuiHandler;
import com.m4thg33k.lit.lib.Names;
import com.m4thg33k.lit.tiles.TileImprovedCraftingTable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ImprovedCraftingTableBlock extends BaseBlock{

    public ImprovedCraftingTableBlock()
    {
        super(Names.IMPROVED_CRAFTING_TABLE);
    }

    @Override
    public void handleRegName() {
        this.setRegistryName(LIT.MODID,Names.IMPROVED_CRAFTING_TABLE);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote)
        {
            playerIn.openGui(LIT.instance, LitGuiHandler.IMPROVED_CRAFTING_TABLE,worldIn,pos.getX(),pos.getY(),pos.getZ());
        }

        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileImprovedCraftingTable();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileImprovedCraftingTable tile = (TileImprovedCraftingTable)worldIn.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(worldIn,pos,tile);
        super.breakBlock(worldIn, pos, state);
    }
}
