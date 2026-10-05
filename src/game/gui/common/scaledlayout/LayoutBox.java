package game.gui.common.scaledlayout;

public class LayoutBox implements ScaledLayoutConstraint
{
    private final LayoutBox parentBox;
    
    private final float xLocation;
    private final float yLocation;
    private final float boxWidth;
    private final float boxHeight;
    
    public LayoutBox()
    {
        this(null, 0, 0, 100, 100);
    }
    
    public LayoutBox(float x, float y, float width, float height)
    {
        this(null, x, y, width, height);
    }
    
    public LayoutBox(LayoutBox parent, float width, float height, ScaledLayoutAnchor alignment)
    {
        this(parent, (100 - width) * alignment.getXLocation(), (100 - height) * alignment.getYLocation(), width, height);
    }
    
    public LayoutBox(LayoutBox parent, float x, float y, float width, float height)
    {
        parentBox = parent;
        xLocation = x / 100f;
        yLocation = y / 100f;
        boxWidth = width / 100f;
        boxHeight = height / 100f;
    }
    
    public LayoutBox[] splitVertical(float... args)
    {
        LayoutBox[] boxes = new LayoutBox[args.length];
        
        float totalWeight = 0;
        float position = 0;
        
        for(float arg : args)
        {
            if(arg <= 0) return boxes;
            totalWeight += arg;
        }
        
        for(int i = 0; i < args.length; i++)
        {
            float relativeWeight = 100 * args[i] / totalWeight;
            boxes[i] = new LayoutBox(this, 0, position, 100, relativeWeight);
            position += relativeWeight;
        }
        
        return boxes;
    }
    
    public LayoutBox[] splitHorizontal(float... args)
    {
        LayoutBox[] boxes = new LayoutBox[args.length];
        
        float totalWeight = 0;
        float position = 0;
        
        for(float arg : args)
        {
            if(arg <= 0) return boxes;
            totalWeight += arg;
        }
        
        for(int i = 0; i < args.length; i++)
        {
            float relativeWeight = 100 * args[i] / totalWeight;
            boxes[i] = new LayoutBox(this, position, 0, relativeWeight, 100);
            position += relativeWeight;
        }
        
        return boxes;
    }
    
    public LayoutBox insets(float inset)
    {
        return new LayoutBox(this, inset, inset, 100 - (2 * inset), 100 - (2 * inset));
    }
    
    public LayoutBox insets(float widthInset, float heightInset)
    {
        return new LayoutBox(this, widthInset, heightInset, 100 - (2 * widthInset), 100 - (2 * heightInset));
    }
    
    public LayoutBox insets(float left, float top, float right, float bottom)
    {
        return new LayoutBox(this, left, top, 100 - (left + right), 100 - (top + bottom));
    }
    
    @Override
    public LayoutBox getBox() { return this; }
    
    public float getWidth() { return boxWidth; }
    
    public float getHeight() { return boxHeight; }
    
    public float getEffectiveWidth() { return parentBox != null ? parentBox.getEffectiveWidth() * boxWidth : boxWidth; }
    
    public float getEffectiveX() { return parentBox != null ? parentBox.getEffectiveX() + parentBox.getEffectiveWidth() * xLocation : xLocation; }
    
    public float getEffectiveHeight() { return parentBox != null ? parentBox.getEffectiveHeight() * boxHeight : boxHeight; }
    
    public float getEffectiveY() { return parentBox != null ? parentBox.getEffectiveY() + parentBox.getEffectiveHeight() * yLocation : yLocation; }
}