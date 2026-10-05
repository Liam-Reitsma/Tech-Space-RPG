package game.options.view;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.config.DisplayableOverlay;
import game.options.controller.OptionsAction;

import javax.swing.*;
import java.awt.*;

public class OptionsOverlay extends JPanel implements DisplayableOverlay
{
    public OptionsOverlay(OptionsAction optionsAction)
    {
        setLayout(new ScaledLayout());
        
        LayoutBox overlayBox = new LayoutBox(0, 0, 100, 100);
        LayoutBox[] boxes = overlayBox.splitVertical(22, 78);
        add(new OptionsTitlePanel(optionsAction), boxes[0]);
        add(new OptionsPanel(optionsAction), boxes[1]);
        
        setOpaque(false);
    }
    
    @Override
    public JPanel getDisplay()
    {
        setVisible(false);
        return this;
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