package game.saves.controller;

import game.entities.player.Player;
import game.frame.controller.FrameAction;
import game.maingame.controller.GameAction;
import game.persistence.PersistenceService;
import game.persistence.SavedData;
import game.saves.view.SavesViewAction;

import java.util.Map;
import java.util.Map.Entry;

public class SavesController implements SavesAction
{
    private String[] saveNames;
    
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
        Map<String, Player> saveNamePlayerMap = persistenceService.getAllSaves();
        String[] displayData = new String[0];
        if(saveNamePlayerMap != null)
        {
            saveNames = new String[saveNamePlayerMap.size()];
            displayData = new String[saveNamePlayerMap.size()];
            int i = 0;
            for(Entry<String, Player> playerEntry : saveNamePlayerMap.entrySet())
            {
                String saveName = playerEntry.getKey();
                Player player = playerEntry.getValue();
                saveNames[i] = saveName;
                displayData[i++] = player.getName() + " | " + player.getLevel() + " | " + player.getClassType().toString();
            }
        }
        savesViewAction.updateSavesList(displayData);
    }
    
    @Override
    public void requestDeleteSave(int deletedIndex)
    {
        if(deletedIndex >= 0 && deletedIndex < saveNames.length)
        {
            persistenceService.deleteSaveGame(saveNames[deletedIndex]);
            savesViewAction.removeSave(deletedIndex);
        }
    }
    
    @Override
    public void requestSelectSave(int selectedIndex)
    {
        if(selectedIndex >= 0 && selectedIndex < saveNames.length)
        {
            SavedData data = persistenceService.loadSaveGame(saveNames[selectedIndex]);
            if(data != null)
                gameAction.requestLoadGame(data);
            //TODO show error
        }
    }
    
    @Override
    public void requestCloseSaves()
    {
        frameAction.requestCloseOverlay();
    }
}