package game.persistence;

import game.Result;
import game.entities.Player;
import game.items.EquippableItem;
import game.items.Item;
import game.world.Point;
import game.world.RuntimeRoom;

import java.util.List;
import java.util.Map;

public class PersistenceResult extends Result<PersistenceErrors>
{
    private final Player player;
    private final List<Item> inventory;
    private final List<EquippableItem> equipped;
    private final Map<Point, RuntimeRoom> world;
    
    public PersistenceResult(PersistenceErrors e, Player loadedPlayer, List<Item> inventoryList, List<EquippableItem> equippedList, Map<Point, RuntimeRoom> worldMap)
    {
        error = e;
        player = loadedPlayer;
        inventory = inventoryList;
        equipped = equippedList;
        world = worldMap;
    }
    
    public Player getPlayer() { return player; }
    
    public List<Item> getInventory() { return inventory; }
    
    public List<EquippableItem> getEquipped() { return equipped; }
    
    public Map<Point, RuntimeRoom> getWorld() { return world; }
}
