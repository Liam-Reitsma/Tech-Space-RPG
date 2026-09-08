package game.world;

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
    
    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }
    
    @Override
    public boolean equals(Object otherPoint)
    {
        if(otherPoint == null) return false;
        if(otherPoint.getClass() != this.getClass()) return false;
        return (((Point)otherPoint).getX() == this.getX() && ((Point)otherPoint).getY() == this.getY());
    }
    
    @Override
    public String toString()
    {
        return "Point(" + x + ", " + y+ ")";
    }
}
