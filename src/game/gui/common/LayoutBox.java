package game.gui.common;

public class LayoutBox
{
    private final float xLocation;
    private final float yLocation;
    private final float boxWidth;
    private final float boxHeight;
    
    public LayoutBox(float x, float y, float width, float height)
    {
        xLocation = x / 100f;
        yLocation = y / 100f;
        boxWidth = width / 100f;
        boxHeight = height / 100f;
    }
    
    public float getXLocation() { return xLocation; }
    
    public float getYLocation() { return yLocation; }
    
    public float getWidth() { return boxWidth; }
    
    public float getHeight() { return boxHeight; }
}