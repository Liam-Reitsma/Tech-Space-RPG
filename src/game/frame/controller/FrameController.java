package game.frame.controller;

import game.frame.view.FrameViewAction;
import game.gui.config.DisplayResolution;
import game.gui.config.Displayable;

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
    public void display(Displayable toDisplay)
    {
        frameViewAction.display(toDisplay);
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