package game.mainmenu.view;

import static game.frame.controller.DisplayTarget.*;
import static game.gui.common.scaledlayout.ScaledLayoutAnchor.CENTER;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.config.DisplayableScreen;
import game.mainmenu.controller.MainMenuAction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainMenuScreen extends JPanel implements DisplayableScreen
{
    private static final Image MAIN_MENU_IMAGE = new ImageIcon("resources/images/Main Menu Background.png").getImage();
    
    private final MainMenuAction mainMenuAction;
    
    public MainMenuScreen(String title, MainMenuAction mmAction)
    {
        setLayout(new ScaledLayout());
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
        
        int i = 0;
        add(new MainMenuButton(NEW_GAME.toString(), e -> mainMenuAction.requestOpenDisplay(NEW_GAME)), new LayoutBox(boxes[i++], 30, 70, CENTER));
        add(new MainMenuButton(SAVES.toString(), e -> mainMenuAction.requestOpenDisplay(SAVES)), new LayoutBox(boxes[i++], 30, 70, CENTER));
        add(new MainMenuButton(OPTIONS.toString(), e -> mainMenuAction.requestOpenDisplay(OPTIONS)), new LayoutBox(boxes[i++], 30, 70, CENTER));
        add(new MainMenuButton(EXIT.toString(), e -> mainMenuAction.requestOpenDisplay(EXIT)), new LayoutBox(boxes[i++], 30, 70, CENTER));
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(MAIN_MENU_IMAGE, 0, 0, getWidth(), getHeight(), null);
    }
    
    @Override
    public JPanel getDisplay()
    {
        return this;
    }
    
    @Override
    public void openDisplay()
    {
        mainMenuAction.requestOpenMainMenu();
        setVisible(true);
    }
    
    @Override
    public void closeDisplay()
    {
        setVisible(false);
    }
}