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
    
    public boolean addToInventory(Item item)
    {
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
            if(inventorySlots[i].getItem() != null && inventorySlots[i].getItem().equals(item))
            {
                inventorySlots[i].addItem();
                return true;
            }
            
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
            if(inventorySlots[i].getItem() == null)
            {
                inventorySlots[i].setItem(item);
                return true;
            }
        
        return false;
    }
    
    public boolean removeFromInventory(Item item, boolean removeStack)
    {
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
        {
            if(inventorySlots[i].getItem() != null && inventorySlots[i].getItem().equals(item))
            {
                if(removeStack)
                    inventorySlots[i].removeStack();
                else
                    inventorySlots[i].removeItem();
                
                return true;
            }
        }
        
        return false;
    }
    
    public void removeFromEquipped(EquippableItem item)
    {
        equippedItemsMap.replace(item.getEquipSlot(), null);
    }
    
    public boolean equipItem(int itemIndex, EquipSlot targetEquipSlot)
    {
        if(itemIndex < 0 || itemIndex >= INVENTORY_CAPACITY)
            return false;
        return equipItem(inventorySlots[itemIndex].getItem(), targetEquipSlot);
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
                
                equippedItemsMap.replace(item.getEquipSlot(), item);
                removeFromInventory(item, false);
                
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
                        inventorySlots[originalIndex].setItem(item);
                    else
                        inventorySlots[originalIndex].addItem();
                    
                    return false;
                }
            }
        }
        
        return false;
    }
    
    public boolean unequipItem(EquipSlot equipSlot, int itemIndex)
    {
        if(itemIndex < 0 || itemIndex >= INVENTORY_CAPACITY)
            return false;
        return unequipItem(equippedItemsMap.get(equipSlot), itemIndex);
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
        
        if(addToInventory(item))
        {
            if(getItemCount(item) == 1)
                swapItems(getItemIndex(item), targetIndex);
                
            equippedItemsMap.replace(item.getEquipSlot(), null);
            return true;
        }
        
        return false;
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
    
    public boolean hasOpenItemSlot()
    {
        for(ItemStack itemStack : inventorySlots)
            if(itemStack.getItem() == null)
                return true;
        
        return false;
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
    
    public ItemStack getItemStack(int index) { return inventorySlots[index]; }
    
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