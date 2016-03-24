package com.m4thg33k.lit.tiles;

import com.m4thg33k.lit.api.furnace.FurnaceTypes;
import com.m4thg33k.lit.core.util.LogHelper;
import com.m4thg33k.lit.lib.Constants;
import com.m4thg33k.lit.network.packets.LITPackets;
import com.m4thg33k.lit.network.packets.PacketNBT;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;

public class TileImprovedFurnace extends TileEntity implements IInventory,ITickable{

    private ItemStack[] inventory;
    private String customName;
    private EnumFacing facing;
    private boolean isOn;

    public int burnTime;
    public int currentItemBurnTime;
    public int cookTime;
    public int totalCookTime;
    public int storedFuel;

    private String defaultName;

    private double cookTimeFactor;
    private double fuelBooster;
    private int upgradeCount;
    private int maxFuel;

    private ItemStack[] upgrades;
    private int numUpgradesInstalled;

    public TileImprovedFurnace()
    {
        this(FurnaceTypes.getTypeByName("Improved"));
    }

    public TileImprovedFurnace(FurnaceTypes type)
    {
        this.cookTimeFactor = type.getCookTimeFactor();
        this.fuelBooster = type.getFuelBooster();
        this.upgradeCount = type.getUpgradeCount();
        this.maxFuel = type.getMaxFuel();

        this.facing = EnumFacing.NORTH;
        this.isOn = false;
        this.inventory = new ItemStack[getSizeInventory()];
        this.numUpgradesInstalled = 0;

        this.defaultName = type.getTypeName();
//        this.setCustomName(defaultName);

        if (this.upgradeCount > 0)
        {
            this.upgrades = new ItemStack[this.upgradeCount];
        }
        else
        {
            this.upgrades = null;
        }
    }

    public int getMaxFuel()
    {
        return maxFuel;
    }

    public boolean getOn()
    {
        return isOn;
    }

    public void setFacing(EnumFacing facing)
    {
        this.facing = facing;
        markDirty();
    }

    public EnumFacing getFacing()
    {
        return this.facing;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.facing = EnumFacing.values()[compound.getInteger("facing")];
        this.isOn = compound.getBoolean("on");

        NBTTagList list = compound.getTagList("Items",10);
        for (int i=0;i<list.tagCount();i++)
        {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot")&255;
            setInventorySlotContents(slot,ItemStack.loadItemStackFromNBT(stackTag));
        }

        if (compound.hasKey("CustomName"))
        {
            setCustomName(compound.getString("CustomName"));
        }

        burnTime = compound.getInteger("burnTime");
        currentItemBurnTime = compound.getInteger("currentItemBurnTime");
        cookTime = compound.getInteger("cookTime");
        totalCookTime = compound.getInteger("totalCookTime");
        storedFuel = compound.getInteger("storedFuel");

        cookTimeFactor = compound.getDouble("cookTimeFactor");
        upgradeCount = compound.getInteger("upgradeCount");
        fuelBooster = compound.getDouble("fuelBooster");
        maxFuel = compound.getInteger("maxFuel");


        if (upgradeCount==0)
        {
            upgrades = null;
        }
        else
        {
            upgrades = new ItemStack[upgradeCount];
        }

        numUpgradesInstalled = compound.getInteger("numUpgradesInstalled");
        NBTTagList list2 = compound.getTagList("Upgrades",10);
        for (int i=0;i<list2.tagCount();i++)
        {
            NBTTagCompound stackTag = list2.getCompoundTagAt(i);
            int slot = stackTag.getInteger("Slot");
            this.upgrades[slot] = ItemStack.loadItemStackFromNBT(stackTag);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("facing",facing.ordinal());
        compound.setBoolean("on",isOn);

        NBTTagList list = new NBTTagList();
        for (int i=0;i<getSizeInventory();i++)
        {
            if (getStackInSlot(i)!=null) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                getStackInSlot(i).writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        compound.setTag("Items",list);

        if (hasCustomName())
        {
            compound.setString("CustomName",getCustomName());
        }

        compound.setInteger("burnTime",burnTime);
        compound.setInteger("currentItemBurnTime",currentItemBurnTime);
        compound.setInteger("cookTime",cookTime);
        compound.setInteger("totalCookTime",totalCookTime);
        compound.setInteger("storedFuel",storedFuel);

        compound.setDouble("cookTimeFactor",cookTimeFactor);
        compound.setInteger("upgradeCount",upgradeCount);
        compound.setDouble("fuelBooster",fuelBooster);
        compound.setInteger("maxFuel",maxFuel);


        compound.setInteger("numUpgradesInstalled",numUpgradesInstalled);
        NBTTagList list2 = new NBTTagList();
        for (int i = 0; i < upgradeCount; i++) {
            if (upgrades[i] != null) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setInteger("Slot",i);
                this.upgrades[i].writeToNBT(stackTag);
                list2.appendTag(stackTag);
            }
        }

        compound.setTag("Upgrades",list2);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeToNBT(tagCompound);

        return new SPacketUpdateTileEntity(pos,1,tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    public String getCustomName()
    {
        return customName;
    }

    public void setCustomName(String name)
    {
        this.customName = name;
    }

    @Override
    public int getSizeInventory() {
        return 3;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index<0 || index>=this.getSizeInventory())
        {
            return null;
        }
        return inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.getStackInSlot(index)!=null)
        {
            ItemStack itemstack;
            if (getStackInSlot(index).stackSize <= count)
            {
                itemstack = getStackInSlot(index);
                setInventorySlotContents(index,null);
                markDirty();
                return itemstack;
            }
            itemstack = this.getStackInSlot(index).splitStack(count);

            if (getStackInSlot(index).stackSize<=0)
            {
                setInventorySlotContents(index,null);
            }
            else
            {
                //just to show that changes happened
                setInventorySlotContents(index,getStackInSlot(index));
            }

            markDirty();
            return itemstack;
        }

        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index<0 || index>=getSizeInventory())
        {
            return null;
        }

        ItemStack stack = inventory[index];
        inventory[index] = null;
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        boolean flag = stack!=null && stack.isItemEqual(inventory[index]) && ItemStack.areItemStackTagsEqual(stack,inventory[index]);
        inventory[index] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }

