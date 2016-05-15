package com.m4thg33k.lit.blocks;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.api.LitStateProps;
import com.m4thg33k.lit.api.furnace.FurnaceTypes;
import com.m4thg33k.lit.lib.EnumBetterFurnaceType;
import com.m4thg33k.lit.lib.Names;
import com.m4thg33k.lit.tiles.TileImprovedFurnace;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

public class BetterFurnaceBlock extends ImprovedFurnaceBlock{

    public static final PropertyEnum<EnumBetterFurnaceType> VARIANT = PropertyEnum.create("variant",EnumBetterFurnaceType.class, EnumSet.range(EnumBetterFurnaceType.IRON,EnumBetterFurnaceType.LAPIS));

    public BetterFurnaceBlock()
    {
        super();

        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT,EnumBetterFurnaceType.IRON).withProperty(LitStateProps.CARDINALS, EnumFacing.NORTH).withProperty(ON,false));
        this.setUnlocalizedName(Names.BETTER_FURNACE);
        this.setCreativeTab(LIT.tabLIT);
    }

    @Override
    public void handleRegName() {
        this.setRegistryName(LIT.MODID,Names.BETTER_FURNACE);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this,LitStateProps.CARDINALS,ON,VARIANT);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (int i=0;i<EnumBetterFurnaceType.values().length;i++)
        {
            list.add(new ItemStack(itemIn,1,i));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT,EnumBetterFurnaceType.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(VARIANT)).ordinal();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileImprovedFurnace tileImprovedFurnace = (TileImprovedFurnace)worldIn.getTileEntity(pos);
        return state.withProperty(ON,tileImprovedFurnace.getOn()).withProperty(LitStateProps.CARDINALS,tileImprovedFurnace.getFacing());
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileImprovedFurnace(FurnaceTypes.getTypeByName(state.getValue(VARIANT).getName()));
    }
}
