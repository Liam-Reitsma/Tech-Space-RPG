package game.mainmenu;

import game.Errors;

public enum MainMenuErrors implements Errors
{
    USER_WENT_BACK(""),
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