        if (index==1 && !flag)
        {
            totalCookTime = getCookTime(stack);
            cookTime = 0;
            markDirty();
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
        switch (index)
        {
            case 0: //fuel
                return isItemFuel(stack);
            case 1: //has a furnace recipe
                return hasFurnaceRecipe(stack);
            default:
                return false;
        }
    }

    @Override
    public int getField(int id) {
        switch (id)
        {
            case 0:
                return burnTime;
            case 1:
                return currentItemBurnTime;
            case 2:
                LogHelper.info("Cook time: " + cookTime);
                return cookTime;
            case 3:
                return totalCookTime;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id)
        {
            case 0:
                burnTime = value;
                break;
            case 1:
                currentItemBurnTime = value;
                break;
            case 2:
                cookTime = value;
                break;
            case 3:
                totalCookTime = value;
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
    }

    @Override
    public void clear() {
        for (int i=0;i<getSizeInventory();i++)
        {
            setInventorySlotContents(i,null);
        }
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : defaultName + " Furnace";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.equals("");
    }

    @Override
    public ITextComponent getDisplayName() {
//        return null;
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }

    public boolean isBurning()
    {
        return burnTime>0;
    }

    public void storeFuel()
    {
        if (inventory[0]==null)
        {
            return;
        }

        int fuelToAdd = getItemBurnTime(inventory[0]);
        if (fuelToAdd==0 || fuelToAdd+storedFuel>maxFuel)
        {
            return;
        }

        storedFuel += fuelToAdd;

        inventory[0].stackSize--;
        if (inventory[0].stackSize==0)
        {
            inventory[0] = inventory[0].getItem().getContainerItem(inventory[0]);
        }

    }

    @Override
    public void update() {
        boolean on = isBurning();
        boolean isDirty = false;


        if (isBurning()) {
            --burnTime;
        }


        if (!worldObj.isRemote) {
            storeFuel();

            if (isBurning() || inventory[1] != null && hasEnoughFuel()) {
                if (!isBurning() && canSmelt()) {
                    currentItemBurnTime = burnTime = getCookTime(inventory[1]);
                    storedFuel -= getCookCost(inventory[1]);
                    if (isBurning()) {
                        isDirty = true;
                    }
                }

                if (isBurning() && canSmelt()) {
                    cookTime++;

                    if (cookTime == totalCookTime) {
                        cookTime = 0;
                        totalCookTime = getCookTime(inventory[1]);
                        smeltItem();
                        isDirty = true;
                    }
                } else {
                    cookTime = 0;
                }
            } else if (!isBurning() && cookTime > 0) {
                cookTime = MathHelper.clamp_int(cookTime - 2, 0, totalCookTime);
            }

            if (on != isBurning()) {
                isDirty = true;
                isOn = isBurning();
            }
        }

        if (isDirty) {
            markDirty();
            NBTTagCompound tag = new NBTTagCompound();
            writeToNBT(tag);
            LITPackets.INSTANCE.sendToAllAround(new PacketNBT(pos, tag), new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32));
        }
    }

