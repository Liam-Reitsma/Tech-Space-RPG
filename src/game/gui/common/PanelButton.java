package game.gui.common;

import game.audio.AudioManager;
import game.audio.SoundEffect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PanelButton extends ConfirmButton
{
    private final AudioManager audioManager;
    
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
        setIconTextGap(0);
        
        audioManager = AudioManager.getInstance();
        
        addActionListener(e -> audioManager.playSound(SoundEffect.BUTTON_CLICK_SOUNDBITE));
        
        normalImage = new ImageIcon("resources/images/Apply_Button.png").getImage();
        rolloverImage = new ImageIcon("resources/images/Apply_Button_Rollover.png").getImage();
        pressedImage = new ImageIcon("resources/images/Apply_Button_Pressed.png").getImage();
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