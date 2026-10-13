package game.persistence;

import game.player.model.ClassType;
import game.player.model.Player;
import game.items.Item;
import game.world.model.Point;
import game.world.model.room.RuntimeRoom;

import java.util.Map;

public interface PersistenceActions
{
    //Loading
    Player loadSavedPlayer(String saveName);
    RuntimeRoom[] loadSavedWorld();
    
    //Full Saves
    void createNewGame(String saveName, String charName, ClassType classType);
    void fullSave(Player player, Map<Point, RuntimeRoom> map);
    
    //Incremental Saves
    void addItem(Item item, int count);
    void removeItem(Item item, int count);
    void updatePlayerStatus(Player player);
    void updateRoomStatus(RuntimeRoom room);
}