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

    /**
     * Rescales the button's normal, rollover and pressed images, as well as its font.
     *
     * @param fWidth The target width of the button images.
     * @param fHeight The target height of the button images.
     */
    public void resizeButton(float fWidth, float fHeight)
    {
        int width = Math.round(fWidth);
        int height = Math.round(fHeight);
        
        setIcon(new ImageIcon(normalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        setRolloverIcon(new ImageIcon(rolloverImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        setPressedIcon(new ImageIcon(pressedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        setFont(getFont().deriveFont(Math.min(fWidth, fHeight) * 0.75f));
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