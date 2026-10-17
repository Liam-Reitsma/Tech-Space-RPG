package game.frame.controller;

import game.gui.config.DisplayResolution;
import game.gui.popup.PopupError;
import game.gui.popup.PopupMessage;

import java.awt.event.ActionListener;

public interface FrameAction
{
    void requestCloseOverlay();
    void requestClosePopup();
    void requestDisplay(DisplayTarget display);
    void requestDisplayError(PopupError error, ActionListener onCancel, ActionListener onConfirm);
    void requestDisplayConfirmation(PopupMessage popupMessage, ActionListener onCancel, ActionListener onConfirm);
    void requestResolutionChange(DisplayResolution resolution);
}