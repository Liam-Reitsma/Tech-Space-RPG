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

    private RoomListener roomListener;
    
    private final GameModel gameModel;
    private final WorldModel worldModel;

    private boolean movementLocked;
    
    public WorldController(GameModel gModel)
    {
        gameModel = gModel;
        worldModel = new WorldModel();
    }
    
    public void setWorldViewAction(WorldViewAction wvAction) { worldViewAction = wvAction; }

    public void setRoomListener(RoomListener rListener) { roomListener = rListener; }

    @Override
    public void requestSetMovementLocked(boolean locked) { movementLocked = locked; }

    @Override
    public Map<Point, RuntimeRoom> requestGenerateWorld()
    {
        return worldModel.generateWorld();
    }
    
    @Override
    public void requestMoveNextRoom(Direction direction)
    {
        if(movementLocked) return;
        gameModel.getWorld().get(gameModel.getPlayer().getPoint()).clearCurrent();
        gameModel.getPlayer().movePoint(direction);
        gameModel.getWorld().get(gameModel.getPlayer().getPoint()).setCurrent();
        if(roomListener != null) roomListener.onRoomEntered(gameModel.getWorld().get(gameModel.getPlayer().getPoint()));
        requestUpdate();
    }
    
    @Override
    public void requestUpdate()
    {
        Player player = gameModel.getPlayer();
        Map<Point, RuntimeRoom> world = gameModel.getWorld();
        Direction[] directions = movementLocked ? new Direction[0] : worldModel.getPossibleDirections(world, player.getPoint());
        worldViewAction.updateAvailableMovementsPanel(directions);
        RuntimeRoom[][] rooms = worldModel.updateWorldInfo(world, player.getPoint());
        worldViewAction.updateWorldMapPanel(rooms);
    }
}