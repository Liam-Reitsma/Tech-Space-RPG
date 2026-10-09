package game.maingame.model;

import game.entities.player.Player;
import game.items.Item;
import game.persistence.InventoryChange;
import game.persistence.PersistenceService;
import game.world.model.Point;
import game.world.model.room.RuntimeRoom;

import java.util.Map;

public class GameModel
{
    private Player player;
    private Map<Point, RuntimeRoom> world;
 
    private final PersistenceService persistenceService;
    
    public GameModel(PersistenceService pService)
    {
        persistenceService = pService;
    }
    
    public void setGame(Player newPlayer, Map<Point, RuntimeRoom> newWorld)
    {
        player = newPlayer;
        world = newWorld;
    }
    
    public void fullSave()
    {
        persistenceService.fullSave(player, world);
    }
    
    public void quickSave(RuntimeRoom room, InventoryChange change, Item item)
    {
        persistenceService.quickSave(player, room, change, item);
    }
    
    public Player getPlayer() { return player; }
    
    public Map<Point, RuntimeRoom> getWorld() { return world; }
}