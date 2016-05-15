package com.m4thg33k.lit.items;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.core.util.ChatHelper;
import com.m4thg33k.lit.lib.Constants;
import com.m4thg33k.lit.lib.Names;
import com.m4thg33k.lit.tiles.TileImprovedFurnace;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemFurnaceUpgrade extends Item {

    public ItemFurnaceUpgrade()
    {
        super();

        this.setCreativeTab(LIT.tabLIT);
        this.setUnlocalizedName(Names.FURNACE_UPGRADE);
        this.setHasSubtypes(true);

        this.setMaxStackSize(64);
        this.setRegistryName(LIT.MODID,Names.FURNACE_UPGRADE);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileImprovedFurnace te = (TileImprovedFurnace)worldIn.getTileEntity(pos);

        if (te.canReceiveUpgrade())
        {
            te.installUpgrade(playerIn,stack);
            stack.stackSize--;
            if (stack.stackSize==0)
            {
                stack = null;
            }
            return EnumActionResult.SUCCESS;
        }
        else
        {
            ChatHelper.sayMessage(worldIn,playerIn,"No room for upgrades!");
            return EnumActionResult.FAIL;
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "_" + stack.getItemDamage();
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (int i=0;i<3;i++)
        {
            subItems.add(new ItemStack(itemIn,1,i));
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> list, boolean advanced) {
        super.addInformation(stack, playerIn, list, advanced);

        switch (stack.getItemDamage())
        {
            case 0:
                list.add("Speeds up the furnace");
                list.add("by a factor of " + Constants.SPEED_MULT);
                break;
            case 1:
                list.add("Increases the fuel efficiency");
                list.add("of the furnace by a factor of " + Constants.FUEL_MULT);
                break;
            case 2:
                list.add("Increases the storage capacity");
                list.add("of the furnace by a factor of " + Constants.CAP_MULT);
                break;
            default:
        }
        list.add("----------------");
        list.add(TextFormatting.ITALIC + "Sneak-use on a furnace to install");
    }
}
