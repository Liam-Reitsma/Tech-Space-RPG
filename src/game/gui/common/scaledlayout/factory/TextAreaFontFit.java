package game.gui.common.scaledlayout.factory;

import javax.swing.*;
import java.awt.*;

public class TextAreaFontFit implements FontFitStrategy
{
    public TextAreaFontFit()
    {
    
    }
    
    @Override
    public float getFittingFontSize(Component comp, Font font)
    {
        if(comp instanceof JTextArea textArea)
        {
            String text = textArea.getText();
            if(text == null || text.isEmpty()) return font.getSize2D();
            
            float fontSize = font.getSize2D();
            
            JTextArea testTextArea = new JTextArea(text);
            testTextArea.setFont(font.deriveFont(fontSize));
            testTextArea.setLineWrap(textArea.getLineWrap());
            testTextArea.setWrapStyleWord(textArea.getWrapStyleWord());
            testTextArea.setSize(textArea.getWidth(), Integer.MAX_VALUE);
            
            
            if(testTextArea.getPreferredSize().height > textArea.getHeight())
            {
                while(fontSize > 1f)
                {
                    fontSize--;
                    testTextArea.setFont(font.deriveFont(fontSize));
                    
                    if(testTextArea.getPreferredSize().height <= textArea.getHeight())
                        return fontSize;
                }
            }
            
            return fontSize;
        }
        
        return font.getSize2D();
    }
}