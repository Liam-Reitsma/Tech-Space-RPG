package game.gui.popup;

import game.gui.common.PanelButton;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.config.Displayable;

import javax.swing.*;
import java.awt.*;

public class PopupOverlay extends JPanel implements Displayable
{
    public PopupOverlay(String title, String message, PanelButton leftButton, PanelButton rightButton)
    {
        setLayout(new ScaledLayout());
        setOpaque(false);
        LayoutBox[] boxes = new LayoutBox().insets(1).splitVertical(20, 60, 20);
        add(new PopupTitlePanel(title), boxes[0]);
        add(new PopupTextDisplay(message), boxes[1]);
        boxes = boxes[2].splitHorizontal(50, 50);
        
        add(leftButton, boxes[0]);
        add(rightButton, boxes[1]);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
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