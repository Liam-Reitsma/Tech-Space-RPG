package game.persistence;

public abstract class DataResult<T>
{
    protected SaveErrors error;
    protected T[] result;
    
    public SaveErrors getError() { return error; }
    
    public int size()
    {
        return result.length;
    }
    
    @Override
    public String toString()
    {
        StringBuilder stringToReturn = new StringBuilder();
        for(T line : result) stringToReturn.append(line).append('\n');
        return stringToReturn.toString();
    }
}
