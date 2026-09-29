package game.options;

import game.gui.DisplayResolution;

public class OptionsController implements OptionsAction
{
    private final OptionsViewAction oViewAction;
    
    public OptionsController(OptionsViewAction oVAction)
    {
        oViewAction = oVAction;
    }
    
    @Override
    public void requestResolutionChange(DisplayResolution resolution)
    {
        oViewAction.resizeGameFrame(resolution);
    }
    
    @Override
    public void requestCloseOptions()
    {
        oViewAction.closeOptionsScreen();
    }
}