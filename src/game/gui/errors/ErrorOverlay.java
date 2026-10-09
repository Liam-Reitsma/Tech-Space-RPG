package game.gui.errors;

import game.gui.common.PanelButton;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.config.Displayable;

import javax.swing.*;

public class ErrorOverlay extends JPanel implements Displayable
{
    public ErrorOverlay(String title, String message, PanelButton leftButton, PanelButton rightButton)
    {
        setLayout(new ScaledLayout());
        setOpaque(false);
        LayoutBox[] boxes = new LayoutBox().splitVertical(20, 60, 20);
        add(new ErrorTitlePanel(title), boxes[0]);
        add(new ErrorTextDisplay(message), boxes[1]);
        boxes = boxes[2].splitHorizontal(50, 50);
        
        add(leftButton, boxes[0]);
        add(rightButton, boxes[1]);
    }
    
    @Override
    public JPanel getDisplay()
    {
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