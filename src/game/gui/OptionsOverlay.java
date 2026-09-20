package game.gui;

import javax.swing.*;
import java.awt.*;

public class OptionsOverlay extends JPanel
{
    public OptionsOverlay(OptionsPanel optionsPanel, OptionsTitlePanel optionsTitlePanel, int programWidth, int programHeight)
    {
        JPanel overlay = new JPanel();
        overlay.setLayout(new BorderLayout());
        overlay.add(optionsTitlePanel, BorderLayout.NORTH);
        overlay.add(optionsPanel, BorderLayout.CENTER);
        setLayout(new GridBagLayout());
        add(overlay, new GridBagConstraints());
        
        setPreferredSize(new Dimension(programWidth, programHeight));
        setSize(getPreferredSize());
        setOpaque(false);
        setVisible(true);
    }
}