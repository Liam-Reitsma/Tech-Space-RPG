package game.saves.controller;

import static game.frame.controller.DisplayTarget.SAVES;

import game.entities.player.Player;
import game.frame.controller.FrameAction;
import game.maingame.controller.GameAction;
import game.persistence.PersistenceService;
import game.persistence.SavedData;
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
        Player[] players = persistenceService.getAllSaves();
        String[] saveData = new String[0];
        if(players != null)
        {
            saveData = new String[players.length];
            for(int i = 0 ; i < saveData.length; i++)
                saveData[i] = players[i].getName() + " | " + players[i].getLevel() + " | " + players[i].getClassType().toString();
        }
        savesViewAction.updateSavesList(saveData);
        frameAction.display(SAVES);
    }
    
    @Override
    public void requestDeleteSave(String saveName)
    {
        persistenceService.deleteSaveGame(saveName.split(" \\| ")[0]);
        savesViewAction.removeSave(saveName);
    }
    
    @Override
    public void requestSelectSave(String saveName)
    {
        SavedData data = persistenceService.loadSaveGame(saveName.split(" \\| ")[0]);
        if(data != null)
            gameAction.requestLoadGame(data);
        //TODO show error
    }
    
    @Override
    public void requestCloseSaves()
    {
        frameAction.closeOverlay();
    }
}