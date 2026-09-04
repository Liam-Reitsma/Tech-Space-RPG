package game.persistence;

public class LoadResult extends DataResult<String>
{
    public LoadResult(SaveErrors e, String[] array)
    {
        error = SaveErrors.SUCCESS;
        result = array;
    }
    
    public String getClassName(int index)
    {
        if(0 > index || index >= result.length) return null;
        String data = result[index];
        int spaceIndex = data.indexOf(": ");
        
        if(spaceIndex == -1) return null;

        return data.substring(0, spaceIndex);
    }
    
    public String[] getClassData(int index)
    {
        if(0 > index || index >= result.length) return null;
        String data = result[index];
        int spaceIndex = data.indexOf(": ");
        
        if(spaceIndex == -1) return null;
        
        data = data.substring(spaceIndex + 2);
        
        return data.split(", ");
    }
}
