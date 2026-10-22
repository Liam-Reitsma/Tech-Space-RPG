package game.frame.controller;

import game.frame.view.FrameViewAction;
import game.frame.view.PopupViewAction;
import game.gui.config.DisplayResolution;
import game.gui.popup.PopupError;
import game.gui.popup.PopupMessage;

import java.awt.event.ActionListener;

public class FrameController implements FrameAction
{
    private FrameViewAction frameViewAction;
    private PopupViewAction popupViewAction;
    
    public FrameController()
    {
    
    }
    
    public <ViewAction extends FrameViewAction & PopupViewAction> void setViewActions(ViewAction viewAction)
    {
        frameViewAction = viewAction;
        popupViewAction = viewAction;
    }
    
    @Override
    public void requestCloseOverlay()
    {
        frameViewAction.closeOverlay();
    }
    
    @Override
    public void requestClosePopup()
    {
        popupViewAction.closePopup();
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
    public void requestDisplayError(PopupError error, ActionListener onCancel, ActionListener onConfirm)
    {
        popupViewAction.displayPopupError(error, onCancel, onConfirm);
    }
    
    @Override
    public void requestDisplayConfirmation(PopupMessage popupMessage, ActionListener onCancel, ActionListener onConfirm)
    {
        popupViewAction.displayPopupConfirmation(popupMessage, onCancel, onConfirm);
    }
    
    @Override
    public void requestResolutionChange(DisplayResolution resolution)
    {
        frameViewAction.resizeGameFrame(resolution);
    }
}