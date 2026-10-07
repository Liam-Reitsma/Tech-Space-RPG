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
    
    public void insertEquippedItems(String saveName, Player player)
    {
        Object[] args = new Object[]
                {
                        saveName, player.getClassType().getStartingWeapon().name(), player.getLevel(), null, null, null, null, null, null
                };
        
        dbManager.addEntryDB(buildInsertIntoSQL(EQUIPPED_TABLE_NAME, EQUIPPED_COLUMNS), args);
    }
    
    public void updateEquippedEquipment(String saveName, Inventory inventory)
    {
        String[] equippedColumns = Arrays.copyOfRange(EQUIPPED_COLUMNS, 1, EQUIPPED_COLUMNS.length);
        Object[] args = new Object[equippedColumns.length];
        args[0] = inventory.getEquippedWeapon().getWeaponType().name();
        args[1] = inventory.getEquippedWeapon().getLevel();
        
        for(int i = 2; i < args.length; i++)
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
        
        dbManager.updateEntryDB(buildUpdateTableSQL(EQUIPPED_ITEMS.name(), SAVE_NAME_COLUMN, equippedColumns), saveName, args);
    }
    
    public void updateStoredEquipment(String saveName, Inventory inventory)
    {
        dbManager.deleteEntryDB(buildDeleteAllRowSQL(STORED_ARMOUR.name(), SAVE_NAME_COLUMN), saveName);
        dbManager.deleteEntryDB(buildDeleteAllRowSQL(STORED_WEAPONS.name(), SAVE_NAME_COLUMN), saveName);
        dbManager.deleteEntryDB(buildDeleteAllRowSQL(STORED_POTIONS.name(), SAVE_NAME_COLUMN), saveName);
        
        Armour[] armourArray = inventory.getAllItemTypeInInventory(Armour.class);
        for(Armour armour : armourArray)
        {
            Object[] args = new Object[]
                    {
                            saveName, armour.getEquipSlot().name(), armour.getArmourMaterial().name(), inventory.getItemCount(armour)
                    };
            
            dbManager.addEntryDB(buildInsertIntoSQL(ARMOR_TABLE_NAME, ARMOR_COLUMNS), args);
        }
        
        Weapon[] weaponsArray = inventory.getAllItemTypeInInventory(Weapon.class);
        for(Weapon weapon : weaponsArray)
        {
            Object[] args = new Object[]
                    {
                            saveName, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel(), inventory.getItemCount(weapon)
                    };
            
            dbManager.addEntryDB(buildInsertIntoSQL(WEAPON_TABLE_NAME, WEAPON_COLUMNS), args);
        }
        
        Potion[] potionsArray = inventory.getAllItemTypeInInventory(Potion.class);
        for(Potion potion : potionsArray)
        {
            Object[] args = new Object[]
                    {
                            saveName, potion.getPotionType().name(), inventory.getItemCount(potion)
                    };
            
            dbManager.addEntryDB(buildInsertIntoSQL(POTION_TABLE_NAME, POTION_COLUMNS), args);
        }
    }
    
    public void addItem(String saveName, Item item, int newCount)
    {
        updateItemEntry(saveName, item, newCount - 1, newCount);
    }
    
    public void removeItem(String saveName, Item item, int newCount)
    {
        updateItemEntry(saveName, item, newCount + 1, newCount);
    }
    
    private void updateItemEntry(String saveName, Item item, int oldCount, int newCount)
    {
        Object[] args;
        if(item instanceof Armour armour)
        {
            if(newCount == 0)
            {
                args = new Object[]{ saveName, armour.getEquipSlot().name(), armour.getArmourMaterial().name() };
                dbManager.deleteSingleEntryDB(buildDeleteRowSQL(
                        ARMOR_TABLE_NAME, STORED_ARMOUR.getPrimaryKeys()), args);
            }
            else if(newCount == 1)
            {
                if(newCount > oldCount)
                {
                    args = new Object[]{saveName, armour.getEquipSlot().name(), armour.getArmourMaterial().name(), newCount };
                    dbManager.addEntryDB(buildInsertIntoSQL(ARMOR_TABLE_NAME, ARMOR_COLUMNS), args);
                }
                else
                {
                    args = new Object[]{saveName, armour.getEquipSlot().name(), armour.getArmourMaterial().name()};
                    dbManager.updateCellEntryDB(buildUpdateRowSQL(ARMOR_TABLE_NAME,
                                    ARMOR_COLUMNS[ARMOR_COLUMNS.length - 1], ARMOR_COLUMNS), newCount, args);
                }
            }
            else
            {
                args = new Object[]{ saveName, armour.getEquipSlot().name(), armour.getArmourMaterial().name() };
                dbManager.updateCellEntryDB(buildUpdateRowSQL(ARMOR_TABLE_NAME,
                                ARMOR_COLUMNS[ARMOR_COLUMNS.length - 1], ARMOR_COLUMNS), newCount, args);
            }
        }
        else if(item instanceof Weapon weapon)
        {
            if(newCount == 0)
            {
                args = new Object[]{saveName, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel()};
                dbManager.deleteSingleEntryDB(buildDeleteRowSQL(
                        WEAPON_TABLE_NAME, STORED_WEAPONS.getPrimaryKeys()), args);
            }
            else if(newCount == 1)
            {
                if(newCount > oldCount)
                {
                    args = new Object[]{saveName, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel(), newCount };
                    dbManager.addEntryDB(buildInsertIntoSQL(WEAPON_TABLE_NAME, WEAPON_COLUMNS), args);
                }
                else
                {
                    args = new Object[]{saveName, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel()};
                    dbManager.updateCellEntryDB(buildUpdateRowSQL(
                            WEAPON_TABLE_NAME, WEAPON_COLUMNS[WEAPON_COLUMNS.length - 1], WEAPON_COLUMNS),
                            newCount, args);
                }
            }
            else
            {
                args = new Object[]{ saveName, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel() };
                dbManager.updateCellEntryDB(buildUpdateRowSQL(WEAPON_TABLE_NAME,
                        WEAPON_COLUMNS[WEAPON_COLUMNS.length - 1], WEAPON_COLUMNS), newCount, args);
            }
        }
        else if(item instanceof Potion potion)
        {
            if(newCount == 0)
            {
                args = new Object[]{ saveName, potion.getPotionType().name() };
                dbManager.deleteSingleEntryDB(buildDeleteRowSQL(
                        POTION_TABLE_NAME, STORED_POTIONS.getPrimaryKeys()), args);
            }
            else if(newCount == 1)
            {
                if(newCount > oldCount)
                {
                    args = new Object[]{saveName, potion.getPotionType().name(), newCount};
                    dbManager.addEntryDB(buildInsertIntoSQL(POTION_TABLE_NAME, POTION_COLUMNS), args);
                }
                else
                {
                    args = new Object[]{saveName, potion.getPotionType().name()};
                    dbManager.updateCellEntryDB(buildUpdateRowSQL(POTION_TABLE_NAME,
                            POTION_COLUMNS[POTION_COLUMNS.length - 1], POTION_COLUMNS), newCount, args);
                }
            }
            else
            {
                args = new Object[]{ saveName, potion.getPotionType().name() };
                dbManager.updateCellEntryDB(buildUpdateRowSQL(POTION_TABLE_NAME,
                        POTION_COLUMNS[POTION_COLUMNS.length - 1], POTION_COLUMNS), newCount, args);
            }
        }
    }
    
    public EquippableItem[] loadEquippedItems(String saveName)
    {
        //TODO: Replace type/string-parsing based loading for Weapon, Armour, WeaponType, ArmourType, etc. with explicit column-name mapping so each saved
        //      field is reconstructed from its intended database column.
        
        List<EquippableItem> equippableItems = new ArrayList<>();
        
        WeaponType weaponType = WeaponType.RUSTED_KNIFE;
        int weaponLevel = 0;
        for(Object datum : dbManager.queryDB(buildSelectSQL(EQUIPPED_ITEMS.name(), SAVE_NAME_COLUMN), saveName))
        {
            if(datum == null)
                continue;
            if(datum instanceof Integer level)
                weaponLevel = level;
            else if(WeaponType.parseWeaponType((String)datum) instanceof WeaponType type)
                weaponType = type;
            else
                equippableItems.add(new Armour((String)datum));
        }
        
        equippableItems.add(new Weapon(weaponType, weaponLevel));
        
        return equippableItems.toArray(new EquippableItem[0]);
    }
}