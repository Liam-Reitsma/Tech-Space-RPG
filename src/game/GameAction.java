package game;

import game.entities.player.Player;
import game.world.Point;
import game.world.room.RuntimeRoom;

import java.util.Map;

public interface GameAction
{
    void requestNewGame(Player newPlayer);
    void requestLoadGame(Player loadedPlayer, Map<Point, RuntimeRoom> loadedWorld);
    void requestGameUpdate();
}