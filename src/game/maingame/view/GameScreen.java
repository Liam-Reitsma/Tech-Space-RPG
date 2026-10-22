package game.maingame.view;

import game.combat.view.CombatPane;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.config.DisplayableScreen;
import game.maingame.controller.GameAction;
import game.maingamemenu.view.MainGameMenuPanel;
import game.player.view.PlayerPanel;
import game.world.view.WorldPanel;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel implements DisplayableScreen
{
    private static final Image MAIN_BACKGROUND = new ImageIcon("resources/images/Main Game.png").getImage();

    private final CombatPane combatPane;
    private final MainGameMenuPanel mainGameMenuPanel;
    private final PlayerPanel playerPanel;
    private final WorldPanel worldPane;
    
    private final GameAction gameAction;
    
    public GameScreen(CombatPane cPane, MainGameMenuPanel mgmPanel, PlayerPanel pPanel, WorldPanel wPanel, GameAction gAction)
    {
        setLayout(new ScaledLayout());
        combatPane = cPane;
        mainGameMenuPanel = mgmPanel;
        playerPanel = pPanel;
        worldPane = wPanel;
        
        LayoutBox[] verticalBoxes = new LayoutBox().splitVertical(90, 10);
        
        LayoutBox[] boxes = verticalBoxes[0].splitHorizontal(33, 33, 33);
        
        add(combatPane, boxes[0]);
        add(playerPanel, boxes[1]);
        add(worldPane, boxes[2]);
        add(mainGameMenuPanel, verticalBoxes[1]);
        
        gameAction = gAction;
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(MAIN_BACKGROUND,0, 0, getWidth(), getHeight(), null);
    }

    /**
     * Returns the game screen panel while making it invisible.
     *
     * @return The JPanel instance.
     */
    @Override
    public JPanel getDisplay()
    {
        setVisible(false);
        return this;
    }

    /**
     * Makes the screen visible and triggers the screen's soundbite and ambient music.
     */
    @Override
    public void openDisplay()
    {
        gameAction.requestOpenGameScreen();
        setVisible(true);
    }
    
    @Override
    public void closeDisplay()
    {
        setVisible(false);
    }
}
