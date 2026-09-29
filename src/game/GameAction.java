package game;

import game.entities.player.Player;
import game.world.RuntimeRoom;

public interface GameAction
{
    void requestNewGame(Player newPlayer, RuntimeRoom[] newWorld);
    void requestLoadGame(Player loadedPlayer, RuntimeRoom[] loadedWorld);
}