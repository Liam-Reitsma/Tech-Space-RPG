package game.world;

import game.Errors;

public enum WorldErrors implements Errors
{
    IMPOSSIBLE_ROOM_ERROR("Room Selection Failed! An unknown error occurred while selecting the room."),
    LOADING_WORLD_ERROR("World Loading Failed! An unknown error occurred while loading the world."),
    GENERATING_ERROR("World Generation Failed! An unknown error occurred while generating the world."),
    USER_WENT_BACK(""),
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