package game.world;

import game.GameModel;
import game.entities.player.Player;

import java.util.Map;

public class WorldController implements WorldAction
{
    private WorldViewAction worldViewAction;
    
    private final GameModel gameModel;
    private final WorldModel worldModel;
    
    public WorldController(GameModel gModel, WorldViewAction wvAction)
    {
        worldViewAction = wvAction;
        
        gameModel = gModel;
        worldModel = new WorldModel();
    }
    
    @Override
    public Map<Point, RuntimeRoom> requestGenerateWorld()
    {
        return worldModel.generateWorld();
    }
    
    @Override
    public void requestMoveNextRoom(Direction direction)
    {
        Player player = gameModel.getPlayer();
        Map<Point, RuntimeRoom> world = gameModel.getWorld();
        player.movePoint(direction);
        worldViewAction.updateAvailableMovementsPanel(worldModel.getPossibleDirections(world, player.getPoint()));
        worldViewAction.updateWorldMapPanel(worldModel.getSurroundingRoomStates(world, player.getPoint()));
    }
}