package game.frame.view;

import game.gui.popup.PopupError;
import game.gui.popup.PopupMessage;

import java.awt.event.ActionListener;

public interface PopupViewAction
{
    void displayError(PopupError error, ActionListener onCancel, ActionListener onConfirm);
    void displayConfirmation(PopupMessage popupMessage, ActionListener onCancel, ActionListener onConfirm);
}