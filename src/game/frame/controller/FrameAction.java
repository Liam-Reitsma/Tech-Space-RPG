package game.frame.controller;

import game.gui.config.DisplayResolution;
import game.gui.errors.DisplayError;
import game.player.view.ItemSlot;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public interface FrameAction
{
    void requestCloseOverlay();
    void requestCloseError();
    void requestDisplay(DisplayTarget display);
    void requestDisplayDrag(MouseEvent e, JComponent drag);
    void requestDisplayError(DisplayError error, ActionListener leftButton, ActionListener rightButton);
    void requestRemoveDrag(JComponent drag);
    void requestResolutionChange(DisplayResolution resolution);
}