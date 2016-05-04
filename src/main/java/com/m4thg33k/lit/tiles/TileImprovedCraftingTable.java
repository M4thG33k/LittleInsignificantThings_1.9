package com.m4thg33k.lit.tiles;

import com.m4thg33k.lit.blocks.ModBlocks;
import com.m4thg33k.lit.core.util.LogHelper;
import com.m4thg33k.lit.inventory.ContainerImprovedCraftingTable;
import com.m4thg33k.lit.inventory.LITInventoryCrafting;
import com.m4thg33k.lit.lib.IHasResult;
import com.m4thg33k.lit.lib.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

public class TileImprovedCraftingTable extends TileEntity implements ITickable,IInventory,ISidedInventory,IHasResult{

    private ItemStack[] craftingGrid = new ItemStack[9];

    private ItemStack result;

    protected int ticksSinceSync = -1;

    protected int numUsingPlayers;
    protected boolean inventoryTouched;
    protected String customName;

    protected EnumFacing facing = EnumFacing.NORTH;

    public TileImprovedCraftingTable()
    {
        super();
    }

    @Override
    public int getSizeInventory() {
        return 9;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index<0 || index>=9)
        {
            return null;
        }
        inventoryTouched = true;
        return craftingGrid[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (getStackInSlot(index)!=null)
        {
            if (craftingGrid[index].stackSize <= count)
            {
                ItemStack itemStack = craftingGrid[index];
                craftingGrid[index] = null;
                markDirty();
                return itemStack;
            }
            ItemStack itemStack = craftingGrid[index].splitStack(count);
            if (craftingGrid[index].stackSize==0)
            {
                craftingGrid[index] = null;
            }
            markDirty();
            return itemStack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (getStackInSlot(index)!=null)
        {
            ItemStack stack = craftingGrid[index];
            craftingGrid[index] = null;
            return stack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
//        LogHelper.info("Setting contents in " + index + " on " + (worldObj.isRemote ? "client " : "server ") + "side!");
        if (index<0 || index>=9)
        {
            return;
        }
        craftingGrid[index] = stack;
        if (stack!=null && stack.stackSize<=0)
        {
            craftingGrid[index] = null;
        }
        if (stack!=null && stack.stackSize>getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }
        syncInventories();
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        if (worldObj==null)
        {
            return false;
        }
        if (worldObj.getTileEntity(pos)!=this)
        {
            return false;
        }
        return player.getDistanceSq(pos.add(0.5,0.5,0.5))<=64;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (worldObj==null)
        {
            return;
        }
        numUsingPlayers++;
        worldObj.addBlockEvent(pos, ModBlocks.improvedCraftingTableBlock,1,numUsingPlayers);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (worldObj==null)
        {
            return;
        }
        numUsingPlayers--;
        worldObj.addBlockEvent(pos,ModBlocks.improvedCraftingTableBlock,1,numUsingPlayers);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
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
        for (int i=0;i<this.craftingGrid.length;i++)
        {
            this.craftingGrid[i] = null;
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container."+Names.IMPROVED_CRAFTING_TABLE;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName!=null && !this.customName.equals("");
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }

    @Override
    public void update() {
//        LogHelper.info(result==null?"<EMPTY>":result.getDisplayName());
        //resync clients with the server state
        if (worldObj!=null && !this.worldObj.isRemote && this.numUsingPlayers!=0 && (this.ticksSinceSync+pos.getX()+pos.getY()+pos.getZ())%200==0)
        {
            this.numUsingPlayers = 0;
            float var1 = 5.0f;
            List<EntityPlayer> var2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.getX()-var1,pos.getY()-var1,pos.getZ()-var1,pos.getX()+1+var1,pos.getY()+1+var1,pos.getZ()+1+var1));

            for (EntityPlayer player : var2)
            {
                if (player.openContainer instanceof ContainerImprovedCraftingTable)
                {
                    ++this.numUsingPlayers;
                }
            }
        }

        if (worldObj!=null && !worldObj.isRemote && ticksSinceSync<0)
        {
            worldObj.addBlockEvent(pos,ModBlocks.improvedCraftingTableBlock,1,numUsingPlayers);
        }

        if (worldObj!=null && !worldObj.isRemote && inventoryTouched)
        {
            inventoryTouched = false;
        }

        LITInventoryCrafting crafting = new LITInventoryCrafting(3,3);
        for (int i=0;i<9;i++)
        {
            crafting.setInventorySlotContents(i,craftingGrid[i]);
        }

        setResult(CraftingManager.getInstance().findMatchingRecipe(crafting,worldObj));

        this.ticksSinceSync++;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagList list = compound.getTagList("Items",10);
        this.craftingGrid = new ItemStack[getSizeInventory()];

        if (compound.hasKey("CustomName"))
        {
            this.customName = compound.getString("CustomName");
        }

        if (compound.hasKey("Facing"))
        {
            facing = EnumFacing.values()[compound.getInteger("Facing")];
        }

        for (int i=0;i<list.tagCount();i++)
        {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot")&0xff;
            if (slot>=0 && slot<craftingGrid.length)
            {
                craftingGrid[slot] = ItemStack.loadItemStackFromNBT(stackTag);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList list = new NBTTagList();
        for (int i=0;i<craftingGrid.length;i++)
        {
            if (craftingGrid[i] != null)
            {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot",(byte)i);
                craftingGrid[i].writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        compound.setTag("Items",list);

        compound.setInteger("Facing",facing.ordinal());

        if (this.hasCustomName())
        {
            compound.setString("CustomName",customName);
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        switch (id)
        {
            case 1:
                numUsingPlayers = type;
                break;
            default:
        }

        return true;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    @Override
    public Packet<?> getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (int i=0;i<craftingGrid.length;i++)
        {
            if (getStackInSlot(i)!=null)
            {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot",(byte)i);
                craftingGrid[i].writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        nbt.setTag("Items",list);

        nbt.setInteger("Facing",facing.ordinal());

//        LogHelper.info("Created list with " + list.tagCount() + " items!");

        return new SPacketUpdateTileEntity(pos,0,nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagList list = pkt.getNbtCompound().getTagList("Items",10);
        craftingGrid = new ItemStack[9];
        for (int i=0;i<list.tagCount();i++)
        {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 0xff;
//            LogHelper.info("Writing data to slot: " + slot);
            if (slot>0 && slot<getSizeInventory())
            {
                craftingGrid[slot] = ItemStack.loadItemStackFromNBT(stackTag);
            }
        }

        facing = EnumFacing.values()[pkt.getNbtCompound().getInteger("Facing")];
    }

    public void syncInventories()
    {
        if (worldObj.isRemote)
        {
            return;
        }
        this.worldObj.markAndNotifyBlock(pos,null,worldObj.getBlockState(pos),worldObj.getBlockState(pos),3);
//        LogHelper.info("Syncing!");
    }

    @Override
    public ItemStack getResult()
    {
        return result;
    }

    @Override
    public void setResult(ItemStack stack)
    {
        if (stack==null)
        {
            result=null;
        }
        else{
            result = stack.copy();
        }
    }

    public void setFacing(EnumFacing face)
    {
        this.facing = face;
    }

    public EnumFacing getFacing()
    {
        return facing;
    }
}
