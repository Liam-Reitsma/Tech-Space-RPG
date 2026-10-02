package game.gui;

import javax.swing.*;
import java.awt.*;

public class OverlayContainer extends JPanel
{
    public OverlayContainer(LayoutManager2 layoutManager)
    {
        setLayout(layoutManager);
        setOpaque(false);
    }
    
    public void setOverlay(Component comp, Object constraints)
    {
        removeAll();
        add(comp, constraints);
    }
}