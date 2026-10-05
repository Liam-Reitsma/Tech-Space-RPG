package game.maingame.controller;

import static game.frame.controller.DisplayTarget.MAIN_GAME;

import game.Updatable;
import game.entities.player.Player;
import game.frame.controller.FrameAction;
import game.maingame.model.GameModel;
import game.maingame.view.GameViewAction;
import game.world.Point;
import game.world.WorldController;
import game.world.room.RuntimeRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameController implements GameAction
{
    private final List<Updatable> updatables;
    
    private final GameModel gameModel;
    
    private final FrameAction frameAction;
    private final WorldController worldController; //TODO: WorldAction
    
    private GameViewAction gameViewAction;
    
    public GameController(GameModel gModel, FrameAction fAction, WorldController wController)
    {
        gameModel = gModel;
        
        frameAction = fAction;
        
        updatables = new ArrayList<>();
        worldController = wController;
        updatables.add(wController);
    }
    
    public void setGameViewAction(GameViewAction gvAction) { gameViewAction = gvAction; }
    
    @Override
    public void requestOpenGameScreen()
    {
        frameAction.display(MAIN_GAME);
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
}