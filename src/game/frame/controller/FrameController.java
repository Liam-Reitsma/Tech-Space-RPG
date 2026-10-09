package game.frame.controller;

import game.frame.view.FrameViewAction;
import game.gui.config.DisplayResolution;
import game.gui.errors.DisplayError;

import javax.swing.*;
import java.awt.event.ActionListener;

public class FrameController implements FrameAction
{
    private FrameViewAction frameViewAction;
    
    public FrameController()
    {
    
    }
    
    public void setFrameViewAction(FrameViewAction fvAction) { frameViewAction = fvAction; }
    
    @Override
    public void requestResolutionChange(DisplayResolution resolution)
    {
        frameViewAction.resizeGameFrame(resolution);
    }
    
    @Override
    public void requestDisplayError(DisplayError error, ActionListener leftButton, ActionListener rightButton)
    {
        frameViewAction.displayError(error, leftButton, rightButton);
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
    public void requestCloseError()
    {
        frameViewAction.closeError();
    }
    
    @Override
    public void requestCloseGame()
    {
        frameViewAction.closeGameFrame();
    }
}