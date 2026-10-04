package game.options.controller;

import game.frame.controller.FrameAction;
import game.gui.config.DisplayResolution;
import game.options.view.OptionsViewAction;

public class OptionsController implements OptionsAction
{
    private final OptionsViewAction optionsViewAction;
    private final FrameAction frameAction;
    
    public OptionsController(FrameAction fAction, OptionsViewAction oViewAction)
    {
        optionsViewAction = oViewAction;
        frameAction = fAction;
    }
    
    @Override
    public void requestOpenOptionsOverlay()
    {
        optionsViewAction.openOptionsOverlay();
    }
    
    @Override
    public void requestResolutionChange(DisplayResolution resolution)
    {
        frameAction.requestResolutionChange(resolution);
    }
    
    @Override
    public void requestCloseOptions()
    {
        optionsViewAction.closeOptionsOverlay();
    }
}