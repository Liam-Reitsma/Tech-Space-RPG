package game.world.controller;

import game.maingame.controller.GameAction;
import game.maingame.model.GameModel;
import game.Updatable;
import game.player.model.Player;
import game.world.model.Direction;
import game.world.model.Point;
import game.world.model.WorldModel;
import game.world.view.WorldViewAction;
import game.world.model.room.RuntimeRoom;

import java.util.Map;

public class WorldController implements WorldAction, Updatable
{
    private boolean movementLocked;

    private RoomListener roomListener;
    
    private final GameModel gameModel;
    private final WorldModel worldModel;
    
    private GameAction gameAction;
    
    private WorldViewAction worldViewAction;
    
    public WorldController(GameModel gModel)
    {
        gameModel = gModel;
        worldModel = new WorldModel();
    }
    
    public void setGameAction(GameAction gAction) { gameAction = gAction;}
    
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
        if(movementLocked)
            return;
        
        Player player = gameModel.getPlayer();
        Point playerLocation = player.getPoint();
        Map<Point, RuntimeRoom> world = gameModel.getWorld();
        
        boolean availableDirection = false;
        for(Direction possibleDirections : worldModel.getPossibleDirections(world, playerLocation))
            if(possibleDirections == direction)
            {
                availableDirection = true;
                break;
            }
        
        if(!availableDirection)
            return;
        
        world.get(playerLocation).clearCurrent();
        player.movePoint(direction);
        
        Point newLocation = player.getPoint();
        RuntimeRoom nextRoom = world.get(newLocation);
        
        nextRoom.setCurrent();
        
        if(roomListener != null)
            roomListener.onRoomEntered(world.get(newLocation));
        
        gameAction.requestGameUpdate();
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