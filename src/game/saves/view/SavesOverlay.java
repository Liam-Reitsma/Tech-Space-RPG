package game.saves.view;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.config.DisplayableOverlay;
import game.saves.controller.SavesAction;

import javax.swing.*;
import java.awt.*;

public class SavesOverlay extends JPanel implements SavesViewAction, DisplayableOverlay
{
    SavesPanel savesPanel;
    public SavesOverlay(SavesAction savesAction)
    {
        setLayout(new ScaledLayout());
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
    public void removeSave(String saveName)
    {
        savesPanel.removeSave(saveName);
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