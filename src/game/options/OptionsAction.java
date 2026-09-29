package game.options;

import game.gui.DisplayResolution;

public interface OptionsAction
{
    void requestResolutionChange(DisplayResolution resolution);
    void requestCloseOptions();
}