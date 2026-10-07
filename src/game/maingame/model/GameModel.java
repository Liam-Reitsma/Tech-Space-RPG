package game.maingame.model;

import game.entities.player.Player;
import game.world.model.Point;
import game.world.model.room.RuntimeRoom;

import java.util.Map;

public class GameModel
{
    private Player player;
    private Map<Point, RuntimeRoom> world;
    
    public GameModel()
    {
    
    }
    
    public void setGame(Player newPlayer, Map<Point, RuntimeRoom> newWorld)
    {
        player = newPlayer;
        world = newWorld;
    }
    
    public Player getPlayer() { return player; }
    
    public Map<Point, RuntimeRoom> getWorld() { return world; }
}