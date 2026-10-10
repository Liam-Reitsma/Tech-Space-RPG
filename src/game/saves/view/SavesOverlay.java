package game.saves.view;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.config.DisplayableOverlay;
import game.saves.controller.SavesAction;

import javax.swing.*;
import java.awt.*;

public class SavesOverlay extends JPanel implements SavesViewAction, DisplayableOverlay
{
    private final SavesAction savesAction;
    
    private final SavesPanel savesPanel;
    
    public SavesOverlay(SavesAction sAction)
    {
        setLayout(new ScaledLayout());
        savesAction = sAction;
        LayoutBox[] box = new LayoutBox().splitVertical(25, 75);
        add(new SavesTitlePanel(savesAction), box[0]);
        savesPanel = new SavesPanel(savesAction);
        add(savesPanel, box[1]);
        
        setOpaque(false);
    }
    
    @Override
    public void updateSavesList(String[] saveNames)
    {
        savesPanel.updateSavesList(saveNames);
    }
    
    @Override
    public void removeSave(int saveIndex)
    {
        savesPanel.removeSave(saveIndex);
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
        savesAction.requestOpenSavesOverlay();
        setVisible(true);
    }
    
    @Override
    public void closeDisplay()
    {
        setVisible(false);
    }
}