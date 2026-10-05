package game.frame.view;

import game.frame.controller.DisplayTarget;
import game.gui.config.DisplayResolution;

public interface FrameViewAction
{
    void display(DisplayTarget display);
    void closeOverlay();
    void resizeGameFrame(DisplayResolution resolution);
    void closeGameFrame();
}