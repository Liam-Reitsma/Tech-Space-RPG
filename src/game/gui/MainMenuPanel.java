package game.gui;

import game.mainmenu.MainMenuController;
import game.mainmenu.MainMenuState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel
{
    private static final Image MAIN_MENU_IMAGE = new ImageIcon("resources/images/Main Menu Background.png").getImage();
    
    private JPanel titlePanel;
    private JPanel buttonPanel;
    
    public MainMenuPanel(String title, MainMenuController mmController)
    {
        super(new GridBagLayout());
        
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.NONE;
        gbConstraints.anchor = GridBagConstraints.CENTER;
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 0;
        gbConstraints.weightx = 1;
        gbConstraints.weighty = 1;
        
        JPanel tPanel = new JPanel();
        tPanel.setOpaque(false);
        tPanel.setLayout(new GridBagLayout());
        
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
        
        titlePanel = tShadowPanel;
        
        tPanel.add(tShadowPanel, gbConstraints);
        
        add(tPanel, gbConstraints);
    }
    
    private void createButtonPanel(MainMenuController mmController, GridBagConstraints gbConstraints)
    {
        gbConstraints.anchor = GridBagConstraints.CENTER;
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.gridx = 1;
        gbConstraints.gridy = 1;
        
        JPanel bPanel = new JPanel(new GridBagLayout());
        bPanel.setOpaque(false);
        createSpacer(bPanel, 0);
        
        GridBagConstraints buttonGBC = new GridBagConstraints();
        buttonGBC.anchor = GridBagConstraints.CENTER;
        buttonGBC.fill = GridBagConstraints.BOTH;
        buttonGBC.insets = new Insets(0, 0, 15, 0);
        int i = 0;
        for(MainMenuState state : MainMenuState.values())
        {
            buttonGBC.gridy = i++;
            ActionListener stateListener = e -> state.executeStateAction(mmController);
            bPanel.add(new MainMenuButton(state.toString(), stateListener), buttonGBC);
        }
        
        buttonPanel = bPanel;
        
        add(bPanel, gbConstraints);
        createSpacer(bPanel, 2);
    }
    
    private void createSpacer(JPanel panel, int gridX)
    {
        GridBagConstraints spacerConstraints = new GridBagConstraints();
        spacerConstraints.gridx = gridX;
        spacerConstraints.gridheight = GridBagConstraints.REMAINDER;
        spacerConstraints.fill = GridBagConstraints.BOTH;
        
        panel.add(Box.createGlue(), spacerConstraints);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(mainMenuImage, 0, 0, getWidth(), getHeight(), null);
    }
    
    private JPanel createTitlePanel(String title)
    {
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        OverlayLayout titlePanelLayout = new OverlayLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Agency FB", Font.BOLD, 100));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        JLabel titleLabelShadow = new JLabel(title);
        titleLabelShadow.setFont(new Font("Agency FB", Font.BOLD, 100));
        titleLabelShadow.setForeground(Color.BLACK);
        titleLabelShadow.setBorder(new EmptyBorder(10, 7, 0, 0));
        titlePanel.add(titleLabelShadow);
        
        return titlePanel;
    }
}