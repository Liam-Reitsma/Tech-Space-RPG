package game.gui.common.scaledlayout;

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