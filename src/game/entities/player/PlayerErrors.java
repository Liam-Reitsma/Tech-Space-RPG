package game.entities.player;

import static game.ErrorSeverity.*;

import game.ErrorSeverity;
import game.Errors;

public enum PlayerErrors implements Errors
{
    MAX_INVENTORY_REACHED("You have reached your maximum inventory. Please remove an item first.", RECOVERABLE),
    ITEM_MISSING("You cannot do that. The item selected is missing.", RECOVERABLE),
    NO_POTION_SELECTED("You have not selected a potion.", RECOVERABLE),
    NO_POTION_AVAILABLE("You have no potion to use.", RECOVERABLE),
    NO_ARMOUR_AVAILABLE("You have no armour to equip.", RECOVERABLE),
    NO_WEAPON_AVAILABLE("You have no weapon to equip.", RECOVERABLE),
    NO_SLOT_ARMOUR_EQUIPPED("You have no armour of that specific slot equipped",  RECOVERABLE),
    NO_ARMOUR_EQUIPPED("You have no armour equipped.", RECOVERABLE),
    NO_WEAPON_EQUIPPED("You have no weapon equipped", RECOVERABLE),
    NO_ITEM_GIVEN("", RECOVERABLE),
    NO_WEAPON_GIVEN("", RECOVERABLE),
    USER_WENT_BACK("", NONE),
    SUCCESS("", NONE);

    private final String message;
    private final ErrorSeverity severity;

    PlayerErrors(String errorMessage, ErrorSeverity errorSeverity)
    {
        message = errorMessage;
        severity = errorSeverity;
    }

    @Override
    public String getMessage() { return message; }

    @Override
    public ErrorSeverity getSeverity() { return severity; }
}