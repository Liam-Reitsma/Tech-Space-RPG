package game.world;

public enum Direction
{
    NORTH, EAST, SOUTH, WEST;
    
    Direction()
    {
    
    }
    
    public Direction opposite()
    {
        return switch(this)
        {
            case NORTH -> SOUTH;
            case EAST -> WEST;
            case SOUTH -> NORTH;
            case WEST -> EAST;
        };
    }
}
