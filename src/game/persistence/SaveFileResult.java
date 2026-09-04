package game.persistence;

import java.io.File;

public class SaveFileResult extends DataResult<File>
{
    public SaveFileResult(SaveErrors e, File[] array)
    {
        error = e;
        result = array;
    }
    
    public File[] getSaveFiles()
    {
        return result;
    }
    
    public File getSelectedSaveFile(int index)
    {
        return result[index];
    }
}
