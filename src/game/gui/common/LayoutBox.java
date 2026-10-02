package game.gui.common;

public class LayoutBox
{
    private final LayoutBox parentBox;
    
    private final float xLocation;
    private final float yLocation;
    private final float boxWidth;
    private final float boxHeight;
    
    public LayoutBox(float x, float y, float width, float height)
    {
        this(null, x, y, width, height);
    }
    
    public LayoutBox(LayoutBox parent, float x, float y, float width, float height)
    {
        parentBox = parent;
        xLocation = x / 100f;
        yLocation = y / 100f;
        boxWidth = width / 100f;
        boxHeight = height / 100f;
    }
    
    public LayoutBox getParentBox() { return parentBox; }
    
    public float getXLocation() { return xLocation; }
    
    public float getYLocation() { return yLocation; }
    
    public float getWidth() { return boxWidth; }
    
    public float getHeight() { return boxHeight; }
}