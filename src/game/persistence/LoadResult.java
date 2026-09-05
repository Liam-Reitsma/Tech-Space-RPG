package game.persistence;

import static game.entities.Persistable.*;

public class LoadResult extends DataResult<String>
{
    public LoadResult(SaveErrors e, String[] array)
    {
        error = e;
        result = array;
    }
    
    public String getClassName(int index)
    {
        if(0 > index || index >= result.length) return null;
        String data = result[index];
        int spaceIndex = data.indexOf(CLASS_SEPARATOR);
        
        if(spaceIndex == -1) return null;
        
        return data.substring(0, spaceIndex);
    }
    
    public String[] getClassData(int index)
    {
        if(0 > index || index >= result.length) return null;
        String data = result[index];
        int spaceIndex = data.indexOf(CLASS_SEPARATOR);
        
        if(spaceIndex == -1) return null;
        
        data = data.substring(spaceIndex + String.valueOf(CLASS_SEPARATOR).length());
        
        return data.split(String.valueOf(DATA_SEPARATOR));
    }
}