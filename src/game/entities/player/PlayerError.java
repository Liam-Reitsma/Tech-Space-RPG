package game.entities.player;

import game.ErrorSeverity;
import game.Errors;

import static game.ErrorSeverity.*;

public enum PlayerError implements Errors
{
    NO_WEAPON_AVAILABLE("", RECOVERABLE),
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