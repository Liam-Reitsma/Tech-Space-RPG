package game.reward;

import static game.ErrorSeverity.NONE;
import static game.ErrorSeverity.RECOVERABLE;

import game.ErrorSeverity;
import game.Errors;

public enum RewardErrors implements Errors
{
    CANNOT_ADD_ITEM("An error occurred while adding your chest item", RECOVERABLE),
    INVENTORY_FULL("Your inventory is too full, please remove something.", RECOVERABLE),
    CLEARING_INVENTORY("", NONE),
    ITEM_DISCARDED("", NONE),
    SUCCESS("", NONE);

    private final String message;
    private final ErrorSeverity severity;

    RewardErrors(String errorMessage, ErrorSeverity errorSeverity)
    {
        message = errorMessage;
        severity = errorSeverity;
    }
    
    @Override
    public String getMessage() { return message; }
    
    @Override
    public ErrorSeverity getSeverity() { return severity; }
}