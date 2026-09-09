package game.persistence;

import game.Result;
import game.entities.Persistable;
import game.entities.Player;
import game.world.Point;
import game.world.RuntimeRoom;

import java.util.List;
import java.util.Map;

public class PersistenceResult extends Result<PersistenceErrors>
{
    private final Player player;
    private final List<Persistable> inventory;
    private final List<Persistable> equipped;
    private final Map<Point, RuntimeRoom> world;
    
    public PersistenceResult(PersistenceErrors e, Player loadedPlayer, List<Persistable> inventoryList, List<Persistable> equippedList, Map<Point, RuntimeRoom> worldMap)
    {
        error = e;
        player = loadedPlayer;
        inventory = inventoryList;
        equipped = equippedList;
        world = worldMap;
    }
    
    public Player getPlayer() { return player; }
    
    public List<Persistable> getInventory() { return inventory; }
    
    public List<Persistable> getEquipped() { return equipped; }
    
    public Map<Point, RuntimeRoom> getWorld() { return world; }
}
