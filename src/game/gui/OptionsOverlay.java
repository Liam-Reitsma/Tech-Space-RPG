package game.gui;

import javax.swing.*;

public class OptionsOverlay extends JPanel
{
    public OptionsOverlay(OptionsPanel optionsPanel)
    {
        add(optionsPanel);
        setSize(optionsPanel.getSize());
        setOpaque(false);
        setVisible(true);
    }
}