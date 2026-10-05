package game.maingame.view;

import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.config.DisplayableScreen;
import game.maingame.controller.GameAction;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel implements GameViewAction, DisplayableScreen
{
    private static final Image MAIN_BACKGROUND = new ImageIcon("resources/images/Main Game.png").getImage();
    
    private JPanel combatPanel;
    private JPanel mapPanel;
    private JPanel playerPanel;
    private JPanel menuPanel;
    
    private JButton northButton;
    private JButton eastButton;
    private JButton southButon;
    private JButton westButton;
    
    private final GameAction gameAction;
    
    public GameScreen(GameAction gAction)
    {
        setLayout(new ScaledLayout());
        
        gameAction = gAction;
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawImage(MAIN_BACKGROUND,0, 0, getWidth(), getHeight(), null);
    }
    
    @Override
    public void updateMainGameScreen()
    {
    
    }
    
    @Override
    public JPanel getDisplay()
    {
        setVisible(false);
        return this;
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
