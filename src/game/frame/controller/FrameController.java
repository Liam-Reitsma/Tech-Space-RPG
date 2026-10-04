package game.frame.controller;

import game.frame.view.FrameViewAction;
import game.gui.config.DisplayResolution;

public class FrameController implements FrameAction
{
    private final FrameViewAction frameViewAction;
    
    public FrameController(FrameViewAction fViewAction)
    {
        frameViewAction = fViewAction;
    }
    
    @Override
    public void requestResolutionChange(DisplayResolution resolution)
    {
        frameViewAction.resizeGameFrame(resolution);
    }
    
    @Override
    public void display(DisplayTarget display)
    {
        frameViewAction.display(display);
    }
    
    @Override
    public void closeOverlay()
    {
        frameViewAction.closeOverlay();
    }
    
    @Override
    public void requestCloseGame()
    {
        frameViewAction.closeGameFrame();
    }
}