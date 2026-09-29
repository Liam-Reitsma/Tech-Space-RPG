package game.gui.saves;

import game.gui.common.PanelCloseButton;
import game.saves.SavesAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class SavesTitlePanel extends JPanel
{
    private static final Image SAVES_TITLE_CONTENT = new ImageIcon("resources/images/Saves Title Content.png").getImage();
    private static final Image SAVES_TITLE_BORDER = new ImageIcon("resources/images/Saves Title Border.png").getImage();
    
    private static final int SAVES_TITLE_IMAGE_TOTAL_WIDTH = SAVES_TITLE_BORDER.getWidth(null);
    private static final int SAVES_TITLE_IMAGE_TOTAL_HEIGHT = SAVES_TITLE_BORDER.getHeight(null);
    
    private static final int SAVES_TITLE_IMAGE_BORDER_WIDTH = (SAVES_TITLE_IMAGE_TOTAL_WIDTH - SAVES_TITLE_CONTENT.getWidth(null)) / 2;
    private static final int SAVES_TITLE_IMAGE_BORDER_HEIGHT = (SAVES_TITLE_IMAGE_TOTAL_HEIGHT - SAVES_TITLE_CONTENT.getHeight(null)) / 2;
    
    private static final int LABEL_FONT_SIZE = 46;
    public SavesTitlePanel(SavesAction saveSaction)
    {
        setLayout(new GridBagLayout());
        GridBagConstraints gbConstraints = new GridBagConstraints();
        
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        
        ActionListener closeButtonListener = e -> saveSaction.requestCloseSaves();
        
        gbConstraints.anchor = GridBagConstraints.WEST;
        gbConstraints.fill = GridBagConstraints.NONE;
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 0;
        
        JLabel label = new JLabel("Save Games");
        label.setFont(new Font("Agency FB", Font.BOLD, LABEL_FONT_SIZE));
        label.setForeground(Color.WHITE);
        add(label, gbConstraints);
        
        gbConstraints.anchor = GridBagConstraints.EAST;
        gbConstraints.gridx = 1;
        PanelCloseButton selectSaveButton = new PanelCloseButton("X", closeButtonListener);
        gbConstraints.weightx = 1;
        add(selectSaveButton, gbConstraints);
        
        addMouseListener(new MouseAdapter() {});
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(SAVES_TITLE_BORDER, 0, 0, getWidth(), getHeight(), null);
        
        int insetX = (int)(getWidth() * ((double)SAVES_TITLE_IMAGE_BORDER_WIDTH / SAVES_TITLE_IMAGE_TOTAL_WIDTH));
        int insetY = (int)(getHeight() * ((double)SAVES_TITLE_IMAGE_BORDER_HEIGHT / SAVES_TITLE_IMAGE_TOTAL_HEIGHT));
        
        g.drawImage(SAVES_TITLE_CONTENT, insetX, insetY, getWidth() - insetX * 2, getHeight() - insetY * 2, null);
    }
}