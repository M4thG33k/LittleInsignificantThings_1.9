package com.m4thg33k.lit.blocks;

import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.lib.Names;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CharcoalBlock extends BaseBlock implements IFuelHandler{

    public CharcoalBlock()
    {
        super(Names.CHARCOAL_BLOCK,Material.rock,1.0f,2.0f);
        this.setDefaultState(blockState.getBaseState());
        GameRegistry.registerFuelHandler(this);
    }

    @Override
    public void handleRegName() {
        this.setRegistryName(LIT.MODID,Names.CHARCOAL_BLOCK);
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        return fuel.getItemDamage() == 0 ? 16000 : 0;
    }
}
