package game.persistence;

import game.entities.Persistable;

import java.util.List;

public class SaveFileHandler
{
    private final SaveFile saveF;
    
    public SaveFileHandler(SaveFile saveFile)
    {
        saveF = saveFile;
    }
    
    public SaveErrors saveData(List<Persistable> persistables)
    {
        return saveF.write(persistables);
    }
    
    public LoadResult loadData()
    {
        return saveF.read();
    }
    
    public SaveErrors createNewSave(String name)
    {
        return saveF.createNewSave(name);
    }
    
    public String listAllSaves()
    {
        return saveF.toString();
    }
    
    public SaveErrors selectSave(int num)
    {
        return saveF.selectSave(num);
    }
}
