package game.gui.errors;

import game.gui.common.PanelButton;

import java.awt.event.ActionListener;

public enum DisplayError
{
    DUPLICATE_SAVE_NAME("Sorry, that save name already exists. Would you like to overwrite?", "Cancel", "Confirm");
    
    private final String errorMessage;
    private final String leftButtonText;
    private final String rightButtonText;
    
    DisplayError(String message, String left, String right)
    {
        errorMessage = message;
        leftButtonText = left;
        rightButtonText = right;
    }
    
    public ErrorOverlay createOverlay(ActionListener leftButtonAction, ActionListener rightButtonAction)
    {
        PanelButton leftButton = new PanelButton(leftButtonText, leftButtonAction);
        PanelButton rightButton = new PanelButton(rightButtonText, rightButtonAction);
        return new ErrorOverlay(errorMessage, leftButton, rightButton);
    }
}