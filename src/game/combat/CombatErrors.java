package game.combat;

import static game.ErrorSeverity.*;

import game.ErrorSeverity;
import game.Errors;

public enum CombatErrors implements Errors
{
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