package game.newgame;

import game.GameAction;
import game.entities.player.ClassType;
import game.entities.player.Player;

public class NewGameController implements NewGameAction
{
    NewGameViewAction newGameViewAction;
    GameAction gameAction;
    
    public NewGameController(NewGameViewAction ngViewAction, GameAction gAction)
    {
         newGameViewAction = ngViewAction;
         gameAction = gAction;
    }
    
    @Override
    public void requestGoBack()
    {
        newGameViewAction.openMainMenuScreen();
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
}