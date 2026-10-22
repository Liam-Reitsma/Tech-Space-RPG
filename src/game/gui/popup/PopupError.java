package game.gui.popup;

import game.gui.common.PanelButton;

import java.awt.event.ActionListener;

public enum PopupError
{
    DUPLICATE_SAVE_NAME("That save name already exists. Would you like to overwrite?", "Cancel", "Confirm"),
    INVENTORY_FULL_EQUIPPING("Your inventory is full. Would you like to discard your equipped item?", "Cancel", "Confirm"),
    INVENTORY_FULL_UNEQUIPPING("Your inventory is full. Would you like to discard your inventory item?", "Cancel", "Confirm");
    
    private final String errorMessage;
    private final String leftButtonText;
    private final String rightButtonText;
    
    PopupError(String message, String left, String right)
    {
        errorMessage = message;
        leftButtonText = left;
        rightButtonText = right;
    }
    
    public PopupOverlay createOverlay(ActionListener leftButtonAction, ActionListener rightButtonAction)
    {
        PanelButton leftButton = new PanelButton(leftButtonText, leftButtonAction);
        PanelButton rightButton = new PanelButton(rightButtonText, rightButtonAction);
        return new PopupOverlay(toString(), errorMessage, leftButton, rightButton);
    }
    
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        String[] words = name().split("_");
        for(String word : words)
            result.append(word.charAt(0)).append(word.substring(1).toLowerCase()).append(" ");
        
        result.deleteCharAt(result.length() - 1);
        
        return result.toString();
    }
}