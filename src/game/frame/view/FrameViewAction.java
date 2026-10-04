package game.frame.view;

import game.gui.config.DisplayResolution;

public interface FrameViewAction
{
    void openGameScreen();
    void resizeGameFrame(DisplayResolution resolution);
    void closeGameFrame();
}