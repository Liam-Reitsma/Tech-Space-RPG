package game.persistence;

import static game.persistence.PersistenceTable.*;
import static game.persistence.SQLCommandFactory.*;

import game.entities.player.ClassType;
import game.entities.player.Player;
import game.items.EquippableItem;
import game.items.Item;
import game.persistence.dao.InventoryDAO;
import game.persistence.dao.PlayerDAO;
import game.persistence.dao.WorldDAO;
import game.world.model.Point;
import game.world.model.room.RuntimeRoom;

import java.util.*;

public class PersistenceService
{
    private static final String COLUMN_NAME = "SAVE_NAME";
    
    private String iKey;
    
    private final DBManager dbManager;
    private final InventoryDAO inventoryDAO;
    private final PlayerDAO playerDAO;
    private final WorldDAO worldDAO;
    
    public PersistenceService(DBManager db, InventoryDAO iDAO, PlayerDAO pDAO, WorldDAO wDAO)
    {
        dbManager = db;
        inventoryDAO = iDAO;
        playerDAO = pDAO;
        worldDAO = wDAO;
    }
    
    public SavedData loadSaveGame(String saveName)
    {
        Player player = playerDAO.loadSavedPlayer(saveName);
        if(player == null)
            return null;
        
        EquippableItem[] equipped = inventoryDAO.loadEquippedItems(saveName);
        if(equipped == null || equipped.length == 0)
            return null;
        
        Map<Item, Integer> inventory = inventoryDAO.loadInventory(saveName);
        if(inventory == null)
            return null;
        
        Map<Point, RuntimeRoom> world = worldDAO.loadSavedWorld(saveName);
        if(world == null || world.isEmpty())
            return null;
        
        iKey = saveName;
        
        return new SavedData(player, equipped, inventory, world);
    }
    
    public boolean createNewSaveGame(String saveName, String charName, ClassType classType, Map<Point, RuntimeRoom> world)
    {
        Player player = new Player(charName, classType);
        if(!createTables())
            return false;
        
        boolean success = playerDAO.insertPlayer(saveName, player) &&
                inventoryDAO.insertEquippedItems(saveName, player) &&
                worldDAO.insertWorld(saveName, world);
        
        if(success)
            iKey = saveName;
        
        return success;
    }
    
    private boolean createTables()
    {
        if(!dbManager.createTable(PLAYER.name(),
                buildCreateTableSQL(PLAYER.name(), PLAYER.getColumns(), PLAYER.getPrimaryKeys(),
                        PLAYER.getColumnStrings())))
            return false;
        
        for(PersistenceTable table : PersistenceTable.values())
        {
            if(table == PLAYER)
                continue;
            if(!dbManager.createTable(table.name(), buildCreateTableSQL(table.name(), table.getColumns(), table.getPrimaryKeys(),
                    table.getColumnStrings(), new String[]{PLAYER.name()}, new String[]{COLUMN_NAME})))
                return false;
        }
        
        return true;
    }
    
    public boolean quickSave(Player player, RuntimeRoom room, InventoryChange change, Item item)
    {
        if(iKey == null)
            return false;
        
        boolean success = playerDAO.updatePlayer(iKey, player) && worldDAO.updateRoomStatus(iKey, room);
        
        if(item == null && (change == InventoryChange.ADD || change == InventoryChange.REMOVE))
            return false;
        
        return switch(change)
        {
            case NONE -> success;
            case ADD -> success && inventoryDAO.addItem(iKey, item, player.getInventory().getItemCount(item));
            case EQUIP -> success && inventoryDAO.updateStoredEquipment(iKey, player.getInventory()) && inventoryDAO.updateEquippedEquipment(iKey, player.getInventory());
            case REMOVE -> success && inventoryDAO.removeItem(iKey, item, player.getInventory().getItemCount(item));
        };
    }
    
    public boolean fullSave(Player player, Map<Point, RuntimeRoom> world)
    {
        if(iKey == null)
            return false;
        return  playerDAO.updatePlayer(iKey, player) &&
                inventoryDAO.updateEquippedEquipment(iKey, player.getInventory()) &&
                inventoryDAO.updateStoredEquipment(iKey, player.getInventory()) &&
                worldDAO.updateMap(iKey, world);
    }
    
    public Player[] getAllSaves() { return playerDAO.getAllSaves(); }
}