package game.gui.common.scaledlayout;

public class PercentageBounds
{
    private final float xPercentage;
    private final float yPercentage;
    private final float widthPercentage;
    private final float heightPercentage;
    
    private final ScaledLayoutAnchor componentAlignment;
    
    private final LayoutBox parentBox;
    private final LayoutBox ownBox;
    
    public PercentageBounds(float x, float y, float width, float height)
    {
        xPercentage = x / 100f;
        yPercentage = y / 100f;
        widthPercentage = width / 100f;
        heightPercentage = height / 100f;
        componentAlignment = null;
        parentBox = null;
        ownBox = new LayoutBox(x, y, width, height);
    }
    
    public PercentageBounds(LayoutBox box, float width, float height, ScaledLayoutAnchor alignment)
    {
        xPercentage = 0f;
        yPercentage = 0f;
        widthPercentage = width / 100f;
        heightPercentage = height / 100f;
        componentAlignment = alignment;
        parentBox = box;
        ownBox = new LayoutBox(parentBox, (100 - width) * alignment.getXLocation(), (100 - height) * alignment.getYLocation(), width, height);
    }
    
    public LayoutBox getParentBox() { return parentBox; }
    
    public LayoutBox getOwnBox() { return ownBox; }
    
    public float getX() { return xPercentage; }
    
    public float getY() { return yPercentage; }
    
    public float getWidth() { return widthPercentage; }
    
    public float getHeight() { return heightPercentage; }
    
    public ScaledLayoutAnchor getComponentAlignment() { return componentAlignment; }
}