package game.options.view;

import game.audio.VolumeType;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.config.DisplayableOverlay;
import game.options.controller.OptionsAction;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class OptionsOverlay extends JPanel implements OptionsViewAction, DisplayableOverlay
{
    private final OptionsPanel optionsPanel;
    
    public OptionsOverlay(OptionsAction oAction)
    {
        setLayout(new ScaledLayout());
        
        LayoutBox overlayBox = new LayoutBox(0, 0, 100, 100);
        LayoutBox[] boxes = overlayBox.splitVertical(22, 78);
        add(new OptionsTitlePanel(oAction), boxes[0]);
        optionsPanel = new OptionsPanel(oAction);
        add(optionsPanel, boxes[1]);
        
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
        optionsPanel.resetValues();
        setVisible(false);
    }
    
    @Override
    public void setNewVolume(Map<VolumeType, Integer> volumeMap)
    {
        optionsPanel.setNewVolume(volumeMap);
    }
}