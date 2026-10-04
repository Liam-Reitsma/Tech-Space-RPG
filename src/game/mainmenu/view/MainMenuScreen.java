package game.mainmenu.view;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.config.DisplayableScreen;
import game.mainmenu.controller.MainMenuAction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainMenuScreen extends JPanel implements MainMenuViewAction, DisplayableScreen
{
    private static final Image MAIN_MENU_IMAGE = new ImageIcon("resources/images/Main Menu Background.png").getImage();
    
    private final MainMenuAction mainMenuAction;
    
    public MainMenuScreen(String title, MainMenuAction mmAction, LayoutManager2 layoutManager)
    {
        super(layoutManager);
        mainMenuAction = mmAction;
        LayoutBox[] boxes = new LayoutBox().splitVertical(40, 60);
        
        createTitlePanel(title, boxes[0]);
        createButtonPanel(boxes[1]);
    }
    
    private void createTitlePanel(String title, LayoutBox box)
    {
        JPanel tShadowPanel = new JPanel();
        tShadowPanel.setOpaque(false);
        tShadowPanel.setLayout(new OverlayLayout(tShadowPanel));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Agency FB", Font.BOLD, 150));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        tShadowPanel.add(titleLabel);
        
        JLabel titleLabelShadow = new JLabel(title);
        titleLabelShadow.setFont(new Font("Agency FB", Font.BOLD, 150));
        titleLabelShadow.setForeground(Color.BLACK);
        titleLabelShadow.setBorder(new EmptyBorder(16, 10, 0, 0));
        titleLabelShadow.setAlignmentX(CENTER_ALIGNMENT);
        tShadowPanel.add(titleLabelShadow, CENTER_ALIGNMENT);
        
        add(tShadowPanel, box);
    }
    
    private void createButtonPanel(LayoutBox box)
    {
        LayoutBox[] boxes = box.splitVertical(25, 25, 25, 25);
        add(new MainMenuButton("New Game", e -> mainMenuAction.requestOpenNewGameScreen()), boxes[0]);
        add(new MainMenuButton("Load Game", e -> mainMenuAction.requestOpenSavesOverlay()), boxes[1]);
        add(new MainMenuButton("Options", e -> mainMenuAction.requestOpenOptionsOverlay()), boxes[2]);
        add(new MainMenuButton("Exit Game", e -> mainMenuAction.requestExitGame()), boxes[3]);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(MAIN_MENU_IMAGE, 0, 0, getWidth(), getHeight(), null);
    }
    
    @Override
    public void openMainMenuScreen()
    {
        setVisible(true);
    }
    
    @Override
    public void closeMainMenuScreen()
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