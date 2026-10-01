package game.gui.common;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ScaledLayout implements LayoutManager2
{
    private final int baseWidth;
    private final int baseHeight;
    
    private final Map<Component, Rectangle> baseRectangles;
    
    public ScaledLayout(int width, int height)
    {
        baseWidth = width;
        baseHeight = height;
        baseRectangles = new HashMap<>();
    }
    
    @Override
    public void addLayoutComponent(Component comp, Object constraints)
    {
        if(constraints instanceof Rectangle rectangle)
        {
            baseRectangles.put(comp, rectangle);
        }
    }
    
    @Override
    public void addLayoutComponent(String name, Component comp)
    {
    
    }
    
    @Override
    public void removeLayoutComponent(Component comp)
    {
        baseRectangles.remove(comp);
    }
    
    @Override
    public void invalidateLayout(Container target)
    {
    
    }
    
    @Override
    public float getLayoutAlignmentX(Container target)
    {
        return 0.5f;
    }
    
    @Override
    public float getLayoutAlignmentY(Container target)
    {
        return 0.5f;
    }
    
    @Override
    public Dimension minimumLayoutSize(Container parent)
    {
        return new Dimension(0, 0);
    }
    
    @Override
    public Dimension maximumLayoutSize(Container target)
    {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    @Override
    public Dimension preferredLayoutSize(Container parent)
    {
        return new Dimension(baseWidth, baseHeight);
    }
    
    @Override
    public void layoutContainer(Container parent)
    {
        float scaleX = (float)parent.getWidth() / baseWidth;
        float scaleY = (float)parent.getHeight() / baseHeight;
        
        for(Entry<Component, Rectangle> entry : baseRectangles.entrySet())
        {
            Component component = entry.getKey();
            Rectangle baseRectangle = entry.getValue();
            
            int newX = Math.round(scaleX * baseRectangle.x);
            int newY = Math.round(scaleY * baseRectangle.y);
            int newWidth = Math.round(scaleX * baseRectangle.width);
            int newHeight = Math.round(scaleY * baseRectangle.height);
            
            component.setBounds(newX, newY, newWidth, newHeight);
        }
    }
}