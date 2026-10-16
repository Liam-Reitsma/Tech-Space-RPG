package game.frame.controller;

import game.frame.view.FrameViewAction;
import game.gui.config.DisplayResolution;
import game.gui.errors.DisplayError;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class FrameController implements FrameAction
{
    private FrameViewAction frameViewAction;
    
    public FrameController()
    {
    
    }
    
    public void setFrameViewAction(FrameViewAction fvAction) { frameViewAction = fvAction; }
    
    @Override
    public void requestCloseOverlay()
    {
        frameViewAction.closeOverlay();
    }
    
    @Override
    public void requestCloseError()
    {
        frameViewAction.closeError();
    }
    
    @Override
    public void requestDisplay(DisplayTarget display)
    {
        if(display == DisplayTarget.EXIT)
            frameViewAction.closeGameFrame();
        else
            frameViewAction.display(display);
    }
    
    @Override
    public void requestDisplayDrag(MouseEvent e, JComponent drag)
    {
        frameViewAction.displayOnOverlay(e , drag);
    }
    
    @Override
    public void requestDisplayError(DisplayError error, ActionListener leftButton, ActionListener rightButton)
    {
        frameViewAction.displayError(error, leftButton, rightButton);
    }
    
    @Override
    public void requestRemoveDrag(JComponent drag)
    {
        frameViewAction.removeFromOverlay(drag);
    }
    
    @Override
    public void requestResolutionChange(DisplayResolution resolution)
    {
        frameViewAction.resizeGameFrame(resolution);
    }
}