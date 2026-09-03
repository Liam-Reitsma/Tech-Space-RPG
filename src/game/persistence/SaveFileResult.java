package game.persistence;

import java.util.ArrayList;

public class SaveFileResult extends DataResult
{
    public SaveFileResult()
    {
        error = SaveErrors.SUCCESS;
        result = new ArrayList<>();
    }
    
    
}
