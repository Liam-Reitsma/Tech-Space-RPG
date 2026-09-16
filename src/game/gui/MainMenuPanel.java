package game.gui;

import game.mainmenu.MainMenuController;
import game.mainmenu.MainMenuState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel
{
    private static final Image mainMenuImage = new ImageIcon("resources/images/Main Menu Background.png").getImage();
    
    public MainMenuPanel(String title, MainMenuController mmController)
    {
        super(new GridBagLayout());
        
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.NONE;
        gbConstraints.anchor = GridBagConstraints.CENTER;
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 0;
        gbConstraints.insets = new Insets(0, 0, 60, 0);
        
        add(createTitlePanel(title), gbConstraints);
        
        gbConstraints.insets = new Insets(0, 0, 15, 0);
        
        int i = 1;
        for(MainMenuState state : MainMenuState.values())
        {
            gbConstraints.gridy = i++;
            
            ActionListener stateListener = e -> state.executeStateAction(mmController);
            add(new MainMenuButton(state.toString(), stateListener), gbConstraints);
        }
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