    public int getCookTime(ItemStack stack)
    {
        return (int)(Math.floor(200*cookTimeFactor));
    }

    public int getCookCost(ItemStack stack)
    {
        return 200;
    }

    /**
     * Returns the number of ticks that the supplied fuel will keep the furnace burning (including fuelBoost) or 0 if
     * the item is not fuel
     */
    public int getItemBurnTime(ItemStack stack)
    {
        if (stack==null)
        {
            return 0;
        }

        return (int)Math.ceil(TileEntityFurnace.getItemBurnTime(stack)*fuelBooster);
    }

    public boolean isItemFuel(ItemStack stack)
    {
        return getItemBurnTime(stack)>0;
    }

    public boolean hasFurnaceRecipe(ItemStack stack)
    {
        return FurnaceRecipes.instance().getSmeltingResult(stack)!=null;
    }

    public boolean hasEnoughFuel()
    {
        if (inventory[1]==null)
        {
            return false;
        }
        return storedFuel >= getCookCost(inventory[1]);
    }

    /**
     * checks if the item in the input slot can be smelted and stack its output in the output slot
     */

    private boolean canSmelt()
    {
        if (inventory[1]==null || !hasFurnaceRecipe(inventory[1])) //anything in input that can be smelted?
        {
            return false;
        }
        if (inventory[2]==null)
        {
            return true;
        }
        ItemStack output = FurnaceRecipes.instance().getSmeltingResult(inventory[1]);
        if (!inventory[2].isItemEqual(output))
        {
            return false;
        }
        int result = inventory[2].stackSize+output.stackSize;
        return result<=getInventoryStackLimit() && result<=inventory[2].getMaxStackSize();
    }

    public void smeltItem()
    {
        if (canSmelt())
        {
            ItemStack stack = FurnaceRecipes.instance().getSmeltingResult(inventory[1]);

            if (inventory[2] == null)
            {
                inventory[2] = stack.copy();
            }
            else if (inventory[2].getItem() == stack.getItem())
            {
                inventory[2].stackSize += stack.stackSize;
            }

            if (inventory[1].getItem() == Item.getItemFromBlock(Blocks.sponge) && inventory[1].getMetadata()==1 && inventory[0] != null && inventory[0].getItem() == Items.bucket)
            {
                inventory[0] = new ItemStack(Items.water_bucket);
            }

            inventory[1].stackSize--;

            if (inventory[1].stackSize<=0)
            {
                inventory[1] = null;
            }
        }
    }

    //returns the largest number of items you can smelt with the currently stored fuel
    public int getNumSmeltable()
    {
//        LogHelper.info(""+storedFuel+","+getCookTime(null)+","+(storedFuel/getCookTime(null)));
        return (storedFuel/getCookCost(null));
    }

    public boolean canReceiveUpgrade()
    {
        return numUpgradesInstalled<upgradeCount;
    }

    public void installUpgrade(ItemStack stack)
    {
        upgrades[numUpgradesInstalled] = stack.copy();
        numUpgradesInstalled++;

        switch (stack.getItemDamage())
        {
            case 0: //speed upgrade
                cookTimeFactor *= Constants.SPEED_MULT;
                break;
            case 1: //fuel boost upgrade
                fuelBooster *= Constants.FUEL_MULT;
                break;
            case 2: //capacity upgrade
                maxFuel *= Constants.CAP_MULT;
                break;
            default:
        }
    }

    public int getUpgradeCount()
    {
        return upgradeCount;
    }

    public ItemStack[] getUpgrades()
    {
        return upgrades;
    }

    public int getNumUpgradesInstalled()
    {
        return numUpgradesInstalled;
    }

    public void dropUpgrades(World world, BlockPos pos)
    {
        for (int i=0;i<numUpgradesInstalled;i++)
        {
            float f = world.rand.nextFloat() * 0.8f + 0.1f;
            float f1 = world.rand.nextFloat() * 0.8f + 0.1f;
            float f2 = world.rand.nextFloat() * 0.8f + 0.1f;

            float f3 = 0.05f;

            EntityItem item = new EntityItem(world, pos.getX()+f,pos.getY()+f1,pos.getZ()+f2,upgrades[i]);

            item.motionX = world.rand.nextGaussian() * (double)f3;
            item.motionY = world.rand.nextGaussian() * (double)f3 + 0.2;
            item.motionZ = world.rand.nextGaussian() * (double)f3;

            world.spawnEntityInWorld(item);
        }
    }
}
