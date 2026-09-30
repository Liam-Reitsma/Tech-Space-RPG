package game.mainmenu;

import game.saves.SavesAction;

public class MainMenuController implements MainMenuAction
{
    private final SavesAction savesAction;
    private final MainMenuViewAction mmViewAction;
    
    public MainMenuController(SavesAction sAction, MainMenuViewAction mmvAction)
    {
        savesAction = sAction;
        mmViewAction = mmvAction;
        
    }
    
    @Override
    public void requestNewGame() { mmViewAction.openNewGameScreen(); }
    
    @Override
    public void requestLoadGame()
    {
        mmViewAction.openLoadGameOverlay(savesAction.requestOpenSaves());
    }
    
    @Override
    public void requestOpenOptions() { mmViewAction.openOptionsOverlay(); }
    
    @Override
    public void requestExitGame() { mmViewAction.closeGameFrame(); }
}