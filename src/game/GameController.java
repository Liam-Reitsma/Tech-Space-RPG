package game;

import game.entities.player.Player;
import game.world.RuntimeRoom;

public class GameController implements GameAction
{
    private Player player;
    private RuntimeRoom[] world;
    
    public GameController()
    {
    
    }
    
    @Override
    public void requestNewGame(Player newPlayer, RuntimeRoom[] newWorld)
    {
        player = newPlayer;
        world = newWorld;
    }
    
    @Override
    public void requestLoadGame(Player loadedPlayer, RuntimeRoom[] loadedWorld)
    {
        player = loadedPlayer;
        world = loadedWorld;
    }
}