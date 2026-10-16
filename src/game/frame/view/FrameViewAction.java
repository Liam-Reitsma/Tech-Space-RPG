package game.frame.view;

import game.frame.controller.DisplayTarget;
import game.gui.config.DisplayResolution;
import game.gui.errors.DisplayError;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public interface FrameViewAction
{
    void closeError();
    void closeGameFrame();
    void closeOverlay();
    void display(DisplayTarget display);
    void displayError(DisplayError error, ActionListener leftButton, ActionListener rightButton);
    void displayOnOverlay(MouseEvent e, JComponent component);
    void removeFromOverlay(JComponent component);
    void resizeGameFrame(DisplayResolution resolution);
}