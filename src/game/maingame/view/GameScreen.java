package game.maingame.view;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.common.scaledlayout.ScaledLayoutAnchor;
import game.gui.config.DisplayableScreen;
import game.maingame.controller.GameAction;

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
    private JPanel worldMap;
    private JPanel buttonPanel;
    
    private JButton northButton;
    private JButton eastButton;
    private JButton southButton;
    private JButton westButton;
    
    private JButton saveGameButton;
    private JButton loadGameButton;
    private JButton exitGameButton;
    
    private final GameAction gameAction;
    
    public GameScreen(GameAction gAction)
    {
        setLayout(new ScaledLayout());
        
        LayoutBox[] boxes = new LayoutBox().splitVertical(90, 10);
        
        createMainPanel(boxes[0]);
        createBottomMenu(boxes[1]);
        
        gameAction = gAction;
    }
    
    private void createMainPanel(LayoutBox box)
    {
        LayoutBox[] boxes = box.splitHorizontal(33, 33, 33);
        
        createTextPane(boxes[0]);
        createInventoryPane(boxes[1]);
        createWorldPane(boxes[2]);
    }
    
    private void createTextPane(LayoutBox box)
    {
        logPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(logPane);
        textBackdrop = new JPanel(new ScaledLayout());
        textBackdrop.add(scrollPane);
        
        add(textBackdrop, box);
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
    
    private void createWorldPane(LayoutBox box)
    {
        LayoutBox[] boxes = box.splitVertical(50, 50);
        
        LayoutBox worldBox = boxes[0];
        worldBackdrop = new JPanel(new ScaledLayout());
        worldMap = new JPanel(new ScaledLayout());
        worldBackdrop.add(worldMap, worldBox.insets(15));
        add(worldBackdrop, worldBox);
        
        LayoutBox movementBox = boxes[1];
        LayoutBox buttonBox = new LayoutBox().insets(15);
        buttonPanel = new JPanel(new ScaledLayout());
        northButton = new JButton("North");
        eastButton = new JButton("East");
        southButton = new JButton("South");
        westButton = new JButton("West");
        
        buttonPanel.add(northButton, new LayoutBox(buttonBox, 30, 30, ScaledLayoutAnchor.NORTH));
        buttonPanel.add(eastButton, new LayoutBox(buttonBox, 30, 30, ScaledLayoutAnchor.EAST));
        buttonPanel.add(southButton, new LayoutBox(buttonBox, 30, 30, ScaledLayoutAnchor.SOUTH));
        buttonPanel.add(westButton, new LayoutBox(buttonBox, 30, 30, ScaledLayoutAnchor.WEST));
        
        add(buttonPanel, movementBox);
    }
    
    private void createBottomMenu(LayoutBox box)
    {
        LayoutBox[] boxes = new LayoutBox().splitHorizontal(33, 33, 33);
        
        menuBackdrop = new JPanel(new ScaledLayout());
        
        LayoutBox saveGameBox = boxes[0];
        saveGameBackdrop = new JPanel(new ScaledLayout());
        saveGameButton = new JButton("Save Game");
        saveGameBackdrop.add(saveGameButton, new LayoutBox().insets(20, 0));
        menuBackdrop.add(saveGameBackdrop, saveGameBox);
        
        LayoutBox loadGameBox = boxes[1];
        loadGameBackdrop = new JPanel(new ScaledLayout());
        loadGameButton = new JButton("Load Game");
        loadGameBackdrop.add(loadGameButton, new LayoutBox().insets(20, 0));
        menuBackdrop.add(loadGameBackdrop, loadGameBox);
        
        LayoutBox exitGameBox = boxes[2];
        exitGameBackdrop = new JPanel(new ScaledLayout());
        exitGameButton = new JButton("Exit Game");
        exitGameBackdrop.add(exitGameButton, new LayoutBox().insets(20, 0));
        menuBackdrop.add(exitGameBackdrop, exitGameBox);
        
        add(menuBackdrop, box);
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
