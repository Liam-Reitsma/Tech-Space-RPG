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
            if(text == null || text.isEmpty())
                return font.getSize2D();
            
            FontMetrics fontMetrics = button.getFontMetrics(font);
            Insets inset = button.getInsets();
            
            float availableWidth = button.getWidth() - (inset.left + inset.right);
            float availableHeight = button.getHeight() - (inset.top + inset.bottom);
            
            if(availableWidth <= 0 || availableHeight <= 0)
                return font.getSize2D();
            
            String result;
            float fontSize = font.getSize2D();
            
            do
            {
                Rectangle viewRect = new Rectangle(inset.left, inset.top, Math.round(availableWidth), Math.round(availableHeight));
                Rectangle iconRect = new Rectangle();
                Rectangle textRect = new Rectangle();
                
                result = SwingUtilities.layoutCompoundLabel
                        (
                                button, fontMetrics, button.getText(), button.getIcon(), button.getVerticalAlignment(),
                                button.getHorizontalAlignment(), button.getVerticalTextPosition(), button.getHorizontalTextPosition(),
                                viewRect, iconRect, textRect, button.getIconTextGap()
                        );
                
                if(result.equals(text))
                    return fontSize;
                
                fontSize = Math.max(fontSize - 1f, 1f);
                fontMetrics = button.getFontMetrics(font.deriveFont(fontSize));
            } while(fontSize > 1f);
            
            return fontSize;
        }
        
        return font.getSize2D();
    }
}