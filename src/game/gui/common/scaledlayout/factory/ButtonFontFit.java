package game.gui.common.scaledlayout.factory;

import javax.swing.*;
import java.awt.*;

public class ButtonFontFit implements FontFitStrategy
{
    public ButtonFontFit()
    {
    
    }
    
    @Override
    public float getFittingFontSize(Component comp, Font font)
    {
        if(comp instanceof AbstractButton button)
        {
            String text = button.getText();
            if(text == null || text.isEmpty()) return font.getSize2D();
            
            FontMetrics fontMetrics = button.getFontMetrics(font);
            Insets inset = button.getInsets();
            
            float availableWidth = button.getWidth() - (inset.left + inset.right);
            float availableHeight = button.getHeight() - (inset.top + inset.bottom);
            
            float widthScale = availableWidth / fontMetrics.stringWidth(text);
            float heightScale = availableHeight / fontMetrics.getHeight();
            
            return (Math.min(Math.min(widthScale, heightScale), 1f) * font.getSize2D());
        }
        
        return font.getSize2D();
    }
}