package game.frame.controller;

import game.gui.config.DisplayResolution;
import game.gui.errors.DisplayError;

import java.awt.event.ActionListener;

public interface FrameAction
{
    void requestCloseOverlay();
    void requestCloseError();
    void requestDisplay(DisplayTarget display);
    void requestDisplayError(DisplayError error, ActionListener leftButton, ActionListener rightButton);
    void requestResolutionChange(DisplayResolution resolution);
}