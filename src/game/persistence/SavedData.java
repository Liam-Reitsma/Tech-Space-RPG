package game.persistence;

import game.player.model.ItemStack;
import game.player.model.Player;
import game.items.EquippableItem;
import game.items.Item;
import game.world.model.Point;
import game.world.model.room.RuntimeRoom;

import java.util.Map;

public class SavedData
{
    public final Player player;
    public final EquippableItem[] equipped;
    public final ItemStack[] inventory;
    public final Map<Point, RuntimeRoom> world;
    
    public SavedData(Player loadedPlayer, EquippableItem[] loadedEquipped, ItemStack[] loadedInventory, Map<Point, RuntimeRoom> loadedWorld)
    {
        player = loadedPlayer;
        equipped = loadedEquipped;
        inventory = loadedInventory;
        world = loadedWorld;
    }
}