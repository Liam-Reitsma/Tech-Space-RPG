package game.gui.common;

import java.awt.*;

public class FontScaler
{
    private FontScaler()
    {
    
    }
    
    public static void scaleFont(Component component, Font font, float fontScale)
    {
        component.setFont(font.deriveFont(font.getSize2D() * fontScale));
    }
}