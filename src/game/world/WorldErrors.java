package game.world;

import static game.ErrorSeverity.*;

import game.Errors;
import game.ErrorSeverity;

public enum WorldErrors implements Errors
{
    IMPOSSIBLE_ROOM_ERROR("Room Selection Failed! An unknown error occurred while selecting the room.", SEVERE),
    LOADING_WORLD_ERROR("World Loading Failed! An unknown error occurred while loading the world.", SEVERE),
    GENERATING_ERROR("World Generation Failed! An unknown error occurred while generating the world.", SEVERE),
    USER_WENT_BACK("", NONE),
    SUCCESS("", NONE);
    
    private final String message;
    private final ErrorSeverity severity;
    
    WorldErrors(String errorMessage, ErrorSeverity errorSeverity)
    {
        message = errorMessage;
        severity = errorSeverity;
    }
    
    @Override
    public String getMessage()
    {
        return message;
    }
    
    @Override
    public ErrorSeverity getSeverity() { return severity; }
}