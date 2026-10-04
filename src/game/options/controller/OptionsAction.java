package game.options.controller;

import game.gui.config.DisplayResolution;

public interface OptionsAction
{
    void requestOpenOptionsOverlay();
    void requestResolutionChange(DisplayResolution resolution);
    void requestCloseOptions();
}