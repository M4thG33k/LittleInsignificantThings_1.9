package com.m4thg33k.lit.tiles;

import com.m4thg33k.lit.api.chest.ChestTypes;
import com.m4thg33k.lit.blocks.ModBlocks;
import com.m4thg33k.lit.inventory.ContainerImprovedChest;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileImprovedChest extends TileEntityLockable implements ITickable, IInventory{

    protected int ticksSinceSync = -1;

    public float prevLidAngle;
    public float lidAngle;

    protected int numUsingPlayers;
    protected ChestTypes type;
    public ItemStack[] inventory;
    protected EnumFacing facing;
    protected boolean inventoryTouched;
    protected String customName;

    public TileImprovedChest()
    {
        this(ChestTypes.getTypeByName("Improved"));
    }

    public TileImprovedChest(ChestTypes type)
    {
        super();
        this.type = type;
        this.inventory = new ItemStack[getSizeInventory()];
        this.facing = EnumFacing.NORTH;
    }

    public void setContents(ItemStack[] contents)
    {
        inventory = new ItemStack[getSizeInventory()];
        for (int i=0;i<contents.length;i++)
        {
            if (i<inventory.length)
            {
                inventory[i] = contents[i];
            }
        }
        inventoryTouched = true;
    }

    @Override
    public int getSizeInventory() {
        return type.getSize();
    }

    public EnumFacing getFacing()
    {
        return this.facing;
    }

    public ChestTypes getType()
    {
        return this.type;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        inventoryTouched = true;
        return inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (inventory[index] != null)
        {
            if (inventory[index].stackSize <= count)
            {
                ItemStack itemStack = inventory[index];
                inventory[index] = null;
                markDirty();
                return itemStack;
            }
            ItemStack itemStack =inventory[index].splitStack(count);
            if (inventory[index].stackSize==0)
            {
                inventory[index] = null;
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
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory[index] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : type.getTypeName();
    }

    @Override
    public boolean hasCustomName() {
        return this.customName!=null && !this.customName.equals("");
    }

    public void setCustomName(String name)
    {
        this.customName = name;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagList list = compound.getTagList("Items",10);
        this.inventory = new ItemStack[getSizeInventory()];

        if (compound.hasKey("CustomName"))
        {
            this.customName = compound.getString("CustomName");
        }

        for (int i=0;i<list.tagCount();i++)
        {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 0xff;
            if (slot >= 0 && slot<inventory.length)
            {
                inventory[slot] = ItemStack.loadItemStackFromNBT(stackTag);
            }
        }
        facing = EnumFacing.values()[compound.getInteger("Facing")];
//        facing = compound.getByte("Facing");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
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
        compound.setInteger("Facing",facing.ordinal());
//        compound.setByte("Facing",facing);

        if (this.hasCustomName())
        {
            compound.setString("CustomName",customName);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        if (worldObj==null)
        {
            return true;
        }
        if (worldObj.getTileEntity(pos) != this)
        {
            return false;
        }
        return player.getDistanceSq(pos.add(0.5,0.5,0.5))<=64;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        switch (id)
        {
            case 1:
                numUsingPlayers = type;
                break;
            case 2:
                facing = EnumFacing.VALUES[(byte)type];
                break;
            case 3:
                facing = EnumFacing.VALUES[(byte)(type & 0x7)];
                numUsingPlayers = (type & 0xF8)>>3;
                break;
            default:
        }
        return true;
    }

    @Override
    public void update() {
        //resync clients with the server state
        if (worldObj!=null && !this.worldObj.isRemote && this.numUsingPlayers!=0 && (this.ticksSinceSync + pos.getX() + pos.getY() + pos.getZ())%200==0) {
            this.numUsingPlayers = 0;
            float var1 = 5.0f;
            List<EntityPlayer> var2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.getX() - var1, pos.getY() - var1, pos.getZ() - var1, pos.getX() + 1 + var1, pos.getY() + 1 + var1, pos.getZ() + 1 + var1));

            for (EntityPlayer var4 : var2) {
                if (var4.openContainer instanceof ContainerImprovedChest) {
                    ++this.numUsingPlayers;
                }
            }
        }

        if (worldObj != null && !worldObj.isRemote && ticksSinceSync<0)
        {
            worldObj.addBlockEvent(pos, ModBlocks.improvedChestBlock,3,((numUsingPlayers << 3) & 0xF8 | ((byte)facing.ordinal() & 0x7)));
        }
        if (!worldObj.isRemote && inventoryTouched)
        {
            inventoryTouched = false;
        }

        this.ticksSinceSync++;
        prevLidAngle = lidAngle;
        float f = 0.1f;
        if (numUsingPlayers>0 && lidAngle==0f)
        {
            double d = pos.getX() + 0.5;
            double d1 = pos.getZ() + 0.5;

            worldObj.playSound((EntityPlayer)null,d,pos.getY()+0.5,d1, SoundEvents.block_chest_open, SoundCategory.BLOCKS,0.5f,worldObj.rand.nextFloat()*0.1f+0.9f);
//            worldObj.playAuxSFX(d,pos.getY()+0.5,d1,"random.chestopen",0.5f,worldObj.rand.nextFloat()*0.1f+0.9f);
        }
        if (numUsingPlayers == 0 && lidAngle > 0.0f || numUsingPlayers > 0 && lidAngle < 1.0f)
        {
            float f1 = lidAngle;
            if (numUsingPlayers>0)
            {
                lidAngle += f;
            }
            else
            {
                lidAngle -= f;
            }
            if (lidAngle>1.0f)
            {
                lidAngle = 1.0f;
            }
            float f2 = 0.5f;
            if (lidAngle < f2 && f1 >= f2)
            {
                double d2 = pos.getX() + 0.5;
                double d3 = pos.getZ() + 0.5;
                worldObj.playSound((EntityPlayer)null,d2,pos.getY()+0.5,d3, SoundEvents.block_chest_close, SoundCategory.BLOCKS,0.5f,worldObj.rand.nextFloat()*0.1f+0.9f);
//                worldObj.playSoundEffect(d2,pos.getY()+0.5,d3,"random.chestclosed",0.5f, worldObj.rand.nextFloat()*0.1f+0.9f);
            }
            if (lidAngle<0.0f)
            {
                lidAngle = 0.0f;
            }
        }

    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (worldObj == null)
        {
            return;
        }
        numUsingPlayers++;
        worldObj.addBlockEvent(pos,ModBlocks.improvedChestBlock,1,numUsingPlayers);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (worldObj == null)
        {
            return;
        }
        numUsingPlayers--;
        worldObj.addBlockEvent(pos, ModBlocks.improvedChestBlock,1,numUsingPlayers);
    }

    public void setFacing(EnumFacing facing)
    {
        this.facing = facing;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("Type",getType().getTypeName());
        nbt.setByte("Facing",(byte)facing.ordinal());

        return new SPacketUpdateTileEntity(pos,0,nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        if (pkt.getTileEntityType() == 0)
        {
            NBTTagCompound compound = pkt.getNbtCompound();
            type = ChestTypes.getTypeByName(compound.getString("Type"));
            facing = EnumFacing.values()[compound.getByte("Facing")];
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (this.inventory[index]!=null)
        {
            ItemStack stack = this.inventory[index];
            this.inventory[index] = null;
            return stack;
        }
        return null;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    public void rotateAround()
    {
        facing.rotateY();
//        facing++;
//        if (facing > EnumFacing.EAST.ordinal())
//        {
//            facing = (byte)EnumFacing.NORTH.ordinal();
//        }
//        setFacing(facing);
        worldObj.addBlockEvent(pos,ModBlocks.improvedChestBlock,2,(byte)facing.ordinal());
    }

    public void wasPlaced(EntityLivingBase entityLivingBase, ItemStack itemStack)
    {

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
        for (int i=0;i<this.inventory.length;i++)
        {
            this.inventory[i] = null;
        }
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return null;
    }

    @Override
    public String getGuiID() {
        return "GemChest:" + type.getTypeName(); 
    }

    @Override
    public boolean canRenderBreaking() {
        return true;
    }
}
