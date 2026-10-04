package game.frame.view;

import game.gui.config.DisplayResolution;

public interface FrameViewAction
{
    public void resizeGameFrame(DisplayResolution resolution);
    public void closeGameFrame();
}