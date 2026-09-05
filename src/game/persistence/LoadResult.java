package game.persistence;

public class LoadResult extends DataResult<String>
{
    private static final String CLASS_NAME_DELIMITER = ": ";
    
    public LoadResult(SaveErrors e, String[] array)
    {
        error = e;
        result = array;
    }
    
    public String getClassName(int index)
    {
        if(0 > index || index >= result.length) return null;
        String data = result[index];
        int spaceIndex = data.indexOf(CLASS_NAME_DELIMITER);
        
        if(spaceIndex == -1) return null;
        
        return data.substring(0, spaceIndex);
    }
    
    public String[] getClassData(int index)
    {
        if(0 > index || index >= result.length) return null;
        String data = result[index];
        String classFieldDelimiter = ", ";
        int spaceIndex = data.indexOf(CLASS_NAME_DELIMITER);
        
        if(spaceIndex == -1) return null;
        
        data = data.substring(spaceIndex + CLASS_NAME_DELIMITER.length());
        
        return data.split(classFieldDelimiter);
    }
}