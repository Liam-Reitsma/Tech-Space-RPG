package game.persistence;

import game.Result;

public abstract class DataResult<T> extends Result<PersistenceErrors>
{
    protected T[] result;
    
    public int size()
    {
        return result.length;
    }
    
    public T[] getResult() { return result; }
    
    @Override
    public String toString()
    {
        StringBuilder stringToReturn = new StringBuilder();
        for(T line : result) stringToReturn.append(line).append('\n');
        return stringToReturn.toString();
    }
}
