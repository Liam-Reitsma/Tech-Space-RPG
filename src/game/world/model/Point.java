package game.world.model;

import java.util.Objects;

public class Point
{
    private final int x;
    private final int y;
    
    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public int getX() { return x; }
    
    public int getY() { return y; }

    /**
     * Gives the cardinal direction from otherPoint to this.
     * The points don't have to be adjacent.
     *
     * @param otherPoint The point used as the reference position.
     * @return The relative direction, or null if no cardinal direction applies.
     */
    public Direction returnRelativeDirection(Point otherPoint)
    {
        if(this.x == otherPoint.x && this.y > otherPoint.y)
            return Direction.NORTH;
        if(this.x == otherPoint.x && this.y < otherPoint.y)
            return Direction.SOUTH;
        if(this.x > otherPoint.x && this.y == otherPoint.y)
            return Direction.EAST;
        if(this.x < otherPoint.x && this.y == otherPoint.y)
            return Direction.WEST;
        
        return null;
    }

    /**
     * Creates a new point that is one coordinate away in the specified direction, leaving the original unchanged.
     *
     * @param direction The direction to move.
     * @return A new point in that direction, or null if the direction is null.
     */
    public Point move(Direction direction)
    {
        if(direction == Direction.NORTH)
            return new Point(getX(), getY() + 1);
        if(direction == Direction.SOUTH)
            return new Point(getX(), getY() - 1);
        if(direction == Direction.EAST)
            return new Point(getX() + 1, getY());
        if(direction == Direction.WEST)
            return new Point(getX() - 1, getY());
        
        return null;
    }

    /**
     * Returns four new Point instances adjacent to the supplied point in the order of North, East, South, West.
     * Does not modify the supplied point.
     *
     * @param playerPoint The reference point.
     * @return Four adjacent points ordered North, East, South, West.
     */
    public static Point[] getOrthogonalPoints(Point playerPoint)
    {
        Point[] orthogonalPoints = new Point[4];
        
        orthogonalPoints[0] = new Point(playerPoint.x, playerPoint.y + 1);
        orthogonalPoints[1] = new Point(playerPoint.x + 1, playerPoint.y);
        orthogonalPoints[2] = new Point(playerPoint.x, playerPoint.y - 1);
        orthogonalPoints[3] = new Point(playerPoint.x - 1, playerPoint.y);
        
        return orthogonalPoints;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }
    
    @Override
    public boolean equals(Object otherPoint)
    {
        if(otherPoint == null)
            return false;
        if(otherPoint.getClass() != this.getClass())
            return false;
        return (((Point)otherPoint).getX() == this.getX() && ((Point)otherPoint).getY() == this.getY());
    }
}