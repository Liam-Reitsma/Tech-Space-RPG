package game.options;

import game.gui.DisplayResolution;

public interface OptionsViewAction
{
    void resizeGameFrame(DisplayResolution resolution);
    void closeOptionsScreen();
}