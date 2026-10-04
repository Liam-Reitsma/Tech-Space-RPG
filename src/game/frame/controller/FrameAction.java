package game.frame.controller;

import game.gui.config.DisplayResolution;

public interface FrameAction
{
    void requestResolutionChange(DisplayResolution resolution);
    void requestCloseGame();
}