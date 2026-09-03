package game.persistence;

import java.util.List;

public abstract class DataResult<T>
{
    protected SaveErrors error;
    protected List<T> result;
    
    void setError(SaveErrors e) { error = e; }
    
    void addData(T data)
    {
        result.add(data);
    }
    
    public SaveErrors getError() { return error; }
    
    public int size()
    {
        return result.size();
    }
    
    @Override
    public String toString()
    {
        StringBuilder stringToReturn = new StringBuilder();
        for(T line : result) stringToReturn.append(line).append('\n');
        return stringToReturn.toString();
    }
}
