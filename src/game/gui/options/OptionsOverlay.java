package game.gui.options;

import game.gui.common.scaledlayout.PercentageBounds;

import javax.swing.*;
import java.awt.*;

public class OptionsOverlay extends JPanel
{
    public OptionsOverlay(OptionsPanel optionsPanel, OptionsTitlePanel optionsTitlePanel, LayoutManager2 layoutManager)
    {
        setLayout(layoutManager);
        add(optionsTitlePanel, new PercentageBounds(0, 0, 100, 20));
        add(optionsPanel, new PercentageBounds(0, 20, 100, 80));
        
        setOpaque(false);
        setVisible(true);
    }
}