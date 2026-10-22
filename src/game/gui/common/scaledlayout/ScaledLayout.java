package game.gui.common.scaledlayout;

import game.gui.common.scaledlayout.factory.FontFitter;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ScaledLayout implements LayoutManager2
{
    private int panelWidth;
    private int panelHeight;
    
    private final boolean isAutomatic;
    
    private final Map<Component, ScaledLayoutConstraint> baseBounds;
    private final Map<Component, Font> baseFonts;
    
    public ScaledLayout()
    {
        isAutomatic = true;
        baseBounds = new HashMap<>();
        baseFonts = new HashMap<>();
    }
    
    public ScaledLayout(int width, int height)
    {
        panelWidth = width;
        panelHeight = height;
        isAutomatic = false;
        baseBounds = new HashMap<>();
        baseFonts = new HashMap<>();
    }
    
    @Override
    public void addLayoutComponent(Component comp, Object constraints)
    {
        switch(constraints)
        {
            case null -> baseBounds.put(comp, new LayoutBox(0, 0, 100, 100));
            case LayoutBox layoutBox -> baseBounds.put(comp, layoutBox);
            case ScaledConstraint scaledConstraint -> baseBounds.put(comp, scaledConstraint);
            default -> { return; }
        }
        
        registerFonts(comp);
    }
    
    private void registerFonts(Component comp)
    {
        if(comp instanceof Container container)
            for(Component component : container.getComponents())
                registerFonts(component);
        
        if(comp.isFontSet())
            baseFonts.put(comp, comp.getFont());
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
            for(Component component : container.getComponents())
                deregisterFonts(component);
        
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
        if(parent.getWidth() <= 0 || parent.getHeight() <= 0 || panelWidth <= 0 || panelHeight <= 0)
            return;
        
        float xScale = (float)parent.getWidth() / panelWidth;
        float yScale = (float)parent.getHeight() / panelHeight;
        float fontScale = Math.max(0.01f, Math.min(xScale, yScale));
        
        for(Entry<Component, ScaledLayoutConstraint> entry : baseBounds.entrySet())
        {
            Component component = entry.getKey();
            ScaledLayoutConstraint constraint = entry.getValue();
            LayoutBox box = constraint.getBox();
            
            int baseX = Math.round(box.getEffectiveX() * panelWidth);
            int baseY = Math.round(box.getEffectiveY() * panelHeight);
            int baseWidth = Math.round(box.getEffectiveWidth() * panelWidth);
            int baseHeight = Math.round(box.getEffectiveHeight() * panelHeight);
            
            if(baseHeight != 0 && constraint instanceof ScaledConstraint scaledConstraint)
            {
                int originalWidth = baseWidth;
                int originalHeight = baseHeight;
                
                if((float)baseWidth / (float)baseHeight > scaledConstraint.getAspectRatio())
                    baseWidth = Math.round(baseHeight * scaledConstraint.getAspectRatio());
                else if((float)baseWidth / (float)baseHeight < scaledConstraint.getAspectRatio())
                    baseHeight = Math.round(baseWidth / scaledConstraint.getAspectRatio());
                
                baseX += Math.round((originalWidth - baseWidth) * scaledConstraint.getAlignment().getXLocation());
                baseY += Math.round((originalHeight - baseHeight) * scaledConstraint.getAlignment().getYLocation());
            }

            baseWidth = Math.clamp(baseWidth, 0, panelWidth);
            baseHeight = Math.clamp(baseHeight, 0, panelHeight);
            baseX = Math.clamp(baseX, 0, panelWidth - baseWidth);
            baseY = Math.clamp(baseY, 0, panelHeight - baseHeight);
            
            if(component instanceof Container container && container.getLayout() instanceof ScaledLayout scaledLayout)
                scaledLayout.setReferenceSize(baseWidth, baseHeight);
            
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
            if(component instanceof Container container)
                container.doLayout();
        
        Map<Class<?>, Float> largestPossibleFonts = FontFitter.getFontFittingSizes(scaledFonts);
        
        for(Entry<Component, Font> entry : scaledFonts.entrySet())
        {
            Component component = entry.getKey();
            
            component.setFont(component.getFont().deriveFont(largestPossibleFonts.get(component.getClass())));
        }
    }
    
    private void setReferenceSize(int width, int height)
    {
        if(isAutomatic)
        {
            panelWidth = width;
            panelHeight = height;
        }
    }
}