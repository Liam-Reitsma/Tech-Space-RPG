package game.reward;

import static game.ErrorSeverity.NONE;

import game.ErrorSeverity;
import game.Errors;

public enum RewardErrors implements Errors
{
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