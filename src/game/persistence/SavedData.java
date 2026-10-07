package game.persistence;

import game.entities.player.Player;
import game.items.EquippableItem;
import game.world.model.Point;
import game.world.model.room.RuntimeRoom;

import java.util.Map;

public class SavedData
{
    public final Player player;
    public final EquippableItem[] equipped;
    public final Map<Point, RuntimeRoom> world;
    
    public SavedData(Player loadedPlayer, EquippableItem[] loadedEquipped, Map<Point, RuntimeRoom> loadedWorld)
    {
        player = loadedPlayer;
        equipped = loadedEquipped;
        world = loadedWorld;
    }
}