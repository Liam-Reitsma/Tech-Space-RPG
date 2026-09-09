package game.persistence;

import game.entities.Persistable;

import java.util.List;

public class PersistenceController
{
    private final SaveFile saveF;
    
    public PersistenceController(SaveFile saveFile) { saveF = saveFile; }
    
    public PersistenceErrors saveData(List<Persistable> persistables) { return saveF.write(persistables); }
    
    public LoadResult loadData() { return saveF.read(); }
    
    public boolean canCreateNewSave(String name) { return saveF.canCreateNewSave(name); }
    
    public PersistenceErrors createNewSave(String name) { return saveF.createNewSave(name); }
    
    public String listAllSaves() { return saveF.toString(); }
    
    public PersistenceErrors selectSave(int num) { return saveF.selectSave(num); }
}