package game.gui.mainmenu;

import static game.gui.config.GUIConfig.PROGRAM_HEIGHT;
import static game.gui.config.GUIConfig.PROGRAM_WIDTH;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.PercentageBounds;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.common.scaledlayout.ScaledLayoutAnchor;
import game.mainmenu.MainMenuAction;
import game.mainmenu.MainMenuState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainMenuScreen extends JPanel
{
    private static final Image MAIN_MENU_IMAGE = new ImageIcon("resources/images/Main Menu Background.png").getImage();
    
    public MainMenuScreen(String title, MainMenuAction mmAction)
    {
        super(new ScaledLayout(PROGRAM_WIDTH, PROGRAM_HEIGHT));
        
        GridBagConstraints gbConstraints = new GridBagConstraints();
        
        createTitlePanel(title);
        createButtonPanel(mmAction);
    }
    
    private void createTitlePanel(String title)
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
        
        add(tShadowPanel, new PercentageBounds(0,0, 100, 40));
    }
    
    private void createButtonPanel(MainMenuAction mmAction)
    {
        LayoutBox buttonLayoutBox = new LayoutBox(0, 40, 100, 60);
        int i = 0;
        for(MainMenuState state : MainMenuState.values())
        {
            LayoutBox buttonBox = new LayoutBox(buttonLayoutBox, 0, 25 * i++, 100, 25);
            add(new MainMenuButton(state.toString(), e -> state.execute(mmAction)),
                    new PercentageBounds(buttonBox, 30, 70, ScaledLayoutAnchor.CENTER));
        }
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(MAIN_MENU_IMAGE, 0, 0, getWidth(), getHeight(), null);
    }
}