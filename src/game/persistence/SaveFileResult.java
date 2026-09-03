package game.persistence;

import java.util.ArrayList;
import java.util.List;

public class SaveFileResult extends DataResult
{
    public SaveFileResult(SaveErrors e)
    {
        error = e;
        result = new ArrayList<>();
    }
    
    public List<String> getSaveFiles()
    {
        return result;
    }
}
