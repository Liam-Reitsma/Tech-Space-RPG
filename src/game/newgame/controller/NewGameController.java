package game.newgame.controller;

import game.GameAction;
import game.entities.player.ClassType;
import game.entities.player.Player;
import game.mainmenu.MainMenuAction;
import game.newgame.view.NewGameViewAction;

public class NewGameController implements NewGameAction
{
    private final GameAction gameAction;
    private final MainMenuAction mainMenuAction;
    private final NewGameViewAction newGameViewAction;
    
    public NewGameController(GameAction gAction, MainMenuAction mmAction, NewGameViewAction ngViewAction)
    {
        gameAction = gAction;
        mainMenuAction = mmAction;
        newGameViewAction = ngViewAction;
    }
    
    @Override
    public void requestOpenNewGameScreen()
    {
        newGameViewAction.openNewGameScreen();
    }
    
    @Override
    public void requestCharacterChange(ClassType classType)
    {
        newGameViewAction.changeCharacterPanel(classType);
    }
    
    @Override
    public void requestConfirmCreation(String name, ClassType classType)
    {
        gameAction.requestNewGame(new Player(name, classType));
    }
    
    @Override
    public void requestCloseNewGameScreen()
    {
        mainMenuAction.requestOpenMainMenuScreen();
        newGameViewAction.closeNewGameScreen();
    }
}