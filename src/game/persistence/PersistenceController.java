package game.persistence;

import static game.persistence.PersistenceErrors.*;
import static game.persistence.PersistenceTable.*;
import static game.persistence.SQLCommandFactory.*;

import game.*;
import game.entities.player.ClassType;
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
    private final SaveFile saveF;
    private final LoadFactory loadF;
    
    private final DBManager dbManager;
    
    public PersistenceController(DBManager dbm)
    {
        saveF = new SaveFile();
        loadF = new LoadFactory();
        dbManager = dbm;
    }
    
    public PersistenceErrors createNewGame(String saveName, String charName, ClassType classType)
    {
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
    
    public PersistenceResult fullSave(Player player, Weapon weapon, List<Item> inventory, List<Armour> armour, Map<Point, RuntimeRoom> map)
    {
        PersistenceErrors error;
        error = saveF.clearFile();
        if(error != SUCCESS) return new PersistenceResult(error);
        
        error = saveF.writeObject(player);
        if(error != SUCCESS) return new PersistenceResult(error);
        
        error = saveF.writeObject(weapon);
        if(error != SUCCESS) return new PersistenceResult(error);
        
        error = saveF.writeItemList(inventory, "inventory");
        if(error != SUCCESS) return new PersistenceResult(error);
        
        error = saveF.writeItemList(armour, "armour");
        if(error != SUCCESS) return new PersistenceResult(error);
        
        error = saveF.writeWorld(new ArrayList<>(map.values()), "world");
        if(error != SUCCESS) return new PersistenceResult(error);
        
        return new PersistenceResult(SUCCESS);
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