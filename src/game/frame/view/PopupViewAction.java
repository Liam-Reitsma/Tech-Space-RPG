package game.frame.view;

import game.gui.popup.PopupError;
import game.gui.popup.PopupMessage;

import java.awt.event.ActionListener;

public interface PopupViewAction
{
    void closePopup();
    void displayPopupError(PopupError error, ActionListener onCancel, ActionListener onConfirm);
    void displayPopupConfirmation(PopupMessage popupMessage, ActionListener onCancel, ActionListener onConfirm);
}