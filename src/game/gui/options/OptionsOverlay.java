package game.gui.options;

import game.gui.common.scaledlayout.LayoutBox;

import javax.swing.*;
import java.awt.*;

public class OptionsOverlay extends JPanel
{
    public OptionsOverlay(LayoutManager2 layoutManager, OptionsPanel optionsPanel, OptionsTitlePanel optionsTitlePanel)
    {
        setLayout(layoutManager);
        LayoutBox overlayBox = new LayoutBox(0, 0, 100, 100);
        LayoutBox[] boxes = overlayBox.splitVertical(22, 78);
        add(optionsTitlePanel, boxes[0]);
        add(optionsPanel, boxes[1]);
        
        setOpaque(false);
        setVisible(true);
    }
}