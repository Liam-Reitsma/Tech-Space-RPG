package game.persistence;

import java.util.ArrayList;
import java.util.List;

public class LoadResult extends DataResult
{
    public LoadResult()
    {
        error = SaveErrors.SUCCESS;
        result = new ArrayList<>();
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
}
