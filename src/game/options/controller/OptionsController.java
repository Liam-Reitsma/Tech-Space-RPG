package game.options.controller;

import game.gui.config.DisplayResolution;
import game.frame.view.FrameViewAction;
import game.options.view.OptionsViewAction;

public class OptionsController implements OptionsAction
{
    private final OptionsViewAction optionsViewAction;
    private final FrameViewAction frameViewAction;
    
    public OptionsController(FrameViewAction fViewAction, OptionsViewAction oViewAction)
    {
        optionsViewAction = oViewAction;
        frameViewAction = fViewAction;
    }
    
    @Override
    public void requestOpenOptions()
    {
        optionsViewAction.openOptionsOverlay();
    }
    
    @Override
    public void requestResolutionChange(DisplayResolution resolution)
    {
        frameViewAction.resizeGameFrame(resolution);
    }
    
    @Override
    public void requestCloseOptions()
    {
        optionsViewAction.closeOptionsOverlay();
    }
}