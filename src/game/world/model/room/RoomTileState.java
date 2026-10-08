package game.world.model.room;

public enum RoomTileState
{
    UNSEEN,
    COMPLETED,
    FLED,
    UNVISITED,
    CURRENT;
    
    @Override
    public String toString()
    {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}