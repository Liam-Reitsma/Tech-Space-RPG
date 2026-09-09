package game.persistence;

import java.io.File;

public class SaveResult extends DataResult<File>
{
    public SaveResult(PersistenceErrors e, File[] array)
    {
        error = e;
        result = array;
    }
    
    public File getSelectedSaveFile(int index)
    {
        return result[index];
    }
}
