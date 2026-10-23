package game.persistence;

import game.player.model.ClassType;
import game.player.model.ItemStack;
import game.player.model.Player;
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

    /**
     * Deletes the save associated with save name and creates a replacement only if the deletion was successful.
     * If replacement creation fails, the previous save is not restored.
     *
     * @param saveName Name of the save.
     * @param charName Name of the new player.
     * @param classType Class type of the player.
     * @param world The world to save with the new character.
     * @return True if the existing save was deleted and its replacement created successfully; otherwise false.
     */
    public boolean forceCreateNewSaveGame(String saveName, String charName, ClassType classType, Map<Point, RuntimeRoom> world)
    {
        return deleteSaveGame(saveName) && createNewSaveGame(saveName, charName, classType, world);
    }

    /**
     * Creates a new save containing the player, starting equipment and world.
     * Ensures the database tables exist and sets the active save only on success.
     * @param saveName Name of the save.
     * @param charName Name of the new player.
     * @param classType Class type of the new player.
     * @param world The world to save with the new character.
     * @return True if all save operations were successful, otherwise false.
     */
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

    /**
     * Saves the player's current state, equipped items and inventory to the active save.
     * Replaces the saved world data in the database.
     * @param player Player to save.
     * @param world The world to save.
     * @return True if all operations succeed; otherwise false.
     */
    public boolean fullSave(Player player, Map<Point, RuntimeRoom> world)
    {
        if(iKey == null)
            return false;
        
        boolean success = playerDAO.updatePlayer(iKey, player);
        success = inventoryDAO.updateEquippedEquipment(iKey, player.getInventory()) && success;
        success = inventoryDAO.updateStoredEquipment(iKey, player.getInventory()) && success;
        success = worldDAO.updateMap(iKey, world) && success;
        
        return  success;
    }

    /**
     * Updates the player's state and the current room's state in the active save.
     * Updates inventory data according to the specified change.
     *
     * @param player The player to save.
     * @param room The room to save.
     * @param change The inventory change: NONE, ADD, REMOVE or EQUIP.
     * @param item The affected item for ADD or REMOVE; otherwise may be null.
     * @return True if all required operations succeed; otherwise false.
     */
    public boolean quickSave(Player player, RuntimeRoom room, InventoryChange change, Item item)
    {
        if(iKey == null)
            return false;
        
        if(item == null && (change == InventoryChange.ADD || change == InventoryChange.REMOVE))
            return false;
        
        boolean success = playerDAO.updatePlayer(iKey, player);
        success = worldDAO.updateRoomStatus(iKey, room) && success;
        
        return switch(change)
        {
            case NONE -> success;
            case ADD -> success && inventoryDAO.addItem(iKey, item, player.getInventory().getItemCount(item), player.getInventory().getItemIndex(item));
            case EQUIP -> success && inventoryDAO.updateStoredEquipment(iKey, player.getInventory()) && inventoryDAO.updateEquippedEquipment(iKey, player.getInventory());
            case REMOVE -> success && inventoryDAO.removeItem(iKey, item, player.getInventory().getItemCount(item), player.getInventory().getItemIndex(item));
        };
    }

    /**
     * Loads the player, equipped items, inventory and world from the specified save.
     * Sets the loaded save as the active save when all required data is retrieved.
     *
     * @param saveName Name of the save.
     * @return The restored player and world as SavedData, or null if loading fails.
     */
    public SavedData loadSaveGame(String saveName)
    {
        Player player = playerDAO.loadSavedPlayer(saveName);
        if(player == null)
            return null;
        
        EquippableItem[] equipped = inventoryDAO.loadEquippedItems(saveName);
        if(equipped == null || equipped.length == 0)
            return null;
        
        ItemStack[] inventory = inventoryDAO.loadInventory(saveName);
        if(inventory == null)
            return null;
        
        Map<Point, RuntimeRoom> world = worldDAO.loadSavedWorld(saveName);
        if(world == null || world.isEmpty())
            return null;
        
        iKey = saveName;
        
        player.setEquipped(equipped);
        player.setInventory(inventory);
        
        return new SavedData(player, world);
    }
    
    public boolean doesSaveExist(String saveName)
    {
        return playerDAO.doesSaveExist(saveName);
    }
    
    public Map<String, Player> getAllSaves() { return playerDAO.getAllSaves(); }
}