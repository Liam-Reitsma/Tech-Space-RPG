package game.world;

import game.entities.Persistable;

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
        return "point" + Persistable.FIELD_SEPARATOR + x + "&" + y;
    }
    
    public static Point parsePoint(String data)
    {
        try
        {
            int dx = Integer.parseInt(data.substring(6).split("&")[0]);
            int dy = Integer.parseInt(data.substring(6).split("&")[1]);
            
            return new Point(dx, dy);
        } catch(RuntimeException e)
        {
            return null;
        }
    }
}
