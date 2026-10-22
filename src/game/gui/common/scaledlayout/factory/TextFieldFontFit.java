package game.gui.common.scaledlayout.factory;

import javax.swing.*;
import java.awt.*;

public class TextFieldFontFit implements FontFitStrategy
{
    public TextFieldFontFit()
    {
    
    }
    
    @Override
    public float getFittingFontSize(Component comp, Font font)
    {
        if(comp instanceof JTextField textField)
        {
            String text = textField.getText();
            if(text == null || text.isEmpty())
                return font.getSize2D();
            
            FontMetrics fontMetrics = textField.getFontMetrics(font);
            Insets inset = textField.getInsets();
            
            float availableWidth = textField.getWidth() - (inset.left + inset.right);
            float availableHeight = textField.getHeight() - (inset.top + inset.bottom);
            
            if(availableWidth <= 0 || availableHeight <= 0)
                return font.getSize2D();
            
            float widthScale = availableWidth / Math.max(fontMetrics.stringWidth(text), 1f);
            float heightScale = availableHeight / Math.max(fontMetrics.getHeight(), 1f);
            
            return (Math.min(Math.min(widthScale, heightScale), 1f) * font.getSize2D());
        }
        
        return font.getSize2D();
    }
}