package game.persistence;

import static game.persistence.PersistenceTable.*;
import static game.persistence.SQLCommandFactory.*;

import game.entities.player.ClassType;
import game.entities.player.Inventory;
import game.entities.player.Player;
import game.items.*;
import game.items.armour.Armour;
import game.items.weapon.Weapon;
import game.items.weapon.WeaponType;
import game.world.Point;
import game.world.room.RuntimeRoom;

import java.util.*;
import java.util.Map.Entry;

public class PersistenceService
{
    private String iKeyCurrent;
    private String iKeyColumnName;
    
    private final DBManager dbManager;
    
    public PersistenceService(DBManager dbm)
    {
        dbManager = dbm;
    }
    
    public void createNewGame(String saveName, String charName, ClassType classType, Map<Point, RuntimeRoom> world)
    {
        iKeyCurrent = saveName;
        iKeyColumnName = "SAVE_NAME";
        Player player = new Player(charName, classType);
        createTables();
        insertPlayer(player);
        insertEquippedItems(player);
        insertWorld(world);
    }
    
    private void createTables()
    {
        PersistenceTable table = PLAYER;
        LinkedHashMap<String, String> columns = table.getColumns();
        dbManager.createTable(table.name(),
                buildCreateTableSQL(table.name(), columns, new String[]{iKeyColumnName},
                        columns.keySet().toArray(new String[0])));
        
        table = PLAYER_STATS;
        columns = table.getColumns();
        dbManager.createTable(table.name(),
                buildCreateTableSQL(table.name(), columns, new String[]{iKeyColumnName},
                        columns.keySet().toArray(new String[0]),
                        new String[]{PLAYER.name()},new String[]{iKeyColumnName}));
        
        table = EQUIPPED_ITEMS;
        columns = table.getColumns();
        dbManager.createTable(table.name(),
                buildCreateTableSQL(table.name(), columns, new String[]{iKeyColumnName},
                        new String[]{iKeyColumnName},
                        new String[]{PLAYER.name()},new String[]{iKeyColumnName}));
        
        table = STORED_WEAPONS;
        columns = table.getColumns();
        dbManager.createTable(table.name(),
                buildCreateTableSQL(table.name(), columns, new String[]{iKeyColumnName, "WEAPON_SLOT", "WEAPON_TYPE"},
                        columns.keySet().toArray(new String[0]),
                        new String[]{PLAYER.name()},new String[]{iKeyColumnName}));
        
        table = STORED_ARMOUR;
        columns = table.getColumns();
        dbManager.createTable(table.name(),
                buildCreateTableSQL(table.name(), columns, new String[]{iKeyColumnName, "ARMOUR_SLOT", "ARMOUR_MATERIAL"},
                        columns.keySet().toArray(new String[0]),
                        new String[]{PLAYER.name()},new String[]{iKeyColumnName}));
        
        table = STORED_POTIONS;
        columns = table.getColumns();
        dbManager.createTable(table.name(),
                buildCreateTableSQL(table.name(), columns, new String[]{iKeyColumnName, "POTION_TYPE"},
                        columns.keySet().toArray(new String[0]),
                        new String[]{PLAYER.name()},new String[]{iKeyColumnName}));
        
        table = ROOMS;
        columns = table.getColumns();
        dbManager.createTable(table.name(),
                buildCreateTableSQL(table.name(), columns, new String[]{iKeyColumnName, "X_POINT", "Y_POINT"},
                        columns.keySet().toArray(new String[0]),
                        new String[]{PLAYER.name()},new String[]{iKeyColumnName}));
    }
    
    private void insertPlayer(Player player)
    {
        String[] playerColumns = PLAYER.getColumns().keySet().toArray(new String[0]);
        
        Object[] playerArgs = new Object[]
                        {
                            iKeyCurrent, player.getName(), player.getLevel(),
                            player.getHealth(), player.getXP(), player.getGold(),
                            player.getClassType().toString(), player.getPoint().getX(), player.getPoint().getY()
                        };
        
        dbManager.addEntryDB(buildInsertIntoSQL(PLAYER.name(), playerColumns), playerArgs);
        
        String[] statColumns = PLAYER_STATS.getColumns().keySet().toArray(new String[0]);
        
        Object[] statArgs = new Object[]
                {
                        iKeyCurrent, player.getSTR(), player.getDEX(), player.getINT()
                };
        
        dbManager.addEntryDB(buildInsertIntoSQL(PLAYER_STATS.name(), statColumns), statArgs);
    }
    
