package game.options.controller;

import game.gui.config.DisplayResolution;

public interface OptionsAction
{
    void requestOpenOptions();
    void requestResolutionChange(DisplayResolution resolution);
    void requestCloseOptions();
}