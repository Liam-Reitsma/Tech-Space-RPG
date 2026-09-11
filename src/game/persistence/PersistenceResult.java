package game.persistence;

import game.Result;
import game.entities.Player;
import game.items.Armour;
import game.items.Item;
import game.items.Weapon;
import game.world.Point;
import game.world.RuntimeRoom;

import java.util.List;
import java.util.Map;

public class PersistenceResult extends Result<PersistenceErrors>
{
    private final Player player;
    private final Weapon weapon;
    private final List<Item> inventory;
    private final List<Armour> armour;
    private final Map<Point, RuntimeRoom> world;
    
    public PersistenceResult(PersistenceErrors e, Player loadedPlayer, Weapon loadedWeapon, List<Item> inventoryList, List<Armour> equippedList, Map<Point, RuntimeRoom> worldMap)
    {
        error = e;
        weapon = loadedWeapon;
        player = loadedPlayer;
        inventory = inventoryList;
        armour = equippedList;
        world = worldMap;
    }
    
    public PersistenceResult(PersistenceErrors e)
    {
        error = e;
        weapon = null;
        player = null;
        inventory = null;
        armour = null;
        world = null;
    }
    
    public Player getPlayer() { return player; }
    
    public Weapon getWeapon() { return weapon; }
    
    public List<Item> getInventory() { return inventory; }
    
    public List<Armour> getArmour() { return armour; }
    
    public Map<Point, RuntimeRoom> getWorld() { return world; }
}