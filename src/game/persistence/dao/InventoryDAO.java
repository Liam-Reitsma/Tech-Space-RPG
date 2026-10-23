package game.persistence.dao;

import static game.persistence.PersistenceTable.*;
import static game.persistence.SQLCommandFactory.*;
import static game.player.model.Inventory.INVENTORY_CAPACITY;

import game.player.model.ItemStack;
import game.player.model.Player;
import game.items.EquippableItem;
import game.items.Item;
import game.items.Potion;
import game.items.PotionType;
import game.items.armour.Armour;
import game.items.armour.ArmourMaterial;
import game.items.armour.ArmourSlot;
import game.items.weapon.Weapon;
import game.items.weapon.WeaponType;
import game.persistence.DBManager;
import game.player.model.ReadInventory;

import java.util.*;

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
    
    public boolean updateEquippedEquipment(String saveName, ReadInventory inventory)
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
                    args[i] = armour.getArmourMaterial().name();
                    break;
                }
            }
        }
        
        args[args.length - 1]  = saveName;
        
        return dbManager.updateDB(buildUpdateTableSQL(EQUIPPED_ITEMS.name(), SAVE_NAME_COLUMN, equippedColumns), args);
    }

    /**
     * Deletes all stored armour, weapons and potions for the specified save.
     * Recreates their database records using the supplied inventory.
     *
     * @param saveName The save whose stored inventory is being updated.
     * @param inventory The inventory containing the items to persist.
     * @return True if every database operation succeeds; otherwise false.
     */
    public boolean updateStoredEquipment(String saveName, ReadInventory inventory)
    {
        if(!dbManager.updateDB(buildDeleteAllRowsSQL(STORED_ARMOUR.name(), SAVE_NAME_COLUMN), saveName))
            return false;
        if(!dbManager.updateDB(buildDeleteAllRowsSQL(STORED_WEAPONS.name(), SAVE_NAME_COLUMN), saveName))
            return false;
        if(!dbManager.updateDB(buildDeleteAllRowsSQL(STORED_POTIONS.name(), SAVE_NAME_COLUMN), saveName))
            return false;
        
        Armour[] armourArray = inventory.getAllItemTypeInInventory(Armour.class);
        for(Armour armour : armourArray)
        {
            Object[] args = new Object[]
                    {
                            saveName, armour.getEquipSlot().name(), armour.getArmourMaterial().name(), inventory.getItemCount(armour), inventory.getItemIndex(armour)
                    };
            
            if(!dbManager.updateDB(buildInsertIntoSQL(ARMOR_TABLE_NAME, ARMOR_COLUMNS), args))
                return false;
        }
        
        Weapon[] weaponsArray = inventory.getAllItemTypeInInventory(Weapon.class);
        for(Weapon weapon : weaponsArray)
        {
            Object[] args = new Object[]
                    {
                            saveName, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel(), inventory.getItemCount(weapon), inventory.getItemIndex(weapon)
                    };
            
            if(!dbManager.updateDB(buildInsertIntoSQL(WEAPON_TABLE_NAME, WEAPON_COLUMNS), args))
                return false;
        }
        
        Potion[] potionsArray = inventory.getAllItemTypeInInventory(Potion.class);
        for(Potion potion : potionsArray)
        {
            Object[] args = new Object[]
                    {
                            saveName, potion.getPotionType().name(), inventory.getItemCount(potion), inventory.getItemIndex(potion)
                    };
            
            if(!dbManager.updateDB(buildInsertIntoSQL(POTION_TABLE_NAME, POTION_COLUMNS), args))
                return false;
        }
        
        return true;
    }
    
    public boolean addItem(String saveName, Item item, int newCount, int slotIndex)
    {
        return updateItemEntry(saveName, item, newCount - 1, newCount, slotIndex);
    }
    
    public boolean removeItem(String saveName, Item item, int newCount, int slotIndex)
    {
        if(item == null || slotIndex < 0 || newCount < 0)
            return false;
        
        return updateItemEntry(saveName, item, newCount + 1, newCount, slotIndex);
    }
    
    private boolean updateItemEntry(String saveName, Item item, int oldCount, int newCount, int slotIndex)
    {
        if(item instanceof Armour armour)
            return updateArmourEntry(saveName, armour, oldCount, newCount, slotIndex);
        else if(item instanceof Weapon weapon)
            return updateWeaponEntry(saveName, weapon, oldCount, newCount, slotIndex);
        else if(item instanceof Potion potion)
            return updatePotionEntry(saveName, potion, oldCount, newCount, slotIndex);
        
        return false;
    }
    
    private boolean updateArmourEntry(String saveName, Armour armour, int oldCount, int newCount, int slotIndex)
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
                    args = new Object[]{saveName, armour.getEquipSlot().name(), armour.getArmourMaterial().name(), newCount, slotIndex};
                    return dbManager.updateDB(buildInsertIntoSQL(ARMOR_TABLE_NAME, ARMOR_COLUMNS), args);
                }
                else
                {
                    args = new Object[]{newCount, saveName, armour.getEquipSlot().name(), armour.getArmourMaterial().name(), slotIndex};
                    return dbManager.updateDB(buildUpdateCellEntrySQL(ARMOR_TABLE_NAME,
                            "COUNT", ARMOR_COLUMNS), args);
                }
            }
            default ->
            {
                args = new Object[]{newCount, saveName, armour.getEquipSlot().name(), armour.getArmourMaterial().name(), slotIndex};
                return dbManager.updateDB(buildUpdateCellEntrySQL(ARMOR_TABLE_NAME,
                        "COUNT", ARMOR_COLUMNS), args);
            }
        }
    }
    
    private boolean updateWeaponEntry(String saveName, Weapon weapon, int oldCount, int newCount, int slotIndex)
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
                    args = new Object[]{saveName, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel(), newCount, slotIndex};
                    return dbManager.updateDB(buildInsertIntoSQL(WEAPON_TABLE_NAME, WEAPON_COLUMNS), args);}
                else
                {
                    args = new Object[]{newCount, saveName, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel(), slotIndex};
                    return dbManager.updateDB(buildUpdateCellEntrySQL(
                            WEAPON_TABLE_NAME, "COUNT", WEAPON_COLUMNS),args);
                }
            }
            default ->
            {
                args = new Object[]{newCount, saveName, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel(), slotIndex};
                return dbManager.updateDB(buildUpdateCellEntrySQL(WEAPON_TABLE_NAME,
                        "COUNT", WEAPON_COLUMNS), args);
            }
        }
    }
    
    private boolean updatePotionEntry(String saveName, Potion potion, int oldCount, int newCount, int slotIndex)
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
                    args = new Object[]{saveName, potion.getPotionType().name(), newCount, slotIndex};
                    return dbManager.updateDB(buildInsertIntoSQL(POTION_TABLE_NAME, POTION_COLUMNS), args);
                }
                else
                {
                    args = new Object[]{newCount, saveName, potion.getPotionType().name(), slotIndex};
                    return dbManager.updateDB(buildUpdateCellEntrySQL(POTION_TABLE_NAME,
                            "COUNT", POTION_COLUMNS), args);
                }
            }
            default ->
            {
                args = new Object[]{newCount, saveName, potion.getPotionType().name(), slotIndex};
                return dbManager.updateDB(buildUpdateCellEntrySQL(POTION_TABLE_NAME,
                        "COUNT", POTION_COLUMNS), args);
            }
        }
    }

    /**
     * Retrieves all the player's equipped items.
     *
     * @param saveName The save whose equipped items are being loaded.
     * @return An array of equipped items, an empty array if no record exists, or null if the query fails.
     */
    public EquippableItem[] loadEquippedItems(String saveName)
    {
        List<Map<String, Object>> itemDataList = dbManager.queryDB(buildSelectSQL(EQUIPPED_TABLE_NAME, SAVE_NAME_COLUMN), saveName);
        List<EquippableItem> equippableItems = new ArrayList<>();
        if(itemDataList == null)
            return null;
        if(itemDataList.isEmpty())
            return new EquippableItem[0];
        
        Map<String, Object> itemData = itemDataList.getFirst();
        
        equippableItems.add(new Weapon(WeaponType.valueOf(itemData.get("WEAPON_TYPE").toString()), (int)itemData.get("WEAPON_LEVEL")));
        if(itemData.get("HEAD") != null)
            equippableItems.add(new Armour(ArmourMaterial.valueOf(itemData.get("HEAD").toString()), ArmourSlot.HEAD));
        if(itemData.get("CHEST") != null)
            equippableItems.add(new Armour(ArmourMaterial.valueOf(itemData.get("CHEST").toString()), ArmourSlot.CHEST));
        if(itemData.get("ARMS") != null)
            equippableItems.add(new Armour(ArmourMaterial.valueOf(itemData.get("ARMS").toString()), ArmourSlot.ARMS));
        if(itemData.get("THIGHS") != null)
            equippableItems.add(new Armour(ArmourMaterial.valueOf(itemData.get("THIGHS").toString()), ArmourSlot.THIGHS));
        if(itemData.get("SHINS") != null)
            equippableItems.add(new Armour(ArmourMaterial.valueOf(itemData.get("SHINS").toString()), ArmourSlot.SHINS));
        if(itemData.get("BOOTS") != null)
            equippableItems.add(new Armour(ArmourMaterial.valueOf(itemData.get("BOOTS").toString()), ArmourSlot.BOOTS));
        
        return equippableItems.toArray(new EquippableItem[0]);
    }

    /**
     * Reconstructs the inventory, restoring items to their original slots.
     * Unoccupied slots are initialised with empty ItemStack objects.
     *
     * @param saveName The save whose inventory is being loaded.
     * @return A fixed-capacity array of restored item stacks, or null if a database query fails.
     */
    public ItemStack[] loadInventory(String saveName)
    {
        List<Map<String, Object>> storedWeaponList = dbManager.queryDB(buildSelectSQL(WEAPON_TABLE_NAME, SAVE_NAME_COLUMN), saveName);
        List<Map<String, Object>> storedArmourList = dbManager.queryDB(buildSelectSQL(ARMOR_TABLE_NAME, SAVE_NAME_COLUMN), saveName);
        List<Map<String, Object>> storedPotionList = dbManager.queryDB(buildSelectSQL(POTION_TABLE_NAME, SAVE_NAME_COLUMN), saveName);
        ItemStack[] itemStacks = new ItemStack[INVENTORY_CAPACITY];
        if(storedWeaponList == null || storedArmourList == null || storedPotionList == null)
            return null;
        
        for(Map<String, Object> storedWeapon : storedWeaponList)
        {
            Weapon weapon = new Weapon(WeaponType.valueOf(storedWeapon.get("WEAPON_TYPE").toString()), (int)storedWeapon.get("LEVEL"));
            itemStacks[(int)storedWeapon.get("SLOT_INDEX")] = new ItemStack(weapon, (int)storedWeapon.get("COUNT"));
        }
        for(Map<String, Object> storedArmour : storedArmourList)
        {
            Armour armour = new Armour(ArmourMaterial.valueOf(storedArmour.get("ARMOUR_MATERIAL").toString()), ArmourSlot.valueOf(storedArmour.get("ARMOUR_SLOT").toString()));
            itemStacks[(int)storedArmour.get("SLOT_INDEX")] = new ItemStack(armour, (int)storedArmour.get("COUNT"));
        }
        for(Map<String, Object> storedPotion : storedPotionList)
        {
            Potion potion = new Potion(PotionType.valueOf(storedPotion.get("POTION_TYPE").toString()));
            itemStacks[(int)storedPotion.get("SLOT_INDEX")] = new ItemStack(potion, (int)storedPotion.get("COUNT"));
        }
        
        for(int i = 0; i < INVENTORY_CAPACITY; i++)
            if(itemStacks[i] == null)
                itemStacks[i] = new ItemStack();
        
        return itemStacks;
    }
    
    public boolean deleteEquipped(String saveName)
    {
        return dbManager.updateDB(buildDeleteRowSQL(EQUIPPED_TABLE_NAME, new String[]{SAVE_NAME_COLUMN}), saveName);
    }
    
    public boolean deleteInventory(String saveName)
    {
        boolean success = dbManager.updateDB(buildDeleteRowSQL(ARMOR_TABLE_NAME, new String[]{SAVE_NAME_COLUMN}), saveName);
        success = dbManager.updateDB(buildDeleteRowSQL(WEAPON_TABLE_NAME, new String[]{SAVE_NAME_COLUMN}), saveName) && success;
        success = dbManager.updateDB(buildDeleteRowSQL(POTION_TABLE_NAME, new String[]{SAVE_NAME_COLUMN}), saveName) && success;
        
        return success;
    }
}