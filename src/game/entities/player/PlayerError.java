package game.entities.player;

import game.ErrorSeverity;
import game.Errors;

import static game.ErrorSeverity.NONE;

public enum PlayerError implements Errors
{
    SUCCESS("", NONE),
    GO_BACK("", NONE),;

    private final String message;
    private final ErrorSeverity severity;

    PlayerError (String errorMessage,  ErrorSeverity errorSeverity)
    {
        message = errorMessage;
        severity = errorSeverity;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ErrorSeverity getSeverity() {
        return severity;
    }
}
