package game.frame.view;

import game.frame.controller.DisplayTarget;
import game.gui.config.DisplayResolution;
import game.gui.errors.DisplayError;

import java.awt.event.ActionListener;

public interface FrameViewAction
{
    void displayError(DisplayError error, ActionListener leftButton, ActionListener rightButton);
    void display(DisplayTarget display);
    void closeOverlay();
    void closeError();
    void resizeGameFrame(DisplayResolution resolution);
    void closeGameFrame();
}