package com.m4thg33k.lit.blocks;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.core.util.ChatHelper;
import com.m4thg33k.lit.core.util.LogHelper;
import com.m4thg33k.lit.lib.Names;
import com.m4thg33k.lit.tiles.TileDeathBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockDeath extends BaseBlock {

    public BlockDeath()
    {
        super(Names.DEATH_BLOCK, Material.wood,100.0f,100.0f);
        this.setDefaultState(blockState.getBaseState());
        this.setBlockUnbreakable();
    }

    @Override
    public void handleRegName() {
        this.setRegistryName(LIT.MODID,Names.DEATH_BLOCK);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileDeathBlock();
    }



    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote)
        {
            ChatHelper.sayMessage(worldIn,playerIn,"This grave belongs to: " + ((TileDeathBlock)worldIn.getTileEntity(pos)).getPlayerName());
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
//        if (worldIn.isRemote)
//        {
//            return true;
//        }
////        if (playerIn.isSneaking())
////        {
////            ChatHelper.sayMessage(worldIn,playerIn,"Your name is: " + playerIn.getName());
////        }
////        else {
//            TileDeathBlock tileDeathBlock = (TileDeathBlock) worldIn.getTileEntity(pos);
//            tileDeathBlock.toggleName(playerIn);
//            ChatHelper.sayMessage(worldIn, playerIn, "Name set to: " + tileDeathBlock.getPlayerName());
////        }
//        return true;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB p_185477_4_, List<AxisAlignedBB> p_185477_5_, Entity entity) {
        if (worldIn.isRemote)
        {
            return;
        }
        TileDeathBlock tileDeathBlock = (TileDeathBlock)worldIn.getTileEntity(pos);
//        LogHelper.info("Same player?: " + (entity instanceof EntityPlayer ? (tileDeathBlock.isSamePlayer((EntityPlayer)entity)): "False"));
        if (entity instanceof  EntityPlayer && tileDeathBlock.isSamePlayer((EntityPlayer)entity))
        {
            super.addCollisionBoxToList(state, worldIn, pos, p_185477_4_, p_185477_5_, entity);
//            LogHelper.info("Adding collision box");
        }
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return super.getSelectedBoundingBox(blockState, worldIn, pos);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
//        LogHelper.info("Block:: Colliding with: " + entityIn.getName());
        TileDeathBlock tileDeathBlock = (TileDeathBlock)worldIn.getTileEntity(pos);
        if (entityIn!=null && entityIn instanceof EntityPlayer && entityIn.isEntityAlive())
        {
            tileDeathBlock.onCollision((EntityPlayer)entityIn);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileDeathBlock tileDeathBlock = (TileDeathBlock)worldIn.getTileEntity(pos);
        tileDeathBlock.dropAllItems();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.25f,0.25f,0.25f,0.75f,0.75f,0.75f);
    }

    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer) {
        return false;
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return false;
    }
}
