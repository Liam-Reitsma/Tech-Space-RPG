package game.gui.popup;

import javax.swing.*;
import java.awt.*;

public class PopupTextDisplay extends JTextArea
{
    private static final Image IMAGE = new ImageIcon("resources/images/Options Content.png").getImage();
    
    public PopupTextDisplay(String message)
    {
        super(message);
        setLineWrap(true);
        setWrapStyleWord(true);
        setFont(new Font("OCR Extended A", Font.PLAIN, 750 / Math.max(1, message.length())));
        setForeground(Color.WHITE);
        setOpaque(false);
    }
    
    protected void paintComponent(Graphics g)
    {
        g.drawImage(IMAGE, 0, 0, getWidth(), getHeight(), this);
        super.paintComponent(g);
    }
}