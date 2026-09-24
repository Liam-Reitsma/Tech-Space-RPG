package game.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PanelButton extends ConfirmButton
{
    private final Image normalImage;
    private final Image rolloverImage;
    private final Image pressedImage;
    
    public PanelButton(String name, ActionListener actionListener)
    {
        super(name, actionListener);
        setFont(new Font("OCR A Extended", Font.BOLD, 30));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.CENTER);
        
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        
        setIconTextGap(0);
        
        normalImage = new ImageIcon("resources/images/Apply_Button.png").getImage();
        rolloverImage = new ImageIcon("resources/images/Apply_Button_Rollover.png").getImage();
        pressedImage = new ImageIcon("resources/images/Apply_Button_Pressed.png").getImage();
    }
    
    public void resizeButton(float fWidth, float fHeight)
    {
        int width = Math.round(fWidth);
        int height = Math.round(fHeight);
        
        setIcon(new ImageIcon(normalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        setRolloverIcon(new ImageIcon(rolloverImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        setPressedIcon(new ImageIcon(pressedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        setFont(getFont().deriveFont(Math.min(fWidth, fHeight) * 0.75f));
    }
}