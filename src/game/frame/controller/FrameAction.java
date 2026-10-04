package game.frame.controller;

import game.gui.config.DisplayResolution;
import game.gui.config.Displayable;

public interface FrameAction
{
    void requestResolutionChange(DisplayResolution resolution);
    void display(Displayable displayable);
    void closeOverlay();
    void requestCloseGame();
}