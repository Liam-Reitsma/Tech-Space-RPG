package game.gui.common;

public enum ScaledLayoutAnchor
{
    NORTHWEST(0f/3f, 0f/3f),  NORTH(1f/3f, 0f/3f),  NORTHEAST(2f/3f, 0f/3f),
         WEST(0f/3f, 1f/3f), CENTER(1f/3f, 1f/3f),       EAST(2f/3f, 1f/3f),
    SOUTHWEST(0f/3f, 2f/3f),  SOUTH(1f/3f, 2f/3f),  SOUTHEAST(2f/3f, 2f/3f);
    
    private final float xLocation;
    private final float yLocation;
    
    ScaledLayoutAnchor(float x, float y)
    {
        xLocation = x;
        yLocation = y;
    }
    
    public float getXRegion() { return xLocation; }
    
    public float getYRegion() { return yLocation; }
    
    public float getXAlignment() { return xLocation * 3f/2f; }
    
    public float getYAlignment() { return yLocation * 3f/2f; }
}