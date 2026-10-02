package game.gui.common;

public enum ScaledLayoutAnchor
{
    NORTHWEST(0.0f, 0.0f),  NORTH(0.5f, 0.0f),  NORTHEAST(1.0f, 0.0f),
         WEST(0.0f, 0.5f), CENTER(0.5f, 0.5f),       EAST(1.0f, 0.5f),
    SOUTHWEST(0.0f, 1.0f),  SOUTH(0.5f, 1.0f),  SOUTHEAST(1.0f, 1.0f);
    
    private final float xLocation;
    private final float yLocation;
    
    ScaledLayoutAnchor(float x, float y)
    {
        xLocation = x;
        yLocation = y;
    }
    
    public float getXLocation() { return xLocation; }
    
    public float getYLocation() { return yLocation; }
}