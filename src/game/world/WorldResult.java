package game.world;

import game.Result;

import java.util.Map;

public class WorldResult extends Result<WorldErrors>
{
    private final Map<Point, RuntimeRoom> world;
    
    public WorldResult(WorldErrors e, Map<Point, RuntimeRoom> worldMap)
    {
        error = e;
        world = worldMap;
    }
    
    public Map<Point, RuntimeRoom> getMap() {return world; }
}
