package com.m4thg33k.lit.tiles;

import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import com.m4thg33k.lit.LIT;
import com.m4thg33k.lit.core.util.LITMathHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;

public class TileDeathBlock extends TileEntity implements ISidedInventory {

    //private ItemStack[] inventory = new ItemStack[0];
    private String playerName = "";

    private InventoryPlayer savedPlayerInventory = new InventoryPlayer(null);
    private NBTTagCompound baublesNBT = new NBTTagCompound();
    private Object savedPlayerBaubles = null;

    private int angle = 0;

    public TileDeathBlock()
    {
        super();
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
    public int getSizeInventory() {
        return savedPlayerInventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return savedPlayerInventory.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return savedPlayerInventory.decrStackSize(index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return savedPlayerInventory.removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        savedPlayerInventory.setInventorySlotContents(index, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return savedPlayerInventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        if (playerName.equals(player.getName()))
        {
            return true;
        }
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
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
        savedPlayerInventory.clear();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    public void setPlayerName(String name)
    {
        playerName = name;
    }

    public void grabPlayer(EntityPlayer player)
    {

        angle = LITMathHelper.rand.nextInt(720); //Minecraft.getMinecraft().theWorld.rand.nextInt(720);
        if (worldObj.isRemote)
        {
            return;
        }
        setPlayerName(player.getName());
        setThisInventory(player.inventory);

        if (LIT.isBaublesInstalled)
        {
            setBaubleInventory(player);
        }
    }

    public void setBaubleInventory(EntityPlayer player)
    {
        PlayerHandler.getPlayerBaubles(player).saveNBT(baublesNBT);
//        savedPlayerBaubles = PlayerHandler.getPlayerBaubles(player);
        PlayerHandler.clearPlayerBaubles(player);
    }

    public void setThisInventory(InventoryPlayer inventoryPlayer)
    {
        this.savedPlayerInventory.copyInventory(inventoryPlayer);
        inventoryPlayer.clear();
    }

    public boolean isSamePlayer(EntityPlayer player)
    {
        return player.getName().equals(playerName);
    }

    //debug method
    public void toggleName(EntityPlayer player)
    {
        if (playerName.equals(player.getName()))
        {
            playerName = "NOPENOPENOPE";
        }
        else
        {
            playerName = player.getName();
        }
        worldObj.markAndNotifyBlock(pos,null,worldObj.getBlockState(pos),worldObj.getBlockState(pos),0);
    }

    public String getPlayerName()
    {
        return playerName;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        playerName = compound.getString("PlayerName");

        savedPlayerInventory.readFromNBT(compound.getTagList("Inventory",10));

        baublesNBT = compound.getCompoundTag("BaublesNBT");

        angle = compound.getInteger("DeathAngle");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("PlayerName",playerName);


        NBTTagList tagList = new NBTTagList();
        savedPlayerInventory.writeToNBT(tagList);
        compound.setTag("Inventory",tagList);

        compound.setTag("BaublesNBT",baublesNBT);

        compound.setInteger("DeathAngle",angle);
    }

    public void onCollision(EntityPlayer player)
    {
        if (worldObj.isRemote || !isSamePlayer(player))
        {
            return;
        }

        for (int i=0;i<savedPlayerInventory.getSizeInventory();i++)
        {
            if(savedPlayerInventory.getStackInSlot(i)!=null && savedPlayerInventory.getStackInSlot(i).stackSize>0) {
                if (player.inventory.getStackInSlot(i) == null) {
                    player.inventory.setInventorySlotContents(i, savedPlayerInventory.getStackInSlot(i));
                } else {
                    EntityItem entityItem = new EntityItem(worldObj, player.posX, player.posY, player.posZ, savedPlayerInventory.getStackInSlot(i));
                    worldObj.spawnEntityInWorld(entityItem);
                }
            }
        }

        if (LIT.isBaublesInstalled) {
            IInventory currentBaubles = PlayerHandler.getPlayerBaubles(player);
            InventoryBaubles savedBaubleData = new InventoryBaubles(player);
            savedBaubleData.readNBT(baublesNBT);
            IInventory savedBaubles = (IInventory)savedBaubleData;
            baublesNBT = new NBTTagCompound();
            for (int i = 0; i < currentBaubles.getSizeInventory();i++){ //currentBaubles.func_70302_i_(); i++) {
                if (savedBaubles.getStackInSlot(i) != null && savedBaubles.getStackInSlot(i).stackSize > 0) {
                    if (currentBaubles.getStackInSlot(i) == null) {
                        currentBaubles.setInventorySlotContents(i, savedBaubles.getStackInSlot(i));
                    }
                    else {
                        EntityItem entityItem = new EntityItem(worldObj, player.posX, player.posY, player.posZ, savedBaubles.getStackInSlot(i));
                        worldObj.spawnEntityInWorld(entityItem);
                    }
                }
            }
        }

        savedPlayerInventory = new InventoryPlayer(null);
        worldObj.setBlockToAir(pos);
    }

    @Override
    public Packet<?> getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("PlayerName",playerName);
        compound.setInteger("DeathAngle",angle);
        return new SPacketUpdateTileEntity(pos,0,compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        playerName = pkt.getNbtCompound().getString("PlayerName");
        angle = pkt.getNbtCompound().getInteger("DeathAngle");
    }

    public int getAngle()
    {
        return angle;
    }

    public void dropAllItems()
    {
        InventoryHelper.dropInventoryItems(worldObj,pos,this);

        if (LIT.isBaublesInstalled) {
            InventoryBaubles baubles = new InventoryBaubles(null);
            baubles.readNBT(baublesNBT);
            InventoryHelper.dropInventoryItems(worldObj, pos, baubles);
        }
    }
}
