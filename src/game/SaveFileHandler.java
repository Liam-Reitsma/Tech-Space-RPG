package game;

import game.entities.Saveable;

import java.util.List;

public class SaveFileHandler
{
    private final SaveFile saveF;
    
    public SaveFileHandler(SaveFile saveFile)
    {
        saveF = saveFile;
    }
    
    public Errors saveData(List<Saveable> saveables)
    {
        return saveF.write(saveables);
    }
    
    public Errors createNewSave(String name)
    {
        return saveF.createNewSave(name);
    }
    
    public String listAllSaves()
    {
        return saveF.toString();
    }
    
    public Errors selectSave(int num)
    {
        return saveF.selectSave(num);
    }
}
