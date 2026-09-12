package game.world;

import game.Result;
import game.entities.Persistable;

public class RoomResult extends Result<WorldErrors>
{
    private final Persistable persistable;
    private final RoomType roomType;
    
    public RoomResult(WorldErrors e, Persistable p, RoomType r)
    {
        error = e;
        persistable = p;
        roomType = r;
    }
    
    public Persistable getPersistable() { return persistable; }
    
    public RoomType getRoomType() { return roomType; }
}