package game.frame.view;

import game.frame.controller.DisplayTarget;
import game.gui.config.DisplayResolution;
import game.gui.errors.DisplayError;

import java.awt.event.ActionListener;

public interface FrameViewAction
{
    void closeError();
    void closeGameFrame();
    void closeOverlay();
    void display(DisplayTarget display);
    void displayError(DisplayError error, ActionListener leftButton, ActionListener rightButton);
    void resizeGameFrame(DisplayResolution resolution);
}