package game.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class OptionsTitlePanel extends JPanel
{
    private static final Image SETTINGS_IMAGE_TITLE = new ImageIcon("resources/images/Settings Title.png").getImage();
    
    private static final int LABEL_FONT_SIZE = 46;
    
    public OptionsTitlePanel(GUIAction guiAction)
    {
        setLayout(new GridBagLayout());
        GridBagConstraints gbConstraints = new GridBagConstraints();
        
        ActionListener closeButtonListener = e -> guiAction.closeOptionsScreen();
        
        gbConstraints.anchor = GridBagConstraints.WEST;
        gbConstraints.fill = GridBagConstraints.NONE;
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 0;
        
        JLabel label = new JLabel("Settings");
        label.setFont(new Font("Agency FB", Font.BOLD, LABEL_FONT_SIZE));
        label.setForeground(Color.BLACK);
        add(label, gbConstraints);
        
        gbConstraints.anchor = GridBagConstraints.SOUTHEAST;
        gbConstraints.gridx = 1;
        ConfirmButton applyResButton = new ConfirmButton("X", closeButtonListener);
        gbConstraints.weightx = 1;
        add(applyResButton, gbConstraints);
        
        addMouseListener(new MouseAdapter() {});
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(SETTINGS_IMAGE_TITLE, 0, 0, getWidth(), getHeight(), null);
    }
}