package game.options.view;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.config.DisplayableOverlay;

import javax.swing.*;
import java.awt.*;

public class OptionsOverlay extends JPanel implements DisplayableOverlay
{
    public OptionsOverlay(LayoutManager2 layoutManager, OptionsPanel optionsPanel, OptionsTitlePanel optionsTitlePanel)
    {
        setLayout(layoutManager);
        LayoutBox overlayBox = new LayoutBox(0, 0, 100, 100);
        LayoutBox[] boxes = overlayBox.splitVertical(22, 78);
        add(optionsTitlePanel, boxes[0]);
        add(optionsPanel, boxes[1]);
        
        setOpaque(false);
    }
    
    @Override
    public void openDisplay()
    {
        setVisible(true);
    }
    
    @Override
    public void closeDisplay()
    {
        setVisible(false);
    }
}