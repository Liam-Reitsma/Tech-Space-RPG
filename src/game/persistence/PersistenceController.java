package game.persistence;

import static game.persistence.PersistenceErrors.*;
import static game.persistence.PersistenceTable.*;
import static game.persistence.SQLCommandFactory.*;

import game.*;
import game.entities.player.ClassType;
import game.entities.player.Inventory;
import game.entities.player.Player;
import game.items.*;
import game.items.armour.Armour;
import game.items.weapon.Weapon;
import game.items.weapon.WeaponType;
import game.world.Point;
import game.world.RuntimeRoom;

import java.util.*;

public class PersistenceController
{
    private String iKeyCurrent;
    private String iKeyColumnName;
    
    private final SaveFile saveF;
    private final DBManager dbManager;
    
    public PersistenceController(DBManager dbm)
    {
        saveF = new SaveFile();
        dbManager = dbm;
    }
    
    public PersistenceErrors createNewGame(String saveName, String charName, ClassType classType)
    {
        iKeyCurrent = saveName;
        Player player = new Player(charName, classType);
        String iKey = "SAVE_NAME";
        createTables(iKey);
        insertPlayer(saveName, player);
        insertPlayerStats(saveName, player);
        insertEquippedItems(saveName, player);
        return SUCCESS;
    }
    
    private void createTables(String iKey)
    {
        PersistenceTable table = PLAYER;
        LinkedHashMap<String, String> columns = table.getColumns();
        dbManager.createTable(table.name(),
                buildCreateTableSQL(table.name(), columns, new String[]{iKey},
                        columns.keySet().toArray(new String[0])));
        
        table = PLAYER_STATS;
        columns = table.getColumns();
        dbManager.createTable(table.name(),
                buildCreateTableSQL(table.name(), columns, new String[]{iKey},
                        columns.keySet().toArray(new String[0]),
                        new String[]{PLAYER.name()},new String[]{iKey}));
        
        table = EQUIPPED_ITEMS;
        columns = table.getColumns();
        dbManager.createTable(table.name(),
                buildCreateTableSQL(table.name(), columns, new String[]{iKey},
                        new String[]{iKey},
                        new String[]{PLAYER.name()},new String[]{iKey}));
        
        table = STORED_WEAPONS;
        columns = table.getColumns();
        dbManager.createTable(table.name(),
                buildCreateTableSQL(table.name(), columns, new String[]{iKey, "WEAPON_TYPE"},
                        columns.keySet().toArray(new String[0]),
                        new String[]{PLAYER.name()},new String[]{iKey}));
        
        table = STORED_ARMOUR;
        columns = table.getColumns();
        dbManager.createTable(table.name(),
                buildCreateTableSQL(table.name(), columns, new String[]{iKey, "ARMOUR_SLOT", "ARMOUR_MATERIAL"},
                        columns.keySet().toArray(new String[0]),
                        new String[]{PLAYER.name()},new String[]{iKey}));
        
        table = STORED_POTIONS;
        columns = table.getColumns();
        dbManager.createTable(table.name(),
                buildCreateTableSQL(table.name(), columns, new String[]{iKey, "POTION_TYPE"},
                        columns.keySet().toArray(new String[0]),
                        new String[]{PLAYER.name()},new String[]{iKey}));
        
        table = ROOMS;
        columns = table.getColumns();
        dbManager.createTable(table.name(),
                buildCreateTableSQL(table.name(), columns, new String[]{iKey, "X_POINT", "Y_POINT"},
                        columns.keySet().toArray(new String[0]),
                        new String[]{PLAYER.name()},new String[]{iKey}));
    }
    
    private void insertPlayer(String saveName, Player player)
    {
        LinkedHashMap<String, String> playerColumns = PLAYER.getColumns();
        
        String[] columns = playerColumns.keySet().toArray(new String[0]);
        
        Object[] args = new Object[]
                        {
                            saveName, player.getName(), player.getLevel(),
                            player.getHealth(), player.getXP(), player.getGold(),
                            player.getClassType().toString(), player.getPoint().getX(), player.getPoint().getY()
                        };
        
        dbManager.insertIntoDB(buildInsertIntoSQL(PLAYER.name(), columns), args);
    }
    
