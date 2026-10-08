package game.world.controller;

import game.maingame.model.GameModel;
import game.Updatable;
import game.entities.player.Player;
import game.world.model.Direction;
import game.world.model.Point;
import game.world.model.WorldModel;
import game.world.view.WorldViewAction;
import game.world.model.room.RuntimeRoom;

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
        gameModel.getWorld().get(gameModel.getPlayer().getPoint()).isCompleted(); //TODO Move to Combat Controller
        gameModel.getPlayer().movePoint(direction);
        gameModel.getWorld().get(gameModel.getPlayer().getPoint()).setCurrent();
        requestUpdate(); //TODO Remove when wired in update in GameController
    }
    
    @Override
    public void requestUpdate()
    {
        Player player = gameModel.getPlayer();
        Map<Point, RuntimeRoom> world = gameModel.getWorld();
        worldViewAction.updateAvailableMovementsPanel(worldModel.getPossibleDirections(world, player.getPoint()));
        RuntimeRoom[][] rooms = worldModel.updateWorldInfo(world, player.getPoint());
        worldViewAction.updateWorldMapPanel(rooms);
    }
}