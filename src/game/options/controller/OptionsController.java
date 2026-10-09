package game.options.controller;

import static game.frame.controller.DisplayTarget.OPTIONS;

import game.frame.controller.FrameAction;
import game.gui.config.DisplayResolution;

public class OptionsController implements OptionsAction
{
    private final FrameAction frameAction;
    
    public OptionsController(FrameAction fAction)
    {
        frameAction = fAction;
    }
    
    @Override
    public void requestOpenOptionsOverlay()
    {
        frameAction.requestDisplay(OPTIONS);
    }
    
    @Override
    public void requestResolutionChange(DisplayResolution resolution)
    {
        frameAction.requestResolutionChange(resolution);
    }
    
    @Override
    public void requestCloseOptions()
    {
        frameAction.requestCloseOverlay();
    }
}