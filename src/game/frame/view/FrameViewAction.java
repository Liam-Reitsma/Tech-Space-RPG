package game.frame.view;

import game.frame.controller.DisplayTarget;
import game.gui.config.DisplayResolution;

public interface FrameViewAction
{
    void closeError();
    void closeGameFrame();
    void closeOverlay();
    void display(DisplayTarget display);
    void resizeGameFrame(DisplayResolution resolution);
}