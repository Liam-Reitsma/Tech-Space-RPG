package game.mainmenu;

import game.Errors;

public enum MainMenuErrors implements Errors
{
    LOADING_LIST_FAILED("Loading Failed! Please try again or create a new save."),
    SUCCESS("");
    
    private final String errorMessage;
    
    MainMenuErrors(String message)
    {
        errorMessage = message;
    }
    
    @Override
    public String toString()
    {
        return errorMessage;
    }
}
