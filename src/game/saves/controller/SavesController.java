package game.saves.controller;

import static game.frame.controller.DisplayTarget.SAVES;

import game.frame.controller.FrameAction;
import game.maingame.controller.GameAction;
import game.persistence.PersistenceService;

public class SavesController implements SavesAction
{
    private final FrameAction frameAction;
    private final GameAction gameAction;
    
    private final PersistenceService persistenceService;
    
    public SavesController(FrameAction fAction, GameAction gAction, PersistenceService pService)
    {
        frameAction = fAction;
        gameAction = gAction;
        persistenceService = pService;
    }
    
    @Override
    public void requestOpenSavesOverlay()
    {
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