    private void insertPlayerStats(String saveName, Player player)
    {
        LinkedHashMap<String, String> playerStatColumns = PLAYER_STATS.getColumns();
        
        String[] columns = playerStatColumns.keySet().toArray(new String[0]);
        
        Object[] args = new Object[]
                        {
                            saveName, player.getSTR(), player.getDEX(), player.getINT()
                        };
        
        dbManager.insertIntoDB(buildInsertIntoSQL(PLAYER_STATS.name(), columns), args);
    }
    
    private void insertEquippedItems(String saveName, Player player)
    {
        String tableName = EQUIPPED_ITEMS.name();
        
        LinkedHashMap<String, String> equippedItemsColumns = EQUIPPED_ITEMS.getColumns();
        
        String[] columns = equippedItemsColumns.keySet().toArray(new String[0]);
        
        Object[] args = new Object[]
                {
                        saveName, player.getClassType().getStartingWeapon(), null, null, null, null, null, null
                };
        
        dbManager.insertIntoDB(buildInsertIntoSQL(tableName, columns), args);
    }
    
    public String[] getSaveOptionNames()
    {
        return dbManager.getSavesName();
    }
    
    public PersistenceResult loadSaveGame(String saveName)
    {
        iKeyCurrent = saveName;
        //TODO: Replace Player Object[] loading with column-name-based mapping (e.g. Map<String, Object>) so loading does not depend on database column order.
        Object[] playerData = dbManager.queryDB(buildSelectSQL(PLAYER.name(), "SAVE_NAME"), saveName);
        Object[] statsData = dbManager.queryDB(buildSelectSQL(PLAYER_STATS.name(), "SAVE_NAME"), saveName);
        Player player = new Player(playerData, statsData);
        
        //TODO: Replace type/string-parsing based loading for Weapon, Armour, WeaponType, ArmourType, etc. with explicit column-name mapping so each saved field is reconstructed from its intended database column.
        WeaponType weaponType = WeaponType.RUSTED_KNIFE;
        int weaponLevel = 0;
        for(Object datum : dbManager.queryDB(buildSelectSQL(EQUIPPED_ITEMS.name(), "SAVE_NAME"), saveName))
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
        return new PersistenceResult(SUCCESS);
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
        dbManager.updateEntryDB(buildUpdateTable(PLAYER.name(), iKeyColumnName, Arrays.copyOfRange(allColumns, 1, allColumns.length)), iKeyCurrent, args);
        
        allColumns = PLAYER_STATS.getColumns().keySet().toArray(new String[0]);
        
        args = new Object[]
                {
                        player.getSTR(), player.getDEX(), player.getINT()
                };
        
        dbManager.updateEntryDB(buildUpdateTable(PLAYER_STATS.name(), iKeyColumnName, Arrays.copyOfRange(allColumns, 1, allColumns.length)), iKeyCurrent, args);
    }
    
    private void updateEquippedEquipment(Inventory inventory)
    {
        String[] allColumns = EQUIPPED_ITEMS.getColumns().keySet().toArray(new String[0]);
        
        return new PersistenceResult(SUCCESS);
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
        
        dbManager.updateEntryDB(buildUpdateTable(EQUIPPED_ITEMS.name(), iKeyColumnName, columns), iKeyCurrent, args);
    }
    
    private void updateMap(Map<Point, RuntimeRoom> map)
    {
    
    }
    
    public PersistenceResult saveItem(String listName, Item item)
    {
        LoadResult result = saveF.readAndInsert(listName, item);
        return result.getError() == SUCCESS ?
                new PersistenceResult(saveF.writeList(result.getResult())) :
                new PersistenceResult(result.getError());
    }
    
    public PersistenceResult removeItem(String listName, Item item)
    {
        LoadResult result = saveF.readAndRemove(listName, item);
        return result.getError() == SUCCESS ?
                new PersistenceResult(saveF.writeList(result.getResult())) :
                new PersistenceResult(result.getError());
    }
    
    public PersistenceResult updatePlayerStatus(Player player)
    {
        return new PersistenceResult(saveF.writeDeltaObject(player));
    }
    
    public PersistenceResult updateRoomStatus(RuntimeRoom room)
    {
        return new PersistenceResult(saveF.writeDeltaRoom(room));
    }
}