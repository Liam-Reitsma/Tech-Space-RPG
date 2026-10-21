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
    public final Map<Point, RuntimeRoom> world;
    
    public SavedData(Player loadedPlayer, Map<Point, RuntimeRoom> loadedWorld)
    {
        player = loadedPlayer;
        world = loadedWorld;
    }
}