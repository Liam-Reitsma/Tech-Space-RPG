package game.world;

import game.Errors;

public enum WorldErrors implements Errors
{
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