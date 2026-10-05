package game.gui.common;

import javax.swing.*;
import java.awt.*;

public class PicturePreview extends JComponent
{
    private Image image;
    
    public PicturePreview()
    {
    
    }
    
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
        
        if(image != null)
        {
            int imageWidth = image.getWidth(null);
            int imageHeight = image.getHeight(null);
            
            float scale = Math.min((float) getWidth() / imageWidth, (float) getHeight() / imageHeight);
            
            int drawWidth = Math.round(imageWidth * scale);
            int drawHeight = Math.round(imageHeight * scale);
            
            int x = (getWidth() - drawWidth) / 2;
            int y = (getHeight() - drawHeight) / 2;
            
            g.drawImage(image, x, y, drawWidth, drawHeight, null);
        }
    }
}