package game.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SaveFileResult extends DataResult<File>
{
    public SaveFileResult(SaveErrors e)
    {
        error = e;
        result = new ArrayList<>();
    }
    
    public List<File> getSaveFiles()
    {
        return result;
    }
    
    public File getSelectedSaveFile(int index)
    {
        return result.get(index);
    }
}
