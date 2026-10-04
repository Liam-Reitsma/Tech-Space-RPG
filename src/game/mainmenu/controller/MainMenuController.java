package game.mainmenu.controller;

import game.frame.controller.FrameAction;
import game.mainmenu.view.MainMenuViewAction;
import game.newgame.controller.NewGameAction;
import game.options.controller.OptionsAction;
import game.saves.controller.SavesAction;

public class MainMenuController implements MainMenuAction
{
    private final FrameAction frameAction;
    private final NewGameAction newGameAction;
    private final OptionsAction optionsAction;
    private final SavesAction savesAction;
    private final MainMenuViewAction mmViewAction;
    
    public MainMenuController(FrameAction fAction, NewGameAction ngAction, OptionsAction oAction, SavesAction sAction, MainMenuViewAction mmvAction)
    {
        frameAction = fAction;
        newGameAction = ngAction;
        optionsAction = oAction;
        savesAction = sAction;
        mmViewAction = mmvAction;
    }
    
    @Override
    public void requestOpenMainMenuScreen()
    {
        mmViewAction.openMainMenuScreen();
    }
    
    @Override
    public void requestOpenNewGameScreen()
    {
        newGameAction.requestOpenNewGameScreen();
        mmViewAction.closeMainMenuScreen();
    }
    
    @Override
    public void requestOpenSavesOverlay()
    {
        savesAction.requestOpenSavesOverlay();
    }
    
    @Override
    public void requestOpenOptionsOverlay()
    {
        optionsAction.requestOpenOptionsOverlay();
    }
    
    @Override
    public void requestCloseMainMenuScreen()
    {
        mmViewAction.closeMainMenuScreen();
    }
    
    @Override
    public void requestExitGame()
    {
        frameAction.requestCloseGame();
    }
}