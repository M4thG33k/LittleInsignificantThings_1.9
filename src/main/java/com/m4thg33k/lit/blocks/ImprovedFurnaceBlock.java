package com.m4thg33k.lit.blocks;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.api.LitStateProps;
import com.m4thg33k.lit.gui.LitGuiHandler;
import com.m4thg33k.lit.lib.Names;
import com.m4thg33k.lit.tiles.TileImprovedFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ImprovedFurnaceBlock extends BaseBlock {

    public static final PropertyBool ON = PropertyBool.create("on");

    public ImprovedFurnaceBlock()
    {
        super(Names.IMPROVED_FURNACE, Material.rock,2.0f,5.0f);

        this.setDefaultState(this.blockState.getBaseState().withProperty(LitStateProps.CARDINALS, EnumFacing.NORTH).withProperty(ON,false));

        handleRegName();
    }

    protected void handleRegName()
    {
        this.setRegistryName(LIT.MODID,Names.IMPROVED_FURNACE);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this,LitStateProps.CARDINALS,ON);
    }


    //    protected BlockState createBlockState() {
//        return new BlockState(this,LitStateProps.CARDINALS,ON);
//    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileImprovedFurnace tileEntity = (TileImprovedFurnace)worldIn.getTileEntity(pos);
        return state.withProperty(ON,(tileEntity).getOn()).withProperty(LitStateProps.CARDINALS,(tileEntity.getFacing()));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileImprovedFurnace tileEntity = (TileImprovedFurnace)worldIn.getTileEntity(pos);
        tileEntity.setFacing(placer.getHorizontalFacing().getOpposite());
        if (stack.hasDisplayName())
        {
            tileEntity.setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileImprovedFurnace();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,  ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote)
        {
            playerIn.openGui(LIT.instance, LitGuiHandler.IMPROVED_FURNACE_GUI, worldIn,pos.getX(),pos.getY(),pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileImprovedFurnace te = (TileImprovedFurnace)worldIn.getTileEntity(pos);

        InventoryHelper.dropInventoryItems(worldIn,pos,te);
        te.dropUpgrades(worldIn,pos);
        super.breakBlock(worldIn, pos, state);
    }
}
