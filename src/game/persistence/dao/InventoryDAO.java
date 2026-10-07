package game.persistence.dao;

import static game.persistence.PersistenceTable.*;
import static game.persistence.SQLCommandFactory.*;

import game.entities.player.Inventory;
import game.entities.player.Player;
import game.items.EquippableItem;
import game.items.Item;
import game.items.Potion;
import game.items.armour.Armour;
import game.items.weapon.Weapon;
import game.items.weapon.WeaponType;
import game.persistence.DBManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InventoryDAO
{
    private static final String SAVE_NAME_COLUMN = "SAVE_NAME";
    private static final String ARMOR_TABLE_NAME = STORED_ARMOUR.name();
    private static final String WEAPON_TABLE_NAME = STORED_WEAPONS.name();
    private static final String POTION_TABLE_NAME = STORED_POTIONS.name();
    private static final String EQUIPPED_TABLE_NAME = EQUIPPED_ITEMS.name();
    private static final String[] ARMOR_COLUMNS = STORED_ARMOUR.getColumnStrings();
    private static final String[] WEAPON_COLUMNS = STORED_WEAPONS.getColumnStrings();
    private static final String[] POTION_COLUMNS = STORED_POTIONS.getColumnStrings();
    private static final String[] EQUIPPED_COLUMNS = EQUIPPED_ITEMS.getColumnStrings();
    
    private final DBManager dbManager;
    
    public InventoryDAO(DBManager db)
    {
        dbManager = db;
    }
    
    public boolean insertEquippedItems(String saveName, Player player)
    {
        Object[] args = new Object[]
                {
                        saveName, player.getClassType().getStartingWeapon().name(), player.getLevel(), null, null, null, null, null, null
                };
        
        return dbManager.updateDB(buildInsertIntoSQL(EQUIPPED_TABLE_NAME, EQUIPPED_COLUMNS), args);
    }
    
    public boolean updateEquippedEquipment(String saveName, Inventory inventory)
    {
        String[] equippedColumns = Arrays.copyOfRange(EQUIPPED_COLUMNS, 1, EQUIPPED_COLUMNS.length);
        Object[] args = new Object[equippedColumns.length + 1];
        args[0] = inventory.getEquippedWeapon().getWeaponType().name();
        args[1] = inventory.getEquippedWeapon().getLevel();
        
        for(int i = 2; i < equippedColumns.length; i++)
        {
            for(Armour armour : inventory.getEquippedArmour())
            {
                if(armour.getEquipSlot().name().equalsIgnoreCase(equippedColumns[i]))
                {
                    args[i] = armour.toString();
                    break;
                }
            }
        }
        
        args[args.length - 1]  = saveName;
        
        return dbManager.updateDB(buildUpdateTableSQL(EQUIPPED_ITEMS.name(), SAVE_NAME_COLUMN, equippedColumns), args);
    }
    
    public boolean updateStoredEquipment(String saveName, Inventory inventory)
    {
        if(!dbManager.updateDB(buildDeleteAllRowSQL(STORED_ARMOUR.name(), SAVE_NAME_COLUMN), saveName))
            return false;
        if(!dbManager.updateDB(buildDeleteAllRowSQL(STORED_WEAPONS.name(), SAVE_NAME_COLUMN), saveName))
            return false;
        if(!dbManager.updateDB(buildDeleteAllRowSQL(STORED_POTIONS.name(), SAVE_NAME_COLUMN), saveName))
            return false;
        
        Armour[] armourArray = inventory.getAllItemTypeInInventory(Armour.class);
        for(Armour armour : armourArray)
        {
            Object[] args = new Object[]
                    {
                            saveName, armour.getEquipSlot().name(), armour.getArmourMaterial().name(), inventory.getItemCount(armour)
                    };
            
            if(!dbManager.updateDB(buildInsertIntoSQL(ARMOR_TABLE_NAME, ARMOR_COLUMNS), args))
                return false;
        }
        
        Weapon[] weaponsArray = inventory.getAllItemTypeInInventory(Weapon.class);
        for(Weapon weapon : weaponsArray)
        {
            Object[] args = new Object[]
                    {
                            saveName, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel(), inventory.getItemCount(weapon)
                    };
            
            if(!dbManager.updateDB(buildInsertIntoSQL(WEAPON_TABLE_NAME, WEAPON_COLUMNS), args))
                return false;
        }
        
        Potion[] potionsArray = inventory.getAllItemTypeInInventory(Potion.class);
        for(Potion potion : potionsArray)
        {
            Object[] args = new Object[]
                    {
                            saveName, potion.getPotionType().name(), inventory.getItemCount(potion)
                    };
            
            if(!dbManager.updateDB(buildInsertIntoSQL(POTION_TABLE_NAME, POTION_COLUMNS), args))
                return false;
        }
        
        return true;
    }
    
    public boolean addItem(String saveName, Item item, int newCount)
    {
        return updateItemEntry(saveName, item, newCount - 1, newCount);
    }
    
    public boolean removeItem(String saveName, Item item, int newCount)
    {
        return updateItemEntry(saveName, item, newCount + 1, newCount);
    }
    
    private boolean updateItemEntry(String saveName, Item item, int oldCount, int newCount)
    {
        if(item instanceof Armour armour)
            return updateArmourEntry(saveName, armour, oldCount, newCount);
        else if(item instanceof Weapon weapon)
            return updateWeaponEntry(saveName, weapon, oldCount, newCount);
        else if(item instanceof Potion potion)
            return updatePotionEntry(saveName, potion, oldCount, newCount);
        
        return false;
    }
    
    private boolean updateArmourEntry(String saveName, Armour armour, int oldCount, int newCount)
    {
        Object[] args;
        switch(newCount)
        {
            case 0 ->
            {
                args = new Object[]{saveName, armour.getEquipSlot().name(), armour.getArmourMaterial().name()};
                return dbManager.updateDB(buildDeleteRowSQL(
                        ARMOR_TABLE_NAME, STORED_ARMOUR.getPrimaryKeys()), args);
            }
            case 1 ->
            {
                if(newCount > oldCount)
                {
                    args = new Object[]{saveName, armour.getEquipSlot().name(), armour.getArmourMaterial().name(), newCount};
                    return dbManager.updateDB(buildInsertIntoSQL(ARMOR_TABLE_NAME, ARMOR_COLUMNS), args);
                }
                else
                {
                    args = new Object[]{newCount, saveName, armour.getEquipSlot().name(), armour.getArmourMaterial().name()};
                    return dbManager.updateDB(buildUpdateRowSQL(ARMOR_TABLE_NAME,
                            ARMOR_COLUMNS[ARMOR_COLUMNS.length - 1], ARMOR_COLUMNS), args);
                }
            }
            default ->
            {
                args = new Object[]{newCount, saveName, armour.getEquipSlot().name(), armour.getArmourMaterial().name()};
                return dbManager.updateDB(buildUpdateRowSQL(ARMOR_TABLE_NAME,
                        ARMOR_COLUMNS[ARMOR_COLUMNS.length - 1], ARMOR_COLUMNS), args);
            }
        }
    }
    
    private boolean updateWeaponEntry(String saveName, Weapon weapon, int oldCount, int newCount)
    {
        Object[] args;
        switch(newCount)
        {
            case 0 ->
            {
                args = new Object[]{saveName, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel()};
                return dbManager.updateDB(buildDeleteRowSQL(
                        WEAPON_TABLE_NAME, STORED_WEAPONS.getPrimaryKeys()), args);
            }
            case 1 ->
            {
                if(newCount > oldCount)
                {
                    args = new Object[]{saveName, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel(), newCount};
                    return dbManager.updateDB(buildInsertIntoSQL(WEAPON_TABLE_NAME, WEAPON_COLUMNS), args);}
                else
                {
                    args = new Object[]{newCount, saveName, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel()};
                    return dbManager.updateDB(buildUpdateRowSQL(
                            WEAPON_TABLE_NAME, WEAPON_COLUMNS[WEAPON_COLUMNS.length - 1], WEAPON_COLUMNS),args);
                }
            }
            default ->
            {
                args = new Object[]{newCount, saveName, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel()};
                return dbManager.updateDB(buildUpdateRowSQL(WEAPON_TABLE_NAME,
                        WEAPON_COLUMNS[WEAPON_COLUMNS.length - 1], WEAPON_COLUMNS), args);
            }
        }
    }
    
    private boolean updatePotionEntry(String saveName, Potion potion, int oldCount, int newCount)
    {
        Object[] args;
        switch(newCount)
        {
            case 0 ->
            {
                args = new Object[]{saveName, potion.getPotionType().name()};
                return dbManager.updateDB(buildDeleteRowSQL(
                        POTION_TABLE_NAME, STORED_POTIONS.getPrimaryKeys()), args);
            }
            case 1 ->
            {
                if(newCount > oldCount)
                {
                    args = new Object[]{saveName, potion.getPotionType().name(), newCount};
                    return dbManager.updateDB(buildInsertIntoSQL(POTION_TABLE_NAME, POTION_COLUMNS), args);
                }
                else
                {
                    args = new Object[]{newCount, saveName, potion.getPotionType().name()};
                    return dbManager.updateDB(buildUpdateRowSQL(POTION_TABLE_NAME,
                            POTION_COLUMNS[POTION_COLUMNS.length - 1], POTION_COLUMNS), args);
                }
            }
            default ->
            {
                args = new Object[]{newCount, saveName, potion.getPotionType().name()};
                return dbManager.updateDB(buildUpdateRowSQL(POTION_TABLE_NAME,
                        POTION_COLUMNS[POTION_COLUMNS.length - 1], POTION_COLUMNS), args);
            }
        }
    }
    
    public EquippableItem[] loadEquippedItems(String saveName)
    {
        List<Map<String, Object>> itemDataList = dbManager.queryDB(buildSelectSQL(EQUIPPED_ITEMS.name(), SAVE_NAME_COLUMN), saveName);
        List<EquippableItem> equippableItems = new ArrayList<>();
        if(itemDataList == null)
            return null;
        if(itemDataList.isEmpty())
            return new EquippableItem[0];
        
        Map<String, Object> itemData = itemDataList.getFirst();
        
        equippableItems.add(new Weapon(WeaponType.valueOf(itemData.get("WEAPON_TYPE").toString()), (int)itemData.get("WEAPON_LEVEL")));
        if(itemData.get("HEAD") != null)
            equippableItems.add(new Armour(itemData.get("HEAD").toString()));
        if(itemData.get("CHEST") != null)
            equippableItems.add(new Armour(itemData.get("CHEST").toString()));
        if(itemData.get("ARMS") != null)
            equippableItems.add(new Armour(itemData.get("ARMS").toString()));
        if(itemData.get("THIGHS") != null)
            equippableItems.add(new Armour(itemData.get("THIGHS").toString()));
        if(itemData.get("SHINS") != null)
            equippableItems.add(new Armour(itemData.get("SHINS").toString()));
        if(itemData.get("BOOTS") != null)
            equippableItems.add(new Armour(itemData.get("BOOTS").toString()));
        
        return equippableItems.toArray(new EquippableItem[0]);
    }
}