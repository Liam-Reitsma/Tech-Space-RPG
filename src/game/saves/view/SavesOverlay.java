package game.saves.view;

import game.gui.common.scaledlayout.LayoutBox;

import javax.swing.*;
import java.awt.*;

public class SavesOverlay extends JPanel implements SavesViewAction
{
    public SavesOverlay(LayoutManager2 layoutManager2, SavesPanel savesPanel, SavesTitlePanel savesTitlePanel)
    {
        setLayout(layoutManager2);
        LayoutBox[] box = new LayoutBox().splitVertical(25, 75);
        add(savesTitlePanel, box[0]);
        add(savesPanel, box[1]);
        
        setOpaque(false);
    }
    
    @Override
    public void openSaveOverlay()
    {
        setVisible(true);
    }
    
    @Override
    public void closeSaveOverlay()
    {
        setVisible(false);
    }
}