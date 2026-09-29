package game.saves;

import game.persistence.PersistenceService;

public class SavesController implements SavesAction
{
    private final PersistenceService pService;
    private final SavesViewAction sViewAction;
    
    public SavesController(PersistenceService persistenceService, SavesViewAction savesViewAction)
    {
        pService = persistenceService;
        sViewAction = savesViewAction;
    }
    
    @Override
    public String[] requestOpenSaves()
    {
        return pService.getSaveOptionNames();
    }
    
    @Override
    public void requestSelectSave(String saveName)
    {
        pService.loadSavedPlayer(saveName);
        pService.loadSavedWorld();
        
        sViewAction.closeSavesScreen();
        sViewAction.openGameScreen();
    }
    
    @Override
    public void requestCloseSaves()
    {
        sViewAction.closeSavesScreen();
    }
}