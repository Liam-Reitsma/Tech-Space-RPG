package game.persistence;

import game.entities.player.Player;
import game.items.EquippableItem;
import game.items.Item;
import game.world.model.Point;
import game.world.model.room.RuntimeRoom;

import java.util.Map;

public class SavedData
{
    public final Player player;
    public final EquippableItem[] equipped;
    public final Map<Item, Integer> inventory;
    public final Map<Point, RuntimeRoom> world;
    
    public SavedData(Player loadedPlayer, EquippableItem[] loadedEquipped, Map<Item, Integer> loadedInventory, Map<Point, RuntimeRoom> loadedWorld)
    {
        player = loadedPlayer;
        equipped = loadedEquipped;
        inventory = loadedInventory;
        world = loadedWorld;
    }
}