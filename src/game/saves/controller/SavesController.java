package game.saves.controller;

import game.GameAction;
import game.persistence.PersistenceService;
import game.saves.view.SavesViewAction;

public class SavesController implements SavesAction
{
    private final GameAction gameAction;
    private final PersistenceService persistenceService;
    private final SavesViewAction savesViewAction;
    
    public SavesController(GameAction gAction, PersistenceService pService, SavesViewAction sViewAction)
    {
        gameAction = gAction;
        persistenceService = pService;
        savesViewAction = sViewAction;
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
    }
    
    @Override
    public void requestCloseSaves()
    {
        savesViewAction.closeSaveOverlay();
    }
}