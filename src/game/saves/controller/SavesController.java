package game.saves.controller;

import static game.frame.controller.DisplayTarget.SAVES;

import game.frame.controller.FrameAction;
import game.maingame.controller.GameAction;
import game.persistence.PersistenceService;
import game.saves.view.SavesViewAction;

public class SavesController implements SavesAction
{
    private final PersistenceService persistenceService;
    
    private final FrameAction frameAction;
    private final GameAction gameAction;
    
    private SavesViewAction savesViewAction;
    
    public SavesController(PersistenceService pService, FrameAction fAction, GameAction gAction)
    {
        persistenceService = pService;
        
        frameAction = fAction;
        gameAction = gAction;
    }
    
    public void setSavesViewAction(SavesViewAction sVAction) { savesViewAction = sVAction; }
    
    @Override
    public void requestOpenSavesOverlay()
    {
        savesViewAction.updateSavesList(persistenceService.getSaveOptionNames());
        frameAction.display(SAVES);
    }
    
    @Override
    public void requestSelectSave(String saveName)
    {
        gameAction.requestLoadGame(persistenceService.loadSavedPlayer(saveName), persistenceService.loadSavedWorld());
    }
    
    @Override
    public void requestCloseSaves()
    {
        frameAction.closeOverlay();
    }
}