package game.gui.errors;

import game.gui.common.PanelButton;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;

import javax.swing.*;

public class ErrorOverlay extends JPanel
{
    public ErrorOverlay(String message, PanelButton leftButton, PanelButton rightButton)
    {
        setLayout(new ScaledLayout());
        LayoutBox[] boxes = new LayoutBox().splitVertical(80, 20);
        add(new JTextArea(message), boxes[0]);
        boxes = boxes[1].splitHorizontal(50, 50);
        
        add(leftButton, boxes[0]);
        add(rightButton, boxes[1]);
    }
}