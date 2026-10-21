package game.player.model;

import game.items.*;
import game.items.armour.Armour;
import game.items.armour.ArmourSlot;
import game.items.weapon.Weapon;
import game.items.weapon.WeaponSlot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory implements ReadInventory
{
    public static final int INVENTORY_CAPACITY = 12;
    
    private ItemStack[] inventorySlots;
    private final Map<EquipSlot, EquippableItem> equippedItemsMap;
    
    public Inventory()
    {
        inventorySlots = new ItemStack[INVENTORY_CAPACITY];
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
            inventorySlots[i] = new ItemStack();
        equippedItemsMap = new HashMap<>();
        for(WeaponSlot weaponSlot : WeaponSlot.values())
            equippedItemsMap.put(weaponSlot, null);
        
        for(ArmourSlot armourSlot : ArmourSlot.values())
            equippedItemsMap.put(armourSlot, null);
    }
    
    public boolean addToInventory(Item itemToAdd)
    {
        if(itemToAdd == null)
            return false;
        
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
            if(inventorySlots[i].getItem() != null && inventorySlots[i].getItem().equals(itemToAdd))
            {
                inventorySlots[i].addItem();
                return true;
            }
            
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
            if(inventorySlots[i].getItem() == null)
            {
                inventorySlots[i].setItem(itemToAdd);
                return true;
            }
        
        return false;
    }
    
    public boolean removeFromInventory(Item itemToRemove, boolean removeStack)
    {
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
        {
            if(inventorySlots[i].getItem() != null && inventorySlots[i].getItem().equals(itemToRemove))
            {
                if(removeStack)
                    inventorySlots[i].removeItemStack();
                else
                    inventorySlots[i].removeItem();
                
                return true;
            }
        }
        
        return false;
    }
    
    public boolean equipItem(int originalIndex, EquipSlot targetEquipSlot)
    {
        if(originalIndex < 0 || originalIndex >= INVENTORY_CAPACITY)
            return false;
        
        return equipItem(inventorySlots[originalIndex].getItem(), targetEquipSlot);
    }
    
    public boolean equipItem(Item originalItem, EquipSlot targetEquipSlot)
    {
        if(originalItem instanceof EquippableItem item)
        {
            if(item.getEquipSlot() == targetEquipSlot)
            {
                int originalIndex = getItemIndex(originalItem);
                if(originalIndex == -1)
                    return false;
                EquippableItem oldItem = equippedItemsMap.get(targetEquipSlot);
                
                if(oldItem != null && getItemCount(item) > 1 && getItemCount(oldItem) == 0 && !hasOpenItemSlot())
                    return false;
                
                EquippableItem inventoryItem = (EquippableItem)inventorySlots[originalIndex].getItem();
                equippedItemsMap.replace(targetEquipSlot, inventoryItem.copy());
                removeFromInventory(inventoryItem, false);
                
                if(oldItem == null)
                    return true;
                
                if(addToInventory(oldItem))
                {
                    if(getItemCount(item) == 0 && getItemCount(oldItem) == 1)
                        swapItems(originalIndex, getItemIndex(oldItem));
                    
                    return true;
                }
                else
                {
                    equippedItemsMap.replace(item.getEquipSlot(), oldItem);
                    if(inventorySlots[originalIndex].getItem() == null)
                        inventorySlots[originalIndex].setItem(inventoryItem);
                    else
                        inventorySlots[originalIndex].addItem();
                    
                    return false;
                }
            }
        }
        
        return false;
    }
    
    public boolean unequipItem(EquipSlot originalEquipSlot, int targetIndex)
    {
        if(targetIndex < 0 || targetIndex >= INVENTORY_CAPACITY)
            return false;
        return unequipItem(equippedItemsMap.get(originalEquipSlot), targetIndex);
    }
    
    public boolean unequipItem(EquippableItem originalItem, int targetIndex)
    {
        EquippableItem equippedItem = originalItem == null ? null : equippedItemsMap.get(originalItem.getEquipSlot());
        
        if(equippedItem == null || !equippedItem.equals(originalItem))
            return false;
        
        if(targetIndex < 0 || targetIndex >= INVENTORY_CAPACITY)
            return false;
        
        if(inventorySlots[targetIndex].getItem() instanceof EquippableItem equippableItem)
            if(equippableItem.getEquipSlot().equals(equippedItem.getEquipSlot()))
                return equipItem(equippableItem, equippedItem.getEquipSlot());
        
        if(addToInventory(equippedItem))
        {
            if(getItemCount(equippedItem) == 1)
                swapItems(getItemIndex(equippedItem), targetIndex);
                
            equippedItemsMap.replace(equippedItem.getEquipSlot(), null);
            return true;
        }
        
        return false;
    }
    
    public void removeFromEquipped(EquippableItem equippedItem)
    {
        if(equippedItem == null)
            return;
        
        
        equippedItemsMap.replace(equippedItem.getEquipSlot(), null);
    }
    
    public boolean swapItems(int originalIndex, int targetIndex)
    {
        if(originalIndex < 0 || originalIndex >= INVENTORY_CAPACITY || targetIndex < 0 || targetIndex >= INVENTORY_CAPACITY)
            return false;
        
        ItemStack originalItemStack = inventorySlots[originalIndex];
        ItemStack targetItemStack = inventorySlots[targetIndex];
        
        inventorySlots[targetIndex] = originalItemStack;
        inventorySlots[originalIndex] = targetItemStack;
        
        return true;
    }
    
    private boolean hasOpenItemSlot()
    {
        for(ItemStack itemStack : inventorySlots)
            if(itemStack.getItem() == null)
                return true;
        
        return false;
    }
    
    public int getItemCount(Item item)
    {
        for(int i = 0 ; i < INVENTORY_CAPACITY; i++)
            if(inventorySlots[i].getItem() != null && inventorySlots[i].getItem().equals(item))
                return inventorySlots[i].getQuantity();
        
        return 0;
    }
    
    public int getItemIndex(Item item)
    {
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
            if(inventorySlots[i].getItem() != null && inventorySlots[i].getItem().equals(item))
                return i;
        
        return -1;
    }
    
    public ItemStack[] getInventorySlots()
    {
        ItemStack[] inventorySlotsCopy = new ItemStack[INVENTORY_CAPACITY];
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
            inventorySlotsCopy[i] = inventorySlots[i].copy();
        
        return inventorySlotsCopy;
    }
    
    public void setInventory(ItemStack[] loadedItemStacks)
    {
        if(loadedItemStacks == null || loadedItemStacks.length != INVENTORY_CAPACITY)
            return;
        
        ItemStack[] copiedItemStacks = new ItemStack[INVENTORY_CAPACITY];
        
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
            if(loadedItemStacks[i] == null)
                return;
            else
                copiedItemStacks[i] = loadedItemStacks[i].copy();
        
        inventorySlots = copiedItemStacks;
    }
    
    public ItemStack getItemStack(int index)
    {
        return (index < 0 || index >= INVENTORY_CAPACITY) ? new ItemStack() : inventorySlots[index].copy();
    }
    
    public <I extends Item> I[] getAllItemTypeInInventory(Class<I> itemClass)
    {
        List<I> itemList = new ArrayList<>();
        
        for(Item item : getItemsInInventory())
            if(itemClass.isInstance(item))
                itemList.add(itemClass.cast(item.copy()));
        
        I[] result = (I[])Array.newInstance(itemClass, itemList.size());
        
        return itemList.toArray(result);
    }
    
    private Item[] getItemsInInventory()
    {
        Item[] items = new Item[INVENTORY_CAPACITY];
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
            items[i] = inventorySlots[i].getItem();
        
        return items;
    }
    
    public EquipSlot[] getAllEquippedSlots()
    {
        WeaponSlot[] weaponSlots = WeaponSlot.values();
        ArmourSlot[] armourSlots = ArmourSlot.values();
        EquipSlot[] equipSlots = new EquipSlot[weaponSlots.length + armourSlots.length];
        int i = 0;
        
        for(WeaponSlot weaponSlot : weaponSlots)
            equipSlots[i++] = weaponSlot;
        for(ArmourSlot armourSlot : armourSlots)
            equipSlots[i++] = armourSlot;
        
        return equipSlots;
    }
    
    public EquippableItem getEquippedItem(EquipSlot slot)
    {
        EquippableItem item = equippedItemsMap.get(slot);
        return item == null ? null : item.copy();
    }
    
    public Armour[] getEquippedArmour()
    {
        List<Armour> armourlist = new ArrayList<>();
        
        for(ArmourSlot armourSlot : ArmourSlot.values())
            if(equippedItemsMap.get(armourSlot) instanceof Armour armour)
                armourlist.add(armour.copy());
        
        return armourlist.toArray(new Armour[0]);
    }
    
    public Weapon getEquippedWeapon()
    {
        for(WeaponSlot weaponSlot : WeaponSlot.values())
            if(equippedItemsMap.get(weaponSlot) instanceof Weapon weapon)
                return weapon.copy();
        
        return null;
    }
    
    public void setEquipped(EquippableItem[] loadedEquippedArray)
    {
        if(loadedEquippedArray == null)
            return;
        
        Map<EquipSlot, EquippableItem> copiedEquippedMap = new HashMap<>();
        
        for(EquippableItem equippableItem : loadedEquippedArray)
            if(equippableItem == null || !equippedItemsMap.containsKey(equippableItem.getEquipSlot()) || copiedEquippedMap.containsKey(equippableItem.getEquipSlot()))
                return;
            else
                copiedEquippedMap.put(equippableItem.getEquipSlot(), equippableItem.copy());
        
        equippedItemsMap.replaceAll((slot, item) -> null);
        equippedItemsMap.putAll(copiedEquippedMap);
    }
}