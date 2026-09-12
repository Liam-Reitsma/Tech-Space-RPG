package game.mainmenu;

import static game.ErrorSeverity.NONE;

import game.ErrorSeverity;
import game.Errors;

public enum MainMenuErrors implements Errors
{
    USER_WENT_BACK("", NONE),
    SUCCESS("", NONE);
    
    private final String message;
    private final ErrorSeverity severity;
    
    MainMenuErrors(String errorMessage, ErrorSeverity errorSeverity)
    {
        message = errorMessage;
        severity = errorSeverity;
    }
    
    @Override
    public String getMessage() { return message; }
    
    @Override
    public ErrorSeverity getSeverity() { return severity; }
}