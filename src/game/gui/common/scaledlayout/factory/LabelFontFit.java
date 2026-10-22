package game.gui.common.scaledlayout.factory;

import javax.swing.*;
import java.awt.*;

public class LabelFontFit implements FontFitStrategy
{
    public LabelFontFit()
    {
    
    }
    
    @Override
    public float getFittingFontSize(Component comp, Font font)
    {
        if(comp instanceof JLabel label)
        {
            String text = label.getText();
            if(text == null || text.isEmpty())
                return font.getSize2D();
            
            FontMetrics fontMetrics = label.getFontMetrics(font);
            
            float availableWidth = label.getWidth();
            float availableHeight = label.getHeight();
            
            if(availableWidth <= 0 || availableHeight <= 0)
                return font.getSize2D();
            
            float widthScale = availableWidth / Math.max(fontMetrics.stringWidth(text), 1f);
            float heightScale = availableHeight / Math.max(fontMetrics.getHeight(), 1f);
            
            return (Math.min(Math.min(widthScale, heightScale), 1f) * font.getSize2D());
        }
        
        return font.getSize2D();
    }
}