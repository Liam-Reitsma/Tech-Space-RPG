package game.gui.common;

public class PercentageBounds
{
    private final float xPercentage;
    private final float yPercentage;
    private final float widthPercentage;
    private final float heightPercentage;
    private final ScaledLayoutAnchor componentRegion;
    private final ScaledLayoutAnchor componentAlignment;
    
    public PercentageBounds(float x, float y, float width, float height)
    {
        xPercentage = x / 100f;
        yPercentage = y / 100f;
        widthPercentage = width / 100f;
        heightPercentage = height / 100f;
        componentRegion = null;
        componentAlignment = null;
    }
    
    public PercentageBounds(float width, float height, ScaledLayoutAnchor region)
    {
        xPercentage = 0f;
        yPercentage = 0f;
        widthPercentage = width / 100f;
        heightPercentage = height / 100f;
        componentRegion = region;
        componentAlignment = ScaledLayoutAnchor.CENTER;
    }
    
    public PercentageBounds(float width, float height, ScaledLayoutAnchor region, ScaledLayoutAnchor alignment)
    {
        xPercentage = 0f;
        yPercentage = 0f;
        widthPercentage = width / 100f;
        heightPercentage = height / 100f;
        componentRegion = region;
        componentAlignment = alignment;
    }
    
    public float getX() { return xPercentage; }
    
    public float getY() { return yPercentage; }
    
    public float getWidth() { return widthPercentage; }
    
    public float getHeight() { return heightPercentage; }
    
    public ScaledLayoutAnchor getComponentRegion() { return componentRegion; }
    
    public ScaledLayoutAnchor getComponentAlignment() { return componentAlignment; }
}