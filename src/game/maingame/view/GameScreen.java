package game.maingame.view;

import game.gui.config.DisplayableScreen;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel implements GameViewAction, DisplayableScreen
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
    
    public GameScreen()
    {
    
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawImage(MAIN_BACKGROUND,0, 0, getWidth(), getHeight(), null);
    }
    
    @Override
    public void openMainGameScreen()
    {
        setVisible(true);
    }
    
    @Override
    public void updateMainGameScreen()
    {
    
    }
    
    @Override
    public void closeMainGameScreen()
    {
        setVisible(false);
    }
    
    @Override
    public void openDisplay()
    {
        setVisible(true);
    }
    
    @Override
    public void closeDisplay()
    {
        setVisible(false);
    }
}
