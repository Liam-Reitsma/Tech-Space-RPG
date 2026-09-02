package game.persistence;

import java.util.ArrayList;
import java.util.List;

public class LoadResult
{
    private SaveErrors error;
    private final List<String> result;
    
    public LoadResult()
    {
        error = SaveErrors.SUCCESS;
        result = new ArrayList<>();
    }
    
    public void setError(SaveErrors e)
    {
        error = e;
    }
    
    public void addData(String string)
    {
        result.add(string);
    }
    
    public String getClassName(int index)
    {
        if(0 > index || index >= result.size()) return null;
        String data = result.get(index);
        int spaceIndex = data.indexOf(": ");
        
        if(spaceIndex == -1) return null;

        return data.substring(0, spaceIndex);
    }
    
    public String[] getClassData(int index)
    {
        if(0 > index || index >= result.size()) return null;
        String data = result.get(index);
        int spaceIndex = data.indexOf(": ");
        
        if(spaceIndex == -1) return null;
        
        data = data.substring(spaceIndex + 2);
        
        return data.split(", ");
    }
    
    public SaveErrors getError()
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
