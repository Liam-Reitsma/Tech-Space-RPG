package game.gui.saves;

import game.gui.common.scaledlayout.LayoutBox;

import javax.swing.*;
import java.awt.*;

public class SavesOverlay extends JPanel
{
    public SavesOverlay(LayoutManager2 layoutManager2, SavesPanel savesPanel, SavesTitlePanel savesTitlePanel)
    {
        setLayout(layoutManager2);
        LayoutBox[] box = new LayoutBox().splitVertical(25, 75);
        add(savesTitlePanel, box[0]);
        add(savesPanel, box[1]);
        
        setOpaque(false);
        setVisible(true);
    }
}