package game;

import java.util.ArrayList;
import java.util.List;

public class LoadResult
{
    private final Errors error;
    private final List<String> result;
    
    public LoadResult(Errors e)
    {
        error = e;
        result = new ArrayList<>();
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
    
    public Errors getError()
    {
        return error;
    }
    
    public int size()
    {
        return result.size();
    }
}
