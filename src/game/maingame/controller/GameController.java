package game.maingame.controller;

import game.Updatable;
import game.entities.player.Player;
import game.maingame.model.GameModel;
import game.maingame.view.GameViewAction;
import game.mainmenu.controller.MainMenuAction;
import game.newgame.controller.NewGameAction;
import game.world.Point;
import game.world.WorldController;
import game.world.room.RuntimeRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameController implements GameAction
{
    private final List<Updatable> updatables;
    
    private final MainMenuAction mainMenuAction;
    private final NewGameAction newGameAction;
    
    private final WorldController worldController;
    
    private final GameModel gameModel;
    
    private final GameViewAction gameViewAction;
    
    
    public GameController(MainMenuAction mmAction, NewGameAction ngAction, WorldController wController, GameModel gModel, GameViewAction gvAction)
    {
        mainMenuAction = mmAction;
        newGameAction = ngAction;
        
        updatables = new ArrayList<>();
        worldController = wController;
        updatables.add(wController);
        
        gameModel = gModel;
        gameViewAction = gvAction;
    }
    
    @Override
    public void requestOpenGameScreen()
    {
        gameViewAction.openMainGameScreen();
    }
    
    @Override
    public void requestNewGame(Player newPlayer)
    {
        startGame(newPlayer, worldController.requestGenerateWorld());
    }
    
    @Override
    public void requestLoadGame(Player loadedPlayer, Map<Point, RuntimeRoom> loadedWorld)
    {
        startGame(loadedPlayer, loadedWorld);
    }
    
    private void startGame(Player player, Map<Point, RuntimeRoom> world)
    {
        gameModel.setGame(player, world);
        
        requestOpenGameScreen();
        mainMenuAction.requestCloseMainMenuScreen();
        newGameAction.requestCloseNewGameScreen();
        
        gameViewAction.updateMainGameScreen();
        
        requestGameUpdate();
    }
    
    @Override
    public void requestGameUpdate()
    {
        for(Updatable controller : updatables)
        {
            controller.requestUpdate();
        }
    }
    
    @Override
    public void requestCloseGameScreen()
    {
        gameViewAction.closeMainGameScreen();
    }
}