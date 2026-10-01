package game.gui.common;

public class PercentageBounds
{
    private final float xPercentage;
    private final float yPercentage;
    private final float widthPercentage;
    private final float heightPercentage;
    
    public PercentageBounds(float x, float y, float width, float height)
    {
        xPercentage = x / 100f;
        yPercentage = y / 100f;
        widthPercentage = width / 100f;
        heightPercentage = height / 100f;
    }
    
    public float getX() { return xPercentage; }
    
    public float getY() { return yPercentage; }
    
    public float getWidth() { return widthPercentage; }
    
    public float getHeight() { return heightPercentage; }
}