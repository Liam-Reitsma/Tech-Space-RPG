package game.gui.common;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel
{
    private final Image image;
    
    public ImagePanel(LayoutManager layoutManager, Image imageToSet)
    {
        super(layoutManager);
        image = imageToSet;
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}
