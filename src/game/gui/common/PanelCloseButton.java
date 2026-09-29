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
        
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.CENTER);
        
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        
        normalImage = new ImageIcon("resources/images/Button_X_Normal.png").getImage();
        rolloverImage = new ImageIcon("resources/images/Button_X_Hover.png").getImage();
        pressedImage = new ImageIcon("resources/images/Button_X_Pressed.png").getImage();
        
        setIcon(new ImageIcon(normalImage));
        setRolloverIcon(new ImageIcon(rolloverImage));
        setPressedIcon(new ImageIcon(pressedImage));
    }
}