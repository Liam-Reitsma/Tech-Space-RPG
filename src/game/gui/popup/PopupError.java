package game.gui.popup;

import game.gui.common.PanelButton;

import java.awt.event.ActionListener;

public enum PopupError
{
    DUPLICATE_SAVE_NAME("That save name already exists. Would you like to overwrite?", "Cancel", "Confirm"),
    SAVE_FAILED("Saving failed. Just letting you know.", "Okay", "Damn That Sucks"),
    LOADING_FAILED("Loading failed. Your load file might be corrupted.", "Noooooo, my character.", "You guys suck"),
    SAVE_DELETION_FAILED("Deleting the selected save failed", "I want him gone...", "Why does he get to stay?"),
    INVENTORY_FULL_EQUIPPING("Your inventory is full. Would you like to discard your equipped item?", "Cancel", "Confirm"),
    INVENTORY_FULL_UNEQUIPPING("Your inventory is full. Would you like to discard your inventory item?", "Cancel", "Confirm"),
    INVENTORY_FULL_ADDING("Your inventory is full. Would you like to discard the item to pickup?", "Cancel", "Confirm"),
    NO_STATS_INCREASED("You have unspent stats points. Please spend them.", "Okay", "Okay");
    
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