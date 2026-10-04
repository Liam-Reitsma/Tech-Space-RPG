package game.frame.view;

import game.gui.config.DisplayResolution;
import game.gui.config.Displayable;

public interface FrameViewAction
{
    void display(Displayable displayable);
    void resizeGameFrame(DisplayResolution resolution);
    void closeGameFrame();
}