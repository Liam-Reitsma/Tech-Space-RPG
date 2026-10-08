package game.entities.player;

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
    private final Map<Item, Integer> storedItemsMap;
    private final Map<EquipSlot, EquippableItem> equippedItemsMap;
    
    public Inventory()
    {
        storedItemsMap = new HashMap<>();
        equippedItemsMap = new HashMap<>();
    }
    
    public void addToInventory(Item item)
    {
        storedItemsMap.put(item, storedItemsMap.getOrDefault(item, 0) + 1);
    }
    
    public void removeFromInventory(Item item, boolean removeStack)
    {
        if(removeStack) storedItemsMap.remove(item);
        else storedItemsMap.put(item, storedItemsMap.getOrDefault(item, 0) - 1);
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
        {
            if(equippedItemsMap.containsKey(armourSlot)) armourlist.add((Armour)equippedItemsMap.get(armourSlot));
        }
        
        return armourlist.toArray(new Armour[0]);
    }
    
    public Item[] getItemsInInventory() { return storedItemsMap.keySet().toArray(new Item[0]); }
    
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
    
    public int getItemCount(Item item) { return storedItemsMap.get(item); }
    
    public void setInventory(Map<Item, Integer> loadedStoredMap) { storedItemsMap.putAll(loadedStoredMap); }
    
    public void setEquipped(EquippableItem[] loadedEquippedArray)
    {
        for(EquippableItem loadedEquippedItem : loadedEquippedArray)
            equippedItemsMap.put(loadedEquippedItem.getEquipSlot(), loadedEquippedItem);
    }
}