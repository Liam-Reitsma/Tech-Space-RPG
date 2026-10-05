    package game.gui.common.scaledlayout.factory;
    
    import java.awt.*;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Map.Entry;
    
    public class FontFitter
{
    private FontFitter()
    {
    
    }
    
    public static Map<Class<?>, Float> getFontFittingSizes(Map<Component, Font> fonts)
    {
        Map<Class<?>, Float> fittingSizes = new HashMap<>();
        
        for(Entry<Component, Font> entry : fonts.entrySet())
        {
            Component component = entry.getKey();
            Font font = entry.getValue();
            float size = getFontFittingSize(component, font);
            
            if(fittingSizes.getOrDefault(component.getClass(), Float.MAX_VALUE) > size)
                fittingSizes.put(component.getClass(), size);
        }
        
        return fittingSizes;
    }
    
    private static float getFontFittingSize(Component component, Font font)
    {
        FontFitStrategy fontFit = FontFitFactory.createFontFit(component);
        
        return fontFit == null ? font.getSize2D() : fontFit.getFittingFontSize(component, font);
    }
}
