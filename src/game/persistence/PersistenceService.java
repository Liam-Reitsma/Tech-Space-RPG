package game.persistence;

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
    private String iKey;
    
    private final DatabaseSchemaManager databaseSchemaManager;
    private final InventoryDAO inventoryDAO;
    private final PlayerDAO playerDAO;
    private final WorldDAO worldDAO;
    
    public PersistenceService(DatabaseSchemaManager dbSchemaManager, InventoryDAO iDAO, PlayerDAO pDAO, WorldDAO wDAO)
    {
        databaseSchemaManager = dbSchemaManager;
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
    
    public boolean doesSaveExist(String saveName)
    {
        return playerDAO.doesSaveExist(saveName);
    }
    
    public void forceCreateNewSaveGame(String saveName, String charName, ClassType classType, Map<Point, RuntimeRoom> world)
    {
        deleteSaveGame(saveName);
        createNewSaveGame(saveName, charName, classType, world);
    }
    
    public boolean createNewSaveGame(String saveName, String charName, ClassType classType, Map<Point, RuntimeRoom> world)
    {
        Player player = new Player(charName, classType);
        if(!databaseSchemaManager.createTables())
            return false;
        
        boolean success = playerDAO.insertPlayer(saveName, player) &&
                inventoryDAO.insertEquippedItems(saveName, player) &&
                worldDAO.insertWorld(saveName, world);
        
        if(success)
            iKey = saveName;
        
        return success;
    }
    
    public boolean deleteSaveGame(String saveName)
    {
        boolean equippedDeleted = inventoryDAO.deleteEquipped(saveName);
        boolean inventoryDeleted = inventoryDAO.deleteInventory(saveName);
        boolean worldDeleted = worldDAO.deleteWorld(saveName);
        boolean playerDeleted = playerDAO.deletePlayer(saveName);
        
        return equippedDeleted && inventoryDeleted && worldDeleted && playerDeleted;
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
    
    public Map<String, Player> getAllSaves() { return playerDAO.getAllSaves(); }
}