    private void insertEquippedItems(Player player)
    {
        String tableName = EQUIPPED_ITEMS.name();
        
        LinkedHashMap<String, String> equippedItemsColumns = EQUIPPED_ITEMS.getColumns();
        
        String[] columns = equippedItemsColumns.keySet().toArray(new String[0]);
        
        Object[] args = new Object[]
                {
                        iKeyCurrent, player.getClassType().getStartingWeapon().toString(), player.getLevel(), null, null, null, null, null, null
                };
        
        dbManager.addEntryDB(buildInsertIntoSQL(tableName, columns), args);
    }
    
    private void insertWorld(Map<Point, RuntimeRoom> world)
    {
        String tableName = ROOMS.name();
        
        LinkedHashMap<String, String> roomsColumns = ROOMS.getColumns();
        
        String[] columns = roomsColumns.keySet().toArray(new String[0]);
        
        for(Entry<Point, RuntimeRoom> worldEntry : world.entrySet())
        {
            Point pointEntry = worldEntry.getKey();
            RuntimeRoom roomEntry = worldEntry.getValue();
            Object[] args = new Object[]
                    {
                            iKeyCurrent, pointEntry.getX(), pointEntry.getY(), roomEntry.getRoomState().toString(), roomEntry.isCompleted()
                    };
            
            dbManager.addEntryDB(buildInsertIntoSQL(tableName, columns), args);
        }
    }
    
    public String[] getSaveOptionNames()
    {
        return dbManager.getSavesName();
    }
    
    public Player loadSavedPlayer(String saveName)
    {
        iKeyCurrent = saveName;
        iKeyColumnName = "SAVE_NAME";
        //TODO: Replace Player Object[] loading with column-name-based mapping (e.g. Map<String, Object>) so loading does not depend on database column order.
        Object[] playerData = dbManager.queryDB(buildSelectSQL(PLAYER.name(), iKeyColumnName), saveName);
        Object[] statsData = dbManager.queryDB(buildSelectSQL(PLAYER_STATS.name(), iKeyColumnName), saveName);
        Player player = new Player(playerData, statsData);
        
        //TODO: Replace type/string-parsing based loading for Weapon, Armour, WeaponType, ArmourType, etc. with explicit column-name mapping so each saved
        //      field is reconstructed from its intended database column.
        WeaponType weaponType = WeaponType.RUSTED_KNIFE;
        int weaponLevel = 0;
        for(Object datum : dbManager.queryDB(buildSelectSQL(EQUIPPED_ITEMS.name(), iKeyColumnName), saveName))
        {
            if(datum instanceof Integer level)
            {
                weaponLevel = level;
            }
            else if(WeaponType.parseWeaponType((String)datum) instanceof WeaponType type)
            {
                weaponType = type;
            }
            else
            {
                player.equipItem(new Armour((String)datum));
            }
        }
        
        player.equipItem(new Weapon(weaponType, weaponLevel));
        return player;
    }
    
    public RuntimeRoom[] loadSavedWorld()
    {
        Object[][] loadedRoomsList = dbManager.queryRoomDB(buildSelectSQL(ROOMS.name(), iKeyColumnName), iKeyCurrent);
        RuntimeRoom[] roomArray = new RuntimeRoom[loadedRoomsList.length];
        int i = 0;
        for(Object[] loadedRoom : loadedRoomsList)
        {
            roomArray[i++] = new RuntimeRoom(loadedRoom);
        }
        
        return roomArray;
    }
    
    public void fullSave(Player player, Map<Point, RuntimeRoom> map)
    {
        if(iKeyCurrent == null) return;
        updatePlayer(player);
        updateEquippedEquipment(player.getInventory());
        updateStoredEquipment(player.getInventory());
        updateMap(map);
    }
    
    private void updatePlayer(Player player)
    {
        String[] allColumns = PLAYER.getColumns().keySet().toArray(new String[0]);
        
        Object[] args = new Object[]
                {
                        player.getName(), player.getLevel(),
                        player.getHealth(), player.getXP(), player.getGold(),
                        player.getClassType().toString(), player.getPoint().getX(), player.getPoint().getY()
                };
        dbManager.updateEntryDB(buildUpdateTableSQL(PLAYER.name(), iKeyColumnName, Arrays.copyOfRange(allColumns, 1, allColumns.length)), iKeyCurrent, args);
        
        allColumns = PLAYER_STATS.getColumns().keySet().toArray(new String[0]);
        
        args = new Object[]
                {
                        player.getSTR(), player.getDEX(), player.getINT()
                };
        
        dbManager.updateEntryDB(buildUpdateTableSQL(PLAYER_STATS.name(), iKeyColumnName, Arrays.copyOfRange(allColumns, 1, allColumns.length)), iKeyCurrent, args);
    }
    
