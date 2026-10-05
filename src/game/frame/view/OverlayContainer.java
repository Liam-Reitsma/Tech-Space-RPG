package game.frame.view;

import static game.gui.config.GUIConfig.*;

import game.gui.common.scaledlayout.ScaledLayout;

import javax.swing.*;
import java.awt.*;

public class OverlayContainer extends JPanel
{
    public OverlayContainer()
    {
        setLayout(new ScaledLayout(PROGRAM_WIDTH, PROGRAM_HEIGHT));
        setOpaque(false);
    }
    
    public void setOverlay(Component comp, Object constraints)
    {
        removeAll();
        add(comp, constraints);
        revalidate();
        repaint();
    }
}