package game;

import game.entities.player.Player;
import game.world.Point;
import game.world.WorldController;
import game.world.room.RuntimeRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameController implements GameAction
{
    private final List<Updatable> updatables;
    
    private final WorldController worldController;
    
    private final GameModel gameModel;
    
    private final GameViewAction gameViewAction;
    
    
    public GameController(WorldController wController,GameModel gModel, GameViewAction gvAction)
    {
        updatables = new ArrayList<>();
        worldController = wController;
        updatables.add(wController);
        
        gameModel = gModel;
        
        gameViewAction = gvAction;
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
        gameViewAction.openMainGameScreen();
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