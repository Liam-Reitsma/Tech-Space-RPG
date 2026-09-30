package game.world;

public enum RoomTileState
{
    WALL,
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