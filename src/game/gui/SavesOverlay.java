package game.gui;

import javax.swing.*;
import java.awt.*;

public class SavesOverlay extends JPanel
{
    public SavesOverlay(SavesPanel savesPanel, SavesTitlePanel savesTitlePanel, int programWidth, int programHeight)
    {
        JPanel overlay = new JPanel();
        overlay.setLayout(new BorderLayout());
        overlay.add(savesTitlePanel, BorderLayout.NORTH);
        overlay.add(savesPanel, BorderLayout.CENTER);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.NONE;
        add(overlay, gbConstraints);
        
        setPreferredSize(new Dimension(programWidth, programHeight));
        setSize(getPreferredSize());
        setOpaque(false);
        setVisible(true);
    }
}