package game.maingame.view;

import game.combat.view.CombatPane;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.config.DisplayableScreen;
import game.maingame.controller.GameAction;
import game.maingamemenu.view.MainGameMenuPanel;
import game.world.view.WorldPanel;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel implements GameViewAction, DisplayableScreen
{
    private static final Image MAIN_BACKGROUND = new ImageIcon("resources/images/Main Game.png").getImage();
    
    private JPanel textBackdrop;
    private JPanel statsBackdrop;
    private JPanel inventoryBackdrop;
    private JPanel equipmentBackdrop;
    private JPanel worldBackdrop;
    private JPanel menuBackdrop;
    private JPanel saveGameBackdrop;
    private JPanel loadGameBackdrop;
    private JPanel exitGameBackdrop;
    
    private JTextPane logPane;
    private JTextPane statsPane;
    private JPanel buttonPanel;
    
    private JButton northButton;
    private JButton eastButton;
    private JButton southButton;
    private JButton westButton;
    
    private JButton saveGameButton;
    private JButton loadGameButton;
    private JButton exitGameButton;

    private final CombatPane combatPane;
    private final MainGameMenuPanel mainGameMenuPanel;
    private final WorldPanel worldPane;
    
    private final GameAction gameAction;
    
    public GameScreen(CombatPane cPane, MainGameMenuPanel mgmPanel, WorldPanel wPanel, GameAction gAction)
    {
        setLayout(new ScaledLayout());
        combatPane = cPane;
        mainGameMenuPanel = mgmPanel;
        worldPane = wPanel;
        
        LayoutBox[] boxes = new LayoutBox().splitVertical(90, 10);
        
        createMainPanel(boxes[0]);
        add(mainGameMenuPanel, (boxes[1]));
        
        gameAction = gAction;
    }
    
    private void createMainPanel(LayoutBox box)
    {
        LayoutBox[] boxes = box.splitHorizontal(33, 33, 33);
        
        add(combatPane, boxes[0]);
        createInventoryPane(boxes[1]);
        add(worldPane, boxes[2]);
    }
    
    private void createInventoryPane(LayoutBox box)
    {
        LayoutBox[] verticalBoxes = box.splitVertical(60, 40);
        LayoutBox[] horizontalBoxes = verticalBoxes[0].splitHorizontal(40, 60);
        
        LayoutBox statsBox = horizontalBoxes[0];
        statsPane = new JTextPane();
        statsBackdrop = new JPanel(new ScaledLayout());
        statsBackdrop.add(statsPane);
        add(statsBackdrop, statsBox);
        
        LayoutBox equipmentBox = horizontalBoxes[1];
        equipmentBackdrop = new JPanel(new ScaledLayout());
        add(equipmentBackdrop, equipmentBox);
        
        LayoutBox inventoryBox = verticalBoxes[1];
        inventoryBackdrop = new JPanel(new ScaledLayout());
        add(inventoryBackdrop, inventoryBox);
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
        gameAction.requestOpenGameScreen();
        setVisible(true);
    }
    
    @Override
    public void closeDisplay()
    {
        setVisible(false);
    }
}
