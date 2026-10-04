package game.saves.view;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.config.DisplayableOverlay;

import javax.swing.*;
import java.awt.*;

public class SavesOverlay extends JPanel implements SavesViewAction, DisplayableOverlay
{
    private final SavesPanel savesPanel;
    
    public SavesOverlay(LayoutManager2 layoutManager2, SavesPanel sPanel, SavesTitlePanel savesTitlePanel)
    {
        setLayout(layoutManager2);
        LayoutBox[] box = new LayoutBox().splitVertical(25, 75);
        add(savesTitlePanel, box[0]);
        savesPanel = sPanel;
        add(savesPanel, box[1]);
        
        setOpaque(false);
    }
    
    @Override
    public void openSavesOverlay(String[] saveNames)
    {
        savesPanel.updateSavesList(saveNames);
        setVisible(true);
    }
    
    @Override
    public void closeSaveOverlay()
    {
        setVisible(false);
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