package com.m4thg33k.lit.blocks;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.api.LitStateProps;
import com.m4thg33k.lit.core.util.LogHelper;
import com.m4thg33k.lit.gui.LitGuiHandler;
import com.m4thg33k.lit.lib.Names;
import com.m4thg33k.lit.tiles.TileSolidGenerator;
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

public class SolidGeneratorBlock extends BaseBlock {

    public static final PropertyBool ON = PropertyBool.create("on");

    public SolidGeneratorBlock()
    {
        super(Names.SOLID_GENERATOR, Material.rock, 2.0f,5.0f);

        this.setDefaultState(this.blockState.getBaseState().withProperty(LitStateProps.CARDINALS,EnumFacing.NORTH).withProperty(ON,false));
    }

    @Override
    public void handleRegName() {
        this.setRegistryName(LIT.MODID, Names.SOLID_GENERATOR);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this,LitStateProps.CARDINALS,ON);
    }

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
        TileSolidGenerator tileSolidGenerator = (TileSolidGenerator)worldIn.getTileEntity(pos);
        return state.withProperty(ON,tileSolidGenerator.isBurning()).withProperty(LitStateProps.CARDINALS,tileSolidGenerator.getFacing());
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileSolidGenerator();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote)
        {
            playerIn.openGui(LIT.instance, LitGuiHandler.SOLID_GENERATOR_GUI,worldIn,pos.getX(),pos.getY(),pos.getZ());
        }

        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileSolidGenerator tile = (TileSolidGenerator)worldIn.getTileEntity(pos);
        tile.setFacing(placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileSolidGenerator tile = (TileSolidGenerator)worldIn.getTileEntity(pos);

        InventoryHelper.dropInventoryItems(worldIn,pos,tile);

        super.breakBlock(worldIn, pos, state);
    }
}
