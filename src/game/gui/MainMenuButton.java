package game.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenuButton extends JButton
{
    public MainMenuButton(String name, ActionListener actionListener)
    {
        super(name);
        //addActionListener(actionListener);
        setPreferredSize(new Dimension(225, 75));
        setFont(new Font("OCR A Extended", Font.BOLD, 30));
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.BLACK);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g.create();
        
        if(getModel().isPressed())
        {
            setForeground(Color.BLACK);
            g2d.setColor(new Color(240, 200, 100, 255));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
        }
        else if(getModel().isRollover())
        {
            setForeground(Color.BLACK);
            g2d.setColor(new Color(240, 180, 45, 150));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
        }
        else
        {
            setForeground(new Color(160, 160, 160));
            g2d.setColor(new Color(80, 60, 15, 150));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
        }
        
        g2d.dispose();
        
        
        super.paintComponent(g);
    }
}