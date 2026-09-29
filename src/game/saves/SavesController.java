package game.saves;

import game.GameAction;
import game.persistence.PersistenceService;

public class SavesController implements SavesAction
{
    private final GameAction gameAction;
    private final PersistenceService persistenceService;
    private final SavesViewAction savesViewAction;
    
    public SavesController(GameAction gAction, PersistenceService pService, SavesViewAction svAction)
    {
        gameAction = gAction;
        persistenceService = pService;
        savesViewAction = svAction;
    }
    
    @Override
    public String[] requestOpenSaves()
    {
        return persistenceService.getSaveOptionNames();
    }
    
    @Override
    public void requestSelectSave(String saveName)
    {
        gameAction.requestLoadGame(persistenceService.loadSavedPlayer(saveName), persistenceService.loadSavedWorld());
        
        savesViewAction.closeSavesScreen();
        savesViewAction.openGameScreen();
    }
    
    @Override
    public void requestCloseSaves()
    {
        savesViewAction.closeSavesScreen();
    }
}