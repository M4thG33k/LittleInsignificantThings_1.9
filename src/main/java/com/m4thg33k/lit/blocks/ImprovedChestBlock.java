package com.m4thg33k.lit.blocks;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.api.LitStateProps;
import com.m4thg33k.lit.api.chest.ChestTypes;
import com.m4thg33k.lit.gui.LitGuiHandler;
import com.m4thg33k.lit.lib.Names;
import com.m4thg33k.lit.tiles.TileImprovedChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ImprovedChestBlock extends BlockContainer{

    public ImprovedChestBlock()
    {
        super(Material.rock);

        this.setDefaultState(this.blockState.getBaseState());

//        this.setBlockBounds(0.0625f,0f,0.0625f,0.9375f,0.875f,0.9375f);
        this.setHardness(3.0f);
        this.setUnlocalizedName(Names.IMPROVED_CHEST);
        this.setCreativeTab(LIT.tabLIT);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0625f,0f,0.0625f,0.9375f,0.875f,0.9375f);
//        return source.getBlockState(pos.north()).getBlock() == this ? field_185557_b : (source.getBlockState(pos.south()).getBlock() == this ? field_185558_c : (source.getBlockState(pos.west()).getBlock() == this ? field_185559_d : (source.getBlockState(pos.east()).getBlock() == this ? field_185560_e : field_185561_f)));return super.getBoundingBox(state, source, pos);
    }


    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);

        if (te==null || !(te instanceof TileImprovedChest))
        {
            return true;
        }

        if (worldIn.isSideSolid(pos.add(0,1,0),EnumFacing.DOWN))
        {
            return true;
        }
        if (worldIn.isRemote)
        {
            return true;
        }

        playerIn.openGui(LIT.instance, LitGuiHandler.IMPROVED_CHEST_GUI,worldIn,pos.getX(),pos.getY(),pos.getZ());
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileImprovedChest(ChestTypes.getTypeByName("Improved"));
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
    protected BlockStateContainer createBlockState() {
        return super.createBlockState();
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.markAndNotifyBlock(pos,null,state,state,0);
//        worldIn.markBlockForUpdate(pos);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        byte chestFacing = 0;
        int facing = MathHelper.floor_double((placer.rotationYaw*4f)/360f + 0.5d)&3;
        switch (facing)
        {
            case 0:
                chestFacing = 2;
                break;
            case 1:
                chestFacing = 5;
                break;
            case 2:
                chestFacing = 3;
                break;
            case 3:
                chestFacing = 4;
                break;
            default:
        }

        TileEntity te = worldIn.getTileEntity(pos);
        if (te!=null && te instanceof TileImprovedChest)
        {
            TileImprovedChest chest = (TileImprovedChest)te;
            chest.wasPlaced(placer,stack);
            chest.setFacing(EnumFacing.VALUES[chestFacing]);
            worldIn.markAndNotifyBlock(pos,null,state,state,0);
//        worldIn.markBlockForUpdate(pos);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileImprovedChest chest = (TileImprovedChest)worldIn.getTileEntity(pos);
        InventoryHelper.dropInventoryItems(worldIn,pos,chest);
        super.breakBlock(worldIn,pos,state);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileImprovedChest && ((TileImprovedChest) te).getType().isExplosionResistant())
        {
            return 10000f;
        }
        return super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state,World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof IInventory)
        {
            return Container.calcRedstoneFromInventory((IInventory)te);
        }
        return 0;
    }

    private static final EnumFacing[] validRotationAxes = new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN};

    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        return validRotationAxes;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        if (world.isRemote)
        {
            return false;
        }

        if (axis== EnumFacing.UP || axis== EnumFacing.DOWN)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileImprovedChest)
            {
                ((TileImprovedChest) tileEntity).rotateAround();
            }
            return true;
        }
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    //    @Override
//    public int getRenderType(IBlockState state) {
//        return 2;
//    }
}
