package game.entities.player;

import game.Result;

public class PlayerResult<T> extends Result<PlayerError>
{
    private final T[] resultArray;
    
    public PlayerResult(PlayerError e, T[] array)
    {
        error = e;
        resultArray = array;
    }
    
    public T[] getResultArray() { return resultArray; }
}