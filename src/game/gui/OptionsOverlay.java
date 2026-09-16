package game.gui;

import javax.swing.*;

public class OptionsOverlay extends JPanel
{
    public OptionsOverlay(OptionsPanel optionsPanel)
    {
        add(optionsPanel);
        setSize(300, 900);
        setOpaque(false);
        setVisible(true);
    }
}