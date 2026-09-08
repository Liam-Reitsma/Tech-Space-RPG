package game.world;

import game.Errors;

public enum WorldErrors implements Errors
{
    GENERATING_ERROR("World Generation Failed! An unknown error occurred while loading your game."),
    SUCCESS("");
    
    private final String message;
    
    WorldErrors(String s)
    {
        message = s;
    }
    
    @Override
    public String toString()
    {
        return message;
    }
}