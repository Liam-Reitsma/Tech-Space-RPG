package game.gui.common.scaledlayout.factory;

import javax.swing.*;
import java.awt.*;

public class ComboBoxFontFit implements FontFitStrategy
{
    public ComboBoxFontFit()
    {
    
    }
    
    @Override
    public float getFittingFontSize(Component comp, Font font)
    {
        if(comp instanceof JComboBox<?> comboBox)
        {
            FontMetrics fontMetrics = comboBox.getFontMetrics(font);
            Insets inset = comboBox.getInsets();
            
            int arrowWidth = 0;
            
            for(Component child : comboBox.getComponents())
            {
                if(child instanceof AbstractButton)
                {
                    arrowWidth = child.getWidth();
                    break;
                }
            }
            
            float availableWidth = comboBox.getWidth() - (inset.left + inset.right + arrowWidth);
            float availableHeight = comboBox.getHeight() - (inset.top + inset.bottom);
            
            float smallestFont = Float.MAX_VALUE;
            int count = comboBox.getItemCount();
            for(int i = 0; i < count; i++)
            {
                Object item = comboBox.getItemAt(i);
                if(item == null) continue;
                String text = item.toString();
                if(text.isEmpty()) continue;
                
                float widthScale = availableWidth / fontMetrics.stringWidth(text);
                float heightScale = availableHeight / fontMetrics.getHeight();
                
                smallestFont = Math.min(smallestFont, Math.min(widthScale, heightScale));
            }
            
            return (Math.min(smallestFont, 1f) * font.getSize2D());
        }
        
        return font.getSize2D();
    }
}