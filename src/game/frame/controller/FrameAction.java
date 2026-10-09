package game.frame.controller;

import game.gui.config.DisplayResolution;
import game.gui.errors.DisplayError;

import java.awt.event.ActionListener;

public interface FrameAction
{
    void requestResolutionChange(DisplayResolution resolution);
    void requestDisplayError(DisplayError error, ActionListener leftButton, ActionListener rightButton);
    void requestDisplay(DisplayTarget display);
    void requestCloseOverlay();
    void requestCloseError();
    void requestCloseGame();
}