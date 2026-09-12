package game.entities.player;

import static game.ErrorSeverity.*;

import game.ErrorSeverity;
import game.Errors;

public enum PlayerError implements Errors
{
    MAX_INVENTORY_REACHED("You have reached your maximum inventory. Please remove an item first.", RECOVERABLE),
    ITEM_MISSING("", RECOVERABLE),
    NO_ITEM_GIVEN("", RECOVERABLE),
    NO_WEAPON_GIVEN("", RECOVERABLE),
    NO_ARMOR_AVAILABLE("", RECOVERABLE),
    NO_WEAPON_AVAILABLE("", RECOVERABLE),
    NO_ARMOUR_EQUIPPED("", RECOVERABLE),
    NO_WEAPON_EQUIPPED("", RECOVERABLE),
    USER_WENT_BACK("", NONE),
    SUCCESS("", NONE);

    private final String message;
    private final ErrorSeverity severity;

    PlayerError (String errorMessage,  ErrorSeverity errorSeverity)
    {
        message = errorMessage;
        severity = errorSeverity;
    }

    @Override
    public String getMessage() { return message; }

    @Override
    public ErrorSeverity getSeverity() { return severity; }
}