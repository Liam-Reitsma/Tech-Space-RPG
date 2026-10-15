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
    
    public void equipItem(EquippableItem item)
    {
        if(item == null) return;
        
        if(equippedItemsMap.containsKey(item.getEquipSlot()))
        {
            addToInventory(item);
            equippedItemsMap.replace(item.getEquipSlot(), item);
        }
        else
        {
            equippedItemsMap.put(item.getEquipSlot(), item);
        }
    }
    
    public void unequipItem(EquippableItem item)
    {
        addToInventory(equippedItemsMap.get(item.getEquipSlot()));
        equippedItemsMap.remove(item.getEquipSlot());
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
            if(equippedItemsMap.containsKey(armourSlot)) armourlist.add((Armour)equippedItemsMap.get(armourSlot));
        
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