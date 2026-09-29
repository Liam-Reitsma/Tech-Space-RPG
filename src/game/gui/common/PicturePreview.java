package game.gui.common;

import javax.swing.*;
import java.awt.*;

public class PicturePreview extends JComponent
{
    private Image image;
    
    public PicturePreview(Image im)
    {
        image = im;
    }
    
    public void setImage(Image newImage)
    {
        image = newImage;
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }
}