    private void updateEquippedEquipment(Inventory inventory)
    {
        String[] allColumns = EQUIPPED_ITEMS.getColumns().keySet().toArray(new String[0]);
        
        String[] columns = Arrays.copyOfRange(allColumns, 1, allColumns.length);
        
        Object[] args = new Object[columns.length];
        
        args[0] = inventory.getEquippedWeapon();
        
        for(int i = 1; i < args.length; i++)
        {
            for(Armour armour : inventory.getEquippedArmour())
            {
                if(armour.getEquipSlot().toString().equalsIgnoreCase(columns[i]))
                {
                    args[i] = armour.toString();
                    break;
                }
            }
        }
        
        dbManager.updateEntryDB(buildUpdateTableSQL(EQUIPPED_ITEMS.name(), iKeyColumnName, columns), iKeyCurrent, args);
    }
    
    private void updateStoredEquipment(Inventory inventory)
    {
        dbManager.deleteEntryDB(buildDeleteAllRowSQL(STORED_ARMOUR.name(), iKeyColumnName), iKeyCurrent);
        dbManager.deleteEntryDB(buildDeleteAllRowSQL(STORED_WEAPONS.name(), iKeyColumnName), iKeyCurrent);
        dbManager.deleteEntryDB(buildDeleteAllRowSQL(STORED_POTIONS.name(), iKeyColumnName), iKeyCurrent);
        
        String tableName = STORED_ARMOUR.name();
        LinkedHashMap<String, String> itemsColumns = STORED_ARMOUR.getColumns();
        String[] columns = itemsColumns.keySet().toArray(new String[0]);
        
        Armour[] armourArray = inventory.getAllItemTypeInInventory(Armour.class);
        for(Armour armour : armourArray)
        {
            Object[] args = new Object[]
                    {
                            iKeyCurrent, armour.getEquipSlot().name(), armour.getArmourMaterial().name(), inventory.getItemCount(armour)
                    };
            
            dbManager.addEntryDB(buildInsertIntoSQL(tableName, columns), args);
        }
        
        tableName = STORED_WEAPONS.name();
        itemsColumns = STORED_WEAPONS.getColumns();
        columns = itemsColumns.keySet().toArray(new String[0]);
        
        Weapon[] weaponsArray = inventory.getAllItemTypeInInventory(Weapon.class);
        for(Weapon weapon : weaponsArray)
        {
            Object[] args = new Object[]
                    {
                            iKeyCurrent, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel(), inventory.getItemCount(weapon)
                    };
            
            dbManager.addEntryDB(buildInsertIntoSQL(tableName, columns), args);
        }
        
        tableName = STORED_POTIONS.name();
        itemsColumns = STORED_POTIONS.getColumns();
        columns = itemsColumns.keySet().toArray(new String[0]);
        
        Potion[] potionsArray = inventory.getAllItemTypeInInventory(Potion.class);
        for(Potion potion : potionsArray)
        {
            Object[] args = new Object[]
                    {
                            iKeyCurrent, potion.getPotionType().name(), inventory.getItemCount(potion)
                    };
            
            dbManager.addEntryDB(buildInsertIntoSQL(tableName, columns), args);
        }
    }
    
    private void updateMap(Map<Point, RuntimeRoom> map)
    {
        dbManager.deleteEntryDB(buildDeleteAllRowSQL(ROOMS.name(), iKeyColumnName), iKeyCurrent);
        
        
        String[] allColumns = ROOMS.getColumns().keySet().toArray(new String[0]);
        for(RuntimeRoom room : map.values())
        {
            Object[] arg = new Object[]
                    {
                            iKeyCurrent, room.getPoint().getX(), room.getPoint().getY(),
                            room.getRoomState().getRoomTileState().name(), room.getRoomState().getRoomType()
                    };
            dbManager.addEntryDB(buildInsertIntoSQL(ROOMS.name(), allColumns),arg);
        }
    }
    
