package game.world.model;

import game.gui.common.scaledlayout.ScaledLayoutAnchor;

public enum Direction
{
    NORTH(ScaledLayoutAnchor.NORTH),
    EAST(ScaledLayoutAnchor.EAST),
    SOUTH(ScaledLayoutAnchor.SOUTH),
    WEST(ScaledLayoutAnchor.WEST);
    
    private final ScaledLayoutAnchor anchor;
    
    Direction(ScaledLayoutAnchor direction)
    {
        anchor = direction;
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
    
    public ScaledLayoutAnchor getAnchor() { return anchor; }
    
    @Override
    public String toString() { return name().charAt(0) + name().substring(1).toLowerCase(); }
}