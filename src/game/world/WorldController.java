package game.world;

import game.maingame.model.GameModel;
import game.Updatable;
import game.entities.player.Player;
import game.world.room.RuntimeRoom;

import java.util.Map;

public class WorldController implements WorldAction, Updatable
{
    private WorldViewAction worldViewAction;
    
    private final GameModel gameModel;
    private final WorldModel worldModel;
    
    public WorldController(GameModel gModel)
    {
        gameModel = gModel;
        worldModel = new WorldModel();
    }
    
    public void setWorldViewAction(WorldViewAction wvAction) { worldViewAction = wvAction; }
    
    @Override
    public Map<Point, RuntimeRoom> requestGenerateWorld()
    {
        return worldModel.generateWorld();
    }
    
    @Override
    public void requestMoveNextRoom(Direction direction)
    {
        gameModel.getPlayer().movePoint(direction);
        requestUpdate();
    }
    
    @Override
    public void requestUpdate()
    {
        Player player = gameModel.getPlayer();
        Map<Point, RuntimeRoom> world = gameModel.getWorld();
        worldViewAction.updateAvailableMovementsPanel(worldModel.getPossibleDirections(world, player.getPoint()));
        worldViewAction.updateWorldMapPanel(worldModel.getSurroundingRoomStates(world, player.getPoint()));
    }
}