    public void addItem(Item item, int count)
    {
        PersistenceTable table;
        Object[] args;
        if(item instanceof Armour armour)
        {
            table = STORED_ARMOUR;
            
            if(count == 1)
            {
                args = new Object[]{ iKeyCurrent, armour.getEquipSlot().name(), armour.getArmourMaterial().name(), count};
                dbManager.addEntryDB(buildInsertIntoSQL(table.name(), table.getColumnStrings()), args);
            }
            else
            {
                args = new Object[]{ iKeyCurrent, armour.getEquipSlot().name(), armour.getArmourMaterial().name() };
                dbManager.updateCellEntryDB(buildUpdateRowSQL(table.name(), table.getColumnStrings()[table.getColumnStrings().length - 1], table.getColumnStrings()), count, args);
            }
        }
        else if(item instanceof Weapon weapon)
        {
            table = STORED_WEAPONS;
            
            if(count == 1)
            {
                args = new Object[]{ iKeyCurrent, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel(), count};
                dbManager.addEntryDB(buildInsertIntoSQL(table.name(), table.getColumnStrings()), args);
            }
            else
            {
                args = new Object[]{ iKeyCurrent, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel()};
                dbManager.updateCellEntryDB(buildUpdateRowSQL(table.name(), table.getColumnStrings()[table.getColumnStrings().length - 1], table.getColumnStrings()), count, args);
            }
        }
        else if(item instanceof Potion potion)
        {
            table = STORED_POTIONS;
            
            if(count == 1)
            {
                args = new Object[]{ iKeyCurrent, potion.getPotionType().name(), count};
                dbManager.addEntryDB(buildInsertIntoSQL(table.name(), table.getColumnStrings()), args);
            }
            else
            {
                args = new Object[]{ iKeyCurrent, potion.getPotionType().name()};
                dbManager.updateCellEntryDB(buildUpdateRowSQL(table.name(), table.getColumnStrings()[table.getColumnStrings().length - 1], table.getColumnStrings()), count, args);
            }
        }
    }
    
    public void removeItem(Item item, int count)
    {
        PersistenceTable table;
        Object[] args;
        if(item instanceof Armour armour)
        {
            table = STORED_ARMOUR;
            
            if(count == 0)
            {
                args = new Object[]{ iKeyCurrent, armour.getEquipSlot().name(), armour.getArmourMaterial().name(), count};
                dbManager.deleteSingleEntryDB(buildDeleteRowSQL(table.name(), table.getColumnStrings()), args);
            }
            else
            {
                args = new Object[]{ iKeyCurrent, armour.getEquipSlot().name(), armour.getArmourMaterial().name() };
                dbManager.updateCellEntryDB(buildUpdateRowSQL(table.name(), table.getColumnStrings()[table.getColumnStrings().length - 1], table.getColumnStrings()), count, args);
            }
        }
        else if(item instanceof Weapon weapon)
        {
            table = STORED_WEAPONS;
            
            if(count == 0)
            {
                args = new Object[]{ iKeyCurrent, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel(), count};
                dbManager.deleteSingleEntryDB(buildDeleteRowSQL(table.name(), table.getColumnStrings()), args);
            }
            else
            {
                args = new Object[]{ iKeyCurrent, weapon.getEquipSlot().name(), weapon.getWeaponType().name(), weapon.getLevel()};
                dbManager.updateCellEntryDB(buildUpdateRowSQL(table.name(), table.getColumnStrings()[table.getColumnStrings().length - 1], table.getColumnStrings()), count, args);
            }
        }
        else if(item instanceof Potion potion)
        {
            table = STORED_POTIONS;
            
            if(count == 0)
            {
                args = new Object[]{ iKeyCurrent, potion.getPotionType().name(), count};
                dbManager.deleteSingleEntryDB(buildDeleteRowSQL(table.name(), table.getColumnStrings()), args);
            }
            else
            {
                args = new Object[]{ iKeyCurrent, potion.getPotionType().name()};
                dbManager.updateCellEntryDB(buildUpdateRowSQL(table.name(), table.getColumnStrings()[table.getColumnStrings().length - 1], table.getColumnStrings()), count, args);
            }
        }
    }
    
    public void updatePlayerStatus(Player player)
    {
        //Update Player Stats
    }

    public void updateRoomStatus(RuntimeRoom room)
    {
        //Update Single Entry
    }
}