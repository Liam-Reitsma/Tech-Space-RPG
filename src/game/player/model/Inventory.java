package game.player.model;

import game.items.*;
import game.items.armour.Armour;
import game.items.armour.ArmourSlot;
import game.items.weapon.Weapon;
import game.items.weapon.WeaponSlot;
import game.items.weapon.WeaponType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory
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
    
    public void setInventory(ItemStack[] loadedItemStacks)
    {
        inventorySlots = loadedItemStacks;
    }
    
    public void setEquipped(EquippableItem[] loadedEquippedArray)
    {
        for(EquippableItem loadedEquippedItem : loadedEquippedArray)
            equippedItemsMap.put(loadedEquippedItem.getEquipSlot(), loadedEquippedItem);
    }
    
    public void addToInventory(Item item)
    {
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
            if(inventorySlots[i].getItem() != null && inventorySlots[i].getItem().equals(item))
            {
                inventorySlots[i].addItem();
                return;
            }
            
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
            if(inventorySlots[i].getItem() == null)
            {
                inventorySlots[i].setItem(item);
                return;
            }
    }
    
    public void removeFromInventory(Item item, boolean removeStack)
    {
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
        {
            if(inventorySlots[i].getItem() != null && inventorySlots[i].getItem().equals(item))
            {
                if(removeStack)
                    inventorySlots[i].removeStack();
                else
                    inventorySlots[i].removeItem();
                
                return;
            }
        }
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
                
                equippedItemsMap.replace(item.getEquipSlot(), item);
                removeFromInventory(item, false);
                
                if(oldItem == null)
                    return true;
                
                addToInventory(oldItem);
                if(getItemCount(item) == 0 && getItemCount(oldItem) == 1)
                    swapItems(originalIndex, getItemIndex(oldItem));
                return true;
            }
        }
        
        return false;
    }
    
    public boolean unequipItem(EquippableItem item, int targetIndex)
    {
        if(item == null || equippedItemsMap.get(item.getEquipSlot()) == null || !equippedItemsMap.containsValue(item))
            return false;
        
        if(targetIndex < 0 || targetIndex >= INVENTORY_CAPACITY)
            return false;
        
        if(inventorySlots[targetIndex].getItem() instanceof EquippableItem equippableItem)
            if(equippableItem.getEquipSlot().equals(item.getEquipSlot()))
                return equipItem(equippableItem, item.getEquipSlot());
        
        addToInventory(item);
        if(getItemCount(item) == 1)
            swapItems(getItemIndex(item), targetIndex);
        equippedItemsMap.replace(item.getEquipSlot(), null);
        return true;
    }
    
    public void swapItems(int originalIndex, int targetIndex)
    {
        if(originalIndex < 0 || originalIndex >= INVENTORY_CAPACITY || targetIndex < 0 || targetIndex >= INVENTORY_CAPACITY)
            return;
        
        ItemStack originalItemStack = inventorySlots[originalIndex];
        ItemStack targetItemStack = inventorySlots[targetIndex];
        
        inventorySlots[targetIndex] = originalItemStack;
        inventorySlots[originalIndex] = targetItemStack;
    }
    
    public Item[] getItemsInInventory()
    {
        Item[] items = new Item[INVENTORY_CAPACITY];
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
            items[i] = inventorySlots[i].getItem();
        
        return items;
    }
    
    public int getItemCount(Item item)
    {
        for(int i = 0 ; i < INVENTORY_CAPACITY; i++)
            if(inventorySlots[i].getItem() != null && inventorySlots[i].getItem().equals(item))
                return inventorySlots[i].getQuantity();
            
        return 0;
    }
    
    public ItemStack[] getInventorySlots()
    {
        return inventorySlots.clone();
    }
    
    public int getItemIndex(Item item)
    {
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
            if(inventorySlots[i].getItem() != null && inventorySlots[i].getItem().equals(item))
                return i;
        
        return -1;
    }
    
    public <I extends Item> I[] getAllItemTypeInInventory(Class<I> itemClass)
    {
        List<I> itemList = new ArrayList<>();
        
        for(Item item : getItemsInInventory())
        {
            if(itemClass.isInstance(item))
            {
                itemList.add(itemClass.cast(item));
            }
        }
        
        I[] result = (I[])Array.newInstance(itemClass, itemList.size());
        
        return itemList.toArray(result);
    }
    
    public Weapon getEquippedWeapon()
    {
        for(WeaponSlot weaponSlot : WeaponSlot.values())
        {
            Weapon weapon = (Weapon)equippedItemsMap.get(weaponSlot);
            
            if(weapon != null) return weapon;
        }
        
        return null;
    }
    
    public Armour[] getEquippedArmour()
    {
        List<Armour> armourlist = new ArrayList<>();
        
        for(ArmourSlot armourSlot : ArmourSlot.values())
            if(equippedItemsMap.containsKey(armourSlot) && equippedItemsMap.get(armourSlot) != null) armourlist.add((Armour)equippedItemsMap.get(armourSlot));
        
        return armourlist.toArray(new Armour[0]);
    }
    
    public EquippableItem getEquippedItem(EquipSlot slot)
    {
        return equippedItemsMap.get(slot);
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
}