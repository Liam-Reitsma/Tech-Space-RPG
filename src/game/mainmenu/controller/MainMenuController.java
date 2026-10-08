package game.mainmenu.controller;

import static game.frame.controller.DisplayTarget.*;

import game.frame.controller.FrameAction;
import game.newgame.controller.NewGameAction;
import game.options.controller.OptionsAction;
import game.saves.controller.SavesAction;

public class MainMenuController implements MainMenuAction
{
    private final FrameAction frameAction;
    private final NewGameAction newGameAction;
    private final OptionsAction optionsAction;
    private final SavesAction savesAction;
    
    public MainMenuController(FrameAction fAction, NewGameAction ngAction, OptionsAction oAction, SavesAction sAction)
    {
        frameAction = fAction;
        newGameAction = ngAction;
        optionsAction = oAction;
        savesAction = sAction;
    }
    
    @Override
    public void requestOpenMainMenu()
    {
        frameAction.display(MAIN_MENU);
    }
    
    @Override
    public void requestOpenDisplay(MainMenuOption mainMenuOption)
    {
        switch(mainMenuOption)
        {
            case NEW_GAME -> newGameAction.requestOpenNewGameScreen();
            case LOAD_GAME -> savesAction.requestOpenSavesOverlay();
            case OPTIONS -> optionsAction.requestOpenOptionsOverlay();
            case EXIT_GAME -> frameAction.requestCloseGame();
        }
    }
    
    @Override
    public void requestExitGame()
    {
        frameAction.requestCloseGame();
    }
}