package game.persistence;

import java.util.List;

public abstract class DataResult
{
    protected SaveErrors error;
    protected List<String> result;
    
    void setError(SaveErrors e) { error = e; }
    
    void addData(String string)
    {
        result.add(string);
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
        for(String line : result) stringToReturn.append(line).append('\n');
        return stringToReturn.toString();
    }
}
