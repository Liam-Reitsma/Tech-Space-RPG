package game.world;

public enum Direction
{
    NORTH, EAST, SOUTH, WEST;
    
    Direction()
    {
    
    }
    
    public Direction left()
    {
        return switch(this)
        {
            case NORTH -> WEST;
            case EAST -> NORTH;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
        };
    }
    
    public Direction right()
    {
        return switch(this)
        {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
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
