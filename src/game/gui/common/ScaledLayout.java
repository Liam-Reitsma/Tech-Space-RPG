package game.gui.common;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ScaledLayout implements LayoutManager2
{
    private final int panelWidth;
    private final int panelHeight;
    
    private final Map<Component, Object> baseBounds;
    
    public ScaledLayout(int width, int height)
    {
        panelWidth = width;
        panelHeight = height;
        baseBounds = new HashMap<>();
    }
    
    @Override
    public void addLayoutComponent(Component comp, Object constraints)
    {
        if(constraints instanceof Rectangle rectangleBounds)
        {
            baseBounds.put(comp, rectangleBounds);
        }
        else if(constraints instanceof PercentageBounds percentageBounds)
        {
            baseBounds.put(comp, percentageBounds);
        }
    }
    
    @Override
    public void addLayoutComponent(String name, Component comp)
    {
    
    }
    
    @Override
    public void removeLayoutComponent(Component comp) { baseBounds.remove(comp); }
    
    @Override
    public void invalidateLayout(Container target) { }
    
    @Override
    public float getLayoutAlignmentX(Container target) { return 0.5f; }
    
    @Override
    public float getLayoutAlignmentY(Container target) { return 0.5f; }
    
    @Override
    public Dimension minimumLayoutSize(Container parent) { return new Dimension(0, 0); }
    
    @Override
    public Dimension maximumLayoutSize(Container target) { return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE); }
    
    @Override
    public Dimension preferredLayoutSize(Container parent) { return new Dimension(panelWidth, panelHeight); }
    
    @Override
    public void layoutContainer(Container parent)
    {
        float scaleX = (float)parent.getWidth() / panelWidth;
        float scaleY = (float)parent.getHeight() / panelHeight;
        
        for(Entry<Component, Object> entry : baseBounds.entrySet())
        {
            Component component = entry.getKey();
            Object baseObjectBounds = entry.getValue();
            
            int baseX = 0;
            int baseY = 0;
            int baseWidth = 0;
            int baseHeight = 0;
            
            if(baseObjectBounds instanceof Rectangle rectangleBounds)
            {
                baseX = rectangleBounds.x;
                baseY = rectangleBounds.y;
                baseWidth = rectangleBounds.width;
                baseHeight = rectangleBounds.height;
            }
            else if(baseObjectBounds instanceof PercentageBounds percentageBounds)
            {
                LayoutBox box = percentageBounds.getOwnBox();
                baseX = Math.round(box.getEffectiveX() * panelWidth);
                baseY = Math.round(box.getEffectiveY() * panelHeight);
                baseWidth = Math.round(box.getEffectiveWidth() * panelWidth);
                baseHeight = Math.round(box.getEffectiveHeight() * panelHeight);
            }
            
            baseWidth = Math.clamp(baseWidth, 0, panelWidth);
            baseHeight = Math.clamp(baseHeight, 0, panelHeight);
            baseX = Math.clamp(baseX, 0, panelWidth - baseWidth);
            baseY = Math.clamp(baseY, 0, panelHeight - baseHeight);
            
            component.setBounds(Math.round(scaleX * baseX), Math.round(scaleY * baseY), Math.round(scaleX * baseWidth), Math.round(scaleY * baseHeight));
        }
    }
}