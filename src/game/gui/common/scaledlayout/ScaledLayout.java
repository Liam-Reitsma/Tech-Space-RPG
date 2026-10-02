package game.gui.common.scaledlayout;

import game.gui.common.scaledlayout.factory.FontFitter;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ScaledLayout implements LayoutManager2
{
    private final int panelWidth;
    private final int panelHeight;
    
    private final Map<Component, Object> baseBounds;
    private final Map<Component, Font> baseFonts;
    
    public ScaledLayout(int width, int height)
    {
        panelWidth = width;
        panelHeight = height;
        baseBounds = new HashMap<>();
        baseFonts = new HashMap<>();
    }
    
    @Override
    public void addLayoutComponent(Component comp, Object constraints)
    {
        if(constraints == null)
        {
            baseBounds.put(comp, new PercentageBounds(0, 0, 100, 100));
        }
        else if(constraints instanceof Rectangle rectangleBounds)
        {
            baseBounds.put(comp, rectangleBounds);
        }
        else if(constraints instanceof PercentageBounds percentageBounds)
        {
            baseBounds.put(comp, percentageBounds);
        }
        else
        {
            return;
        }
        
        registerFonts(comp);
    }
    
    private void registerFonts(Component comp)
    {
        if(comp instanceof Container container)
        {
            for(Component component : container.getComponents())
            {
                registerFonts(component);
            }
        }
        
        if(comp.isFontSet()) baseFonts.put(comp, comp.getFont());
    }
    
    @Override
    public void addLayoutComponent(String name, Component comp)
    {
    
    }
    
    @Override
    public void removeLayoutComponent(Component comp)
    {
        baseBounds.remove(comp);
        deregisterFonts(comp);
    }
    
    private void deregisterFonts(Component comp)
    {
        if(comp instanceof Container container)
        {
            for(Component component : container.getComponents())
            {
                deregisterFonts(component);
            }
        }
        
        baseFonts.remove(comp);
    }
    
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
        float xScale = (float)parent.getWidth() / panelWidth;
        float yScale = (float)parent.getHeight() / panelHeight;
        float fontScale = Math.min(xScale, yScale);
        
        if(parent.getWidth() <= 0 || parent.getHeight() <= 0)
            return;
        
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
            
            component.setBounds(Math.round(xScale * baseX), Math.round(yScale * baseY), Math.round(xScale * baseWidth), Math.round(yScale * baseHeight));
        }
        
        Map<Component, Font> scaledFonts = new HashMap<>();
        
        for(Entry<Component, Font> entry : baseFonts.entrySet())
        {
            Component component = entry.getKey();
            Font baseFont = entry.getValue();
            FontScaler.scaleFont(component, baseFont, fontScale);
            scaledFonts.put(component, component.getFont());
        }
        
        for(Component component : baseBounds.keySet())
        {
            if(component instanceof Container container)
            {
                container.doLayout();
            }
        }
        
        Map<Class<?>, Float> largestPossibleFonts = FontFitter.getFontFittingSizes(scaledFonts);
        
        for(Entry<Component, Font> entry : scaledFonts.entrySet())
        {
            Component component = entry.getKey();
            
            component.setFont(component.getFont().deriveFont(largestPossibleFonts.get(component.getClass())));
        }
    }
}