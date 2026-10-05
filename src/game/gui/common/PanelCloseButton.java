package game.gui.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PanelCloseButton extends ConfirmButton
{
    private final Image normalImage;
    private final Image rolloverImage;
    private final Image pressedImage;
    
    public PanelCloseButton(String name, ActionListener actionListener)
    {
        super(name, actionListener);
        
        setFont(new Font("OCR A Extended", Font.BOLD, 30));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.white);
        
        normalImage = new ImageIcon("resources/images/Button_X_Normal.png").getImage();
        rolloverImage = new ImageIcon("resources/images/Button_X_Hover.png").getImage();
        pressedImage = new ImageIcon("resources/images/Button_X_Pressed.png").getImage();
    }
    
    protected void paintComponent(Graphics g)
    {
        Image image;
        
        if(getModel().isPressed())
            image = pressedImage;
        else if(getModel().isRollover())
            image = rolloverImage;
        else
            image = normalImage;
        
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        
        super.paintComponent(g);
    }
}