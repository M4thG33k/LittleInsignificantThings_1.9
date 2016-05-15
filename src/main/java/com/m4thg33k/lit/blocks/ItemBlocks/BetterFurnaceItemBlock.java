package com.m4thg33k.lit.blocks.ItemBlocks;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.blocks.BetterFurnaceBlock;
import com.m4thg33k.lit.lib.EnumBetterFurnaceType;
import com.m4thg33k.lit.lib.Names;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class BetterFurnaceItemBlock extends ItemBlock{

    public BetterFurnaceItemBlock(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setCreativeTab(LIT.tabLIT);

        this.setRegistryName(LIT.MODID, Names.BETTER_FURNACE);
    }

    @Override
    public int getMetadata(int damage) {
        if (damage>0 && damage< EnumBetterFurnaceType.values().length)
        {
            return damage;
        }
        return 0;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "_" + EnumBetterFurnaceType.values()[stack.getItemDamage()].getName();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> list, boolean advanced) {
        boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

        if (shifted)
        {
            EnumBetterFurnaceType type = EnumBetterFurnaceType.values()[stack.getItemDamage()];
            String[] stats = type.getDisplayInfo();

            list.add(TextFormatting.ITALIC + "Properties");
            list.add("----------------");
            list.add(TextFormatting.GOLD + "Time Reduction Multiplier: " + TextFormatting.RESET + stats[0]);
            list.add(TextFormatting.GOLD + "Fuel Bonus Multiplier: " + TextFormatting.RESET + stats[1]);
            list.add(TextFormatting.GOLD + "Upgrade Slots: " + TextFormatting.RESET + stats[2]);
            list.add(TextFormatting.GOLD + "Fuel Capacity: " + TextFormatting.RESET + stats[3]);
        }
        else
        {
            list.add(TextFormatting.ITALIC + "<Press Shift>");
        }
    }
}
