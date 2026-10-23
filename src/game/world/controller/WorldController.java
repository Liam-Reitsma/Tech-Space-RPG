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
import java.util.Set;

public class WorldController implements WorldAction, Updatable
{
    private boolean movementLocked;

    private Point previousPoint;
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

    /**
     * Attempts to move the player to the next room, ignoring invalid or locked movement.
     * On success, updates the current room and notifies the room listener.
     *
     * @param direction The direction in which the player attempts to move.
     */
    @Override
    public Set<RuntimeRoom> requestGetUnsavedDiscoveredRooms()
    {
        return worldModel.getUnsavedDiscoveredRooms();
    }
    
    @Override
    public void requestClearUnsavedDiscoveredRooms()
    {
        worldModel.clearUnsavedDiscoveredRooms();
    }
    
    @Override
    public void requestMarkDiscoveredRoomSaved(RuntimeRoom room)
    {
        worldModel.markDiscoveredRoomSaved(room);
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
        
        previousPoint = player.getPoint();
        
        world.get(playerLocation).clearCurrent();
        player.movePoint(direction);
        
        Point newLocation = player.getPoint();
        RuntimeRoom nextRoom = world.get(newLocation);
        
        nextRoom.setCurrent();
        
        if(roomListener != null)
            roomListener.onRoomEntered(world.get(newLocation));
        
        gameAction.requestGameUpdate();
    }

    /**
     * Updates the available movement directions, displaying none if movement is locked.
     * Updates the world map and marks nearby unseen rooms as seen.
     */
    @Override
    public void requestReturnPreviousRoom()
    {
        if(previousPoint == null)
            return;
        
        Player player = gameModel.getPlayer();
        Map<Point, RuntimeRoom> world = gameModel.getWorld();
        
        RuntimeRoom currentRoom = world.get(player.getPoint());
        RuntimeRoom previousRoom = world.get(previousPoint);
        Direction returnDirection = previousPoint.returnRelativeDirection(player.getPoint());
        
        if(currentRoom == null || previousRoom == null || returnDirection == null)
            return;
        
        
        currentRoom.clearCurrent();
        player.movePoint(returnDirection);
        previousRoom.setCurrent();
        
        previousPoint = null;
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