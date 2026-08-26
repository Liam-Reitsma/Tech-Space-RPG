package game;

public class SaveFileHandler
{
    private final SaveFile saveF;
    
    public SaveFileHandler(SaveFile saveFile)
    {
        saveF = saveFile;
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
