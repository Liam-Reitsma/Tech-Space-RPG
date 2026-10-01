package game.gui.maingame;

import javax.swing.*;
import java.awt.*;

public class MainGameScreen extends JPanel
{
    private final Image MAIN_BACKGROUND = new ImageIcon("resources/images/Main Game.png").getImage();
    
    private JPanel combatPanel;
    private JPanel mapPanel;
    private JPanel playerPanel;
    private JPanel menuPanel;
    
    private JButton northButton;
    private JButton eastButton;
    private JButton southButon;
    private JButton westButton;
    
    public MainGameScreen()
    {
        //gay cunt
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawImage(MAIN_BACKGROUND,0, 0, getWidth(), getHeight(), null);
    }
}
