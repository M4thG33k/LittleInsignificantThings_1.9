package com.m4thg33k.lit.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import com.m4thg33k.lit.core.util.LogHelper;
import com.m4thg33k.lit.lib.Names;
import com.m4thg33k.lit.network.packets.LITPackets;
import com.m4thg33k.lit.network.packets.PacketNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class TileSolidGenerator extends TileEntity implements IEnergyProvider,ITickable,ISidedInventory {

    protected EnergyStorage storage = new EnergyStorage(500000);
    protected ItemStack[] inventory = new ItemStack[1];
    protected String customName = "Solid-Fueled Generator";
    protected boolean isOn;
    protected int burnTime;
    protected int fullBurnTime;

    protected int ENERGY_PUSH = 1000000;
    protected int RF_PER_TICK = 40;

    protected EnumFacing facing = EnumFacing.NORTH;

    public TileSolidGenerator()
    {

    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        storage.readFromNBT(compound);

        NBTTagList list= compound.getTagList("Items",10);
        this.inventory = new ItemStack[getSizeInventory()];
        for (int i=0;i<list.tagCount();i++)
        {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot")&0xff;
            if (slot>=0 && slot<inventory.length)
            {
                inventory[slot] = ItemStack.loadItemStackFromNBT(stackTag);
            }
        }

        isOn = compound.getBoolean("IsOn");
        burnTime = compound.getInteger("BurnTime");
        fullBurnTime = compound.getInteger("FullBurnTime");

        if (compound.hasKey("Facing"))
        {
            facing = EnumFacing.values()[compound.getInteger("Facing")];
        }

        if (compound.hasKey("CustomName")){
            this.customName = compound.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        storage.writeToNBT(compound);
        NBTTagList list = new NBTTagList();
        for (int i=0;i<inventory.length;i++)
        {
            if (inventory[i]!=null)
            {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot",(byte)i);
                inventory[i].writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        compound.setTag("Items",list);

        compound.setInteger("BurnTime",burnTime);
        compound.setInteger("FullBurnTime",fullBurnTime);
        compound.setBoolean("IsOn",isOn);

        compound.setInteger("Facing",facing.ordinal());

        if (this.hasCustomName())
        {
            compound.setString("CustomName",customName);
        }
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return storage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }

    protected void generateEnergy()
    {
        storage.receiveEnergy(RF_PER_TICK,false);
    }

    protected int attemptEnergyPush()
    {
        int total = 0;
        for (EnumFacing facing : EnumFacing.values())
        {
            TileEntity tileEntity = worldObj.getTileEntity(pos.add(facing.getDirectionVec()));
            if (tileEntity!=null && (tileEntity instanceof IEnergyReceiver))
            {
                IEnergyReceiver te = (IEnergyReceiver)tileEntity;
                int attempt = te.receiveEnergy(facing.getOpposite(),storage.extractEnergy(ENERGY_PUSH,true),true);
                if (attempt>0)
                {
                    total += te.receiveEnergy(facing.getOpposite(),storage.extractEnergy(ENERGY_PUSH,false),false);
                }
            }
        }
        return total;
    }

    public boolean isBurning()
    {
        if (burnTime<0)
        {
            burnTime = 0;
        }
        return burnTime>0;
    }

    public void consumeFuel()
    {
        if (inventory[0]==null || !isItemFuel(inventory[0]))
        {
            return;
        }

        if (!isBurning())
        {
            burnTime = getItemBurnTime(getStackInSlot(0));
            fullBurnTime = getItemBurnTime(getStackInSlot(0));
            inventory[0].stackSize--;
            if (inventory[0].stackSize==0)
            {
                if (inventory[0].getItem().hasContainerItem(inventory[0]))
                {
                    setInventorySlotContents(0,inventory[0].getItem().getContainerItem(inventory[0]));
                }
                else
                {
                    setInventorySlotContents(0,null);
                }
            }
        }
    }

    @Override
    public void update() {
        boolean on = isBurning();
        boolean isDirty = false;

        if (isBurning())
        {
            burnTime-=2;
        }

        if (!worldObj.isRemote)
        {
            if (!isBurning())
            {
                consumeFuel();
//                isDirty = true;
            }
            if (isBurning())
            {
                generateEnergy();
                isDirty = true;
            }

            if (on != isBurning())
            {
                isDirty = true;
                isOn = isBurning();
            }

            if (attemptEnergyPush()>0)
            {
                isDirty = true;
            }


        }

        if (isDirty)
        {
            markDirty();
            NBTTagCompound tag = new NBTTagCompound();
            writeToNBT(tag);
            LITPackets.INSTANCE.sendToAllAround(new PacketNBT(pos,tag),new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(),pos.getX(),pos.getY(),pos.getZ(),32));
//            LogHelper.info("Sending update");
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        int[] toReturn = new int[1];
        toReturn[0] = 0;
        return toReturn;
    }

    public static int getItemBurnTime(ItemStack stack)
    {
        if (stack==null)
        {
            return 0;
        }

        return TileEntityFurnace.getItemBurnTime(stack);
    }

    public boolean isItemFuel(ItemStack stack)
    {
        return getItemBurnTime(stack)>0;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return isItemFuel(itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index==0 ? inventory[0] : null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.getStackInSlot(index)!=null)
        {
            ItemStack stack;
            if (getStackInSlot(index).stackSize<=count)
            {
                stack = getStackInSlot(index);
                setInventorySlotContents(index,null);
                markDirty();
                return stack;
            }
            stack = this.getStackInSlot(index).splitStack(count);

            if (getStackInSlot(index).stackSize<=0)
            {
                setInventorySlotContents(index,null);
            }
            else
            {
                setInventorySlotContents(index,getStackInSlot(index));
            }

            markDirty();
            return stack;
        }

        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index!=0)
        {
            return null;
        }

        ItemStack stack = getStackInSlot(index);
        inventory[index] = null;
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {

        boolean flag = stack!=null && stack.isItemEqual(inventory[0]) && ItemStack.areItemStackTagsEqual(stack,inventory[index]);
        inventory[index] = stack;

        if (stack!=null && stack.stackSize>getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }

    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(pos)==this && player.getDistanceSq(pos.add(0.5,0.5,0.5))<=64;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index==0 && isItemFuel(stack);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        setInventorySlotContents(0,null);
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container."+ Names.SOLID_GENERATOR;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName!=null && !this.customName.equals("");
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }

    public double getEnergyPercentage()
    {
        return (double)((int)((((double)storage.getEnergyStored())/((double)storage.getMaxEnergyStored()))*10000))*.0001;
    }

    public int getFullBurnTime()
    {
        return fullBurnTime;
    }

    public double getBurnPercentage()
    {
        int diff = fullBurnTime-burnTime;
        if (diff==0 || diff==fullBurnTime)
        {
            return 0;
        }

        return (0.0001*(int)(10000*(((double)diff)/((double)fullBurnTime))));

    }

    public void setFacing(EnumFacing face)
    {
        this.facing = face;
    }

    public EnumFacing getFacing()
    {
        return facing;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public Packet<?> getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeToNBT(tagCompound);
        return new SPacketUpdateTileEntity(pos,1,tagCompound);
    }
}
