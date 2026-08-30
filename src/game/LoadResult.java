package game;

import java.util.ArrayList;
import java.util.List;

public class LoadResult
{
    private Errors error;
    private final List<String> result;
    
    public LoadResult()
    {
        error = Errors.SUCCESS;
        result = new ArrayList<>();
    }
    
    public void setError(Errors e)
    {
        error = e;
    }
    
    public void addData(String string)
    {
        result.add(string);
    }
    
    public String getData(int index)
    {
        if(0 >= index || index >= result.size()) return null;
        return result.get(index);
    }
    
    public String getPlayerData()
    {
        return result.getFirst();
    }
    
    public Errors getError()
    {
        return error;
    }
    
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
