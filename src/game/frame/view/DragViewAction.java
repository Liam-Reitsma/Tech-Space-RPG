package game.frame.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public interface DragViewAction
{
    void displayOnOverlay(MouseEvent e, JComponent component);
    Component getDropTarget(MouseEvent e);
    void removeFromOverlay(JComponent component);
}