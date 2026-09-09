package game.world;

import game.Result;

public class WorldResult extends Result<WorldErrors>
{
    private final RuntimeRoom room;
    
    public WorldResult(WorldErrors e, RuntimeRoom rRoom)
    {
        error = e;
        room = rRoom;
    }
    
    public RuntimeRoom getRoom() { return room; }
}
