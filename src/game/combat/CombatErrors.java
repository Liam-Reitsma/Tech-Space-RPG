package game.combat;

import static game.ErrorSeverity.*;

import game.ErrorSeverity;
import game.Errors;

public enum CombatErrors implements Errors
{
    /** Error for when there are no potions in the inventory. Used to prompt the Player again.*/
    NO_POTION_TO_USE("You have no potions to use!", RECOVERABLE),
    /** Not a real error. Used when Player chooses to go back.*/
    USER_WENT_BACK("", NONE),
    /** Not a real error. Used to show that the task was successful.*/
    SUCCESS("", NONE);

    private final String message;
    private final ErrorSeverity severity;

    CombatErrors(String errorMessage, ErrorSeverity errorSeverity)
    {
        message = errorMessage;
        severity = errorSeverity;
    }

    @Override
    public String getMessage() { return message; }
    
    @Override
    public ErrorSeverity getSeverity() { return severity; }
}