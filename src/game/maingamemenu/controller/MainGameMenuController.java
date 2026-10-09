package game.maingamemenu.controller;

import game.frame.controller.FrameAction;
import game.maingame.model.GameModel;
import game.mainmenu.controller.MainMenuAction;
import game.options.controller.OptionsAction;
import game.saves.controller.SavesAction;

public class MainGameMenuController implements MainGameMenuAction
{
    private final GameModel gameModel;
    
    private final FrameAction frameAction;
    private final MainMenuAction mainMenuAction;
    private final OptionsAction optionsAction;
    private final SavesAction savesAction;
    
    public MainGameMenuController(GameModel gModel, FrameAction fAction, MainMenuAction mmAction, OptionsAction oAction, SavesAction sAction)
    {
        gameModel = gModel;
        frameAction = fAction;
        mainMenuAction = mmAction;
        optionsAction = oAction;
        savesAction = sAction;
    }
    
    @Override
    public void requestSaveGame()
    {
        gameModel.fullSave();
    }
    
    @Override
    public void requestOpenDisplay(MainGameMenuOption option)
    {
        switch(option)
        {
            case LOAD_GAME -> savesAction.requestOpenSavesOverlay();
            case OPTIONS -> optionsAction.requestOpenOptionsOverlay();
            case MAIN_MENU -> mainMenuAction.requestOpenMainMenu();
            case EXIT_GAME -> frameAction.requestCloseGame();
        }
    }
}