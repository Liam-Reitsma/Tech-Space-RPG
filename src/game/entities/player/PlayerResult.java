package game.entities.player;

import game.Result;

public class PlayerResult<T> extends Result<PlayerErrors>
{
    private final T[] resultArray;
    
    public PlayerResult(PlayerErrors e, T[] array)
    {
        error = e;
        resultArray = array;
    }
    
    public T[] getResultArray() { return resultArray; }
}