package game.saves.controller;

import game.gui.popup.PopupError;
import game.player.model.Player;
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

    /**
     * Retrieves all saves from the database and refreshes the displayed save list.
     * Clears the list if no saves are found or retrieval fails.
     */
    @Override
    public void requestOpenSavesOverlay()
    {
        Map<String, Player> saveNamePlayerMap = persistenceService.getAllSaves();
        saveNames = new String[0];
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

    /**
     * Attempts to delete the selected save if the index is valid.
     * Displays an error if deletion fails; otherwise updates the saved-game list.
     *
     * @param deletedIndex The index of the save to delete.
     */
    @Override
    public void requestDeleteSave(int deletedIndex)
    {
        if(saveNames != null && deletedIndex >= 0 && deletedIndex < saveNames.length)
        {
            if(!persistenceService.deleteSaveGame(saveNames[deletedIndex]))
            {
                frameAction.requestDisplayError(PopupError.SAVE_DELETION_FAILED,
                        e-> frameAction.requestClosePopup(),
                        e -> frameAction.requestClosePopup());
                return;
            }
            
            savesViewAction.removeSave(deletedIndex);
            
            String[] newSaveList = new String[saveNames.length - 1];
            for(int i = 0; i < saveNames.length; i++)
                if(i != deletedIndex)
                    newSaveList[i++] = saveNames[i];
            
            saveNames = newSaveList;
        }
    }

    /**
     * Attempts to load the selected save if the index is valid.
     * Starts the game on success or displays an error popup if loading fails.
     *
     * @param selectedIndex The index of the save to load.
     */
    @Override
    public void requestSelectSave(int selectedIndex)
    {
        if(saveNames != null && selectedIndex >= 0 && selectedIndex < saveNames.length)
        {
            SavedData data = persistenceService.loadSaveGame(saveNames[selectedIndex]);
            if(data != null)
                gameAction.requestLoadGame(data);
            else
                frameAction.requestDisplayError(PopupError.LOADING_FAILED,
                        e-> frameAction.requestClosePopup(),
                        e -> frameAction.requestClosePopup());
        }
    }
    
    @Override
    public void requestCloseSaves()
    {
        frameAction.requestCloseOverlay();
    }
}