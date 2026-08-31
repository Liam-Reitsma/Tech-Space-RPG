package game.saves;

import game.entities.Saveable;

import java.util.List;

public class SaveFileHandler
{
    private final SaveFile saveF;
    
    public SaveFileHandler(SaveFile saveFile)
    {
        saveF = saveFile;
    }
    
    public SaveErrors saveData(List<Saveable> saveables)
    {
        return saveF.write(saveables);
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
