package com.m4thg33k.lit.core.event;

import com.m4thg33k.lit.blocks.ModBlocks;
import com.m4thg33k.lit.core.util.ChatHelper;
import com.m4thg33k.lit.core.util.LogHelper;
import com.m4thg33k.lit.lib.LITConfigs;
import com.m4thg33k.lit.tiles.TileDeathBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LITCommonEvents {

    private static final int MAX_RADIUS = LITConfigs.GRAVE_RANGE;

    public LITCommonEvents()
    {

    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onPlayerDeath(LivingDeathEvent event)
    {
//        if (LITConfigs.ENABLE_GRAVES && event.getEntityLiving() instanceof EntityPlayer)
//        {
//            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
//            if (!player.worldObj.isRemote)
//            {
//                IBlockState state = ModBlocks.blockDeath.getDefaultState();
//                BlockPos posToPlace = findValidLocation(player.worldObj,player.getPosition());
//                if (posToPlace.getY()!=-1) {
//                    ChatHelper.sayMessage(player.worldObj,player,"Place of death (x,y,z) = ("+ posToPlace.getX() + "," + posToPlace.getY() + "," + posToPlace.getZ() + ")");
//                    player.worldObj.setBlockState(posToPlace, state);
//                    TileEntity tileEntity = player.worldObj.getTileEntity(posToPlace);
//                    if (tileEntity != null && tileEntity instanceof TileDeathBlock) {
//                        ((TileDeathBlock) tileEntity).grabPlayer(player);
//                    } else {
//                        LogHelper.info("Error! Death block tile not found!");
//                    }
//                }
//                else
//                {
//                    ChatHelper.sayMessage(player.worldObj,player,"Could not find suitable grave location.");
//                }
//
//            }
//        }
    }

    private BlockPos findValidLocation(World world, BlockPos pos)
    {
        BlockPos toReturn = new BlockPos(-1,-1,-1);
        BlockPos toCheck = pos.add(0,0,0);
        if (toCheck.getY()<=0)
        {
            toCheck = toCheck.add(0,MathHelper.abs_int(toCheck.getY())+1,0);
            LogHelper.info(toCheck.toString());
        }
        for (int r=0;r<=MAX_RADIUS;r++)
        {
            toReturn = checkLevel(world,toCheck,r);
            if (toReturn.getY()!=-1)
            {
                return toReturn;
            }
        }
        return toReturn;
    }

    private BlockPos checkLevel(World world, BlockPos pos, int radius)
    {
        if (radius==0 && isValidLocation(world,pos))
        {
            return pos;
        }
        for (int i=-radius;i<=radius;i++)
        {
            for (int j=-radius;j<=radius;j++)
            {
                for (int k=radius;k>=-radius;k--)
                {
                    if (MathHelper.abs_int(i)==radius || MathHelper.abs_int(j)==radius || MathHelper.abs_int(k)==radius)
                    {
                        if (isValidLocation(world,pos.add(i,j,k)))
                        {
                            return pos.add(i,j,k);
                        }
                    }
                }
            }
        }
        return new BlockPos(-1,-1,-1);
    }

    private boolean isValidLocation(World world,BlockPos pos)
    {
        Block theBlock = world.getBlockState(pos).getBlock();
        if (world.isAirBlock(pos))
        {
            return true;
        }
        if (LITConfigs.ALLOW_GRAVES_IN_LAVA && theBlock == Blocks.lava)
        {
            return true;
        }
        if (LITConfigs.ALLOW_GRAVES_IN_FLOWING_LAVA && theBlock == Blocks.flowing_lava)
        {
            return true;
        }
        if (LITConfigs.ALLOW_GRAVES_IN_WATER && theBlock == Blocks.water)
        {
            return true;
        }
        if (LITConfigs.ALLOW_GRAVES_IN_FLOWING_WATER && theBlock == Blocks.flowing_water)
        {
            return true;
        }
        return false;
    }

}
