package game.newgame.controller;

import static game.frame.controller.DisplayTarget.*;

import game.frame.controller.FrameAction;
import game.maingame.controller.GameAction;
import game.entities.player.ClassType;
import game.entities.player.Player;
import game.newgame.view.NewGameViewAction;

public class NewGameController implements NewGameAction
{
    private final FrameAction frameAction;
    private final GameAction gameAction;
    private final NewGameViewAction newGameViewAction;
    
    public NewGameController(FrameAction fAction, GameAction gAction, NewGameViewAction ngViewAction)
    {
        frameAction = fAction;
        gameAction = gAction;
        newGameViewAction = ngViewAction;
    }
    
    @Override
    public void requestOpenNewGameScreen()
    {
        frameAction.display(NEW_GAME);
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
    public void requestMainMenuScreen()
    {
        frameAction.display(MAIN_MENU);